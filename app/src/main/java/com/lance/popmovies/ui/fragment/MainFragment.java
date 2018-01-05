package com.lance.popmovies.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lance.popmovies.R;
import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.sync.SyncUtils;
import com.lance.popmovies.ui.adapter.MainAdapter;
import com.lance.popmovies.utils.MainLoader;
import com.lance.popmovies.utils.NetworkUtils;
import com.lance.popmovies.utils.PreferenceUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class MainFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Movie>>,
        MainAdapter.ListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private RecyclerView mMainRecyclerView;
    private MainAdapter mMainAdapter;
    private List<Movie> mMovieList;

    private SwipeRefreshLayout mMainRefresh;
    private TextView mErrorTextView;
    private ContentLoadingProgressBar mMainLoading;

    private int mRequestType;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onMovieSelected(Movie movie);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法
        setHasOptionsMenu(true);
        mRequestType = PreferenceUtils.getMainSortBy(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mErrorTextView = view.findViewById(R.id.tv_error);
        mMainLoading = view.findViewById(R.id.pb_main_loading);
        mMainRefresh = view.findViewById(R.id.swipe_refresh);

        mMainRefresh.setColorSchemeResources(R.color.colorPrimary);
        mMainRefresh.setOnRefreshListener(this);

        mMainRecyclerView = view.findViewById(R.id.rcv_main);
        LinearLayoutManager layoutManager;
        DisplayMetrics metric = getResources().getDisplayMetrics();
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        int widthDp = Math.round(width / (metric.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        int heightDp = Math.round(height / (metric.ydpi / DisplayMetrics.DENSITY_DEFAULT));

        int dp;
        if (widthDp >= heightDp) {
            dp = heightDp;
        } else {
            dp = widthDp;
        }
        //有些最小是600dp的计算出来是552dp
        //所以只能大于550，不然应该是大于等于600
        //比如7_WSVGA_Tablet
        if (dp >= 550) {
            layoutManager = new GridLayoutManager(getContext(), 3);
        } else {
            layoutManager = new GridLayoutManager(getContext(), 2);
        }
        mMainRecyclerView.setLayoutManager(layoutManager);
        mMainRecyclerView.setHasFixedSize(true);
        mMainAdapter = new MainAdapter(view.getContext(), this);
        mMainRecyclerView.setAdapter(mMainAdapter);

        initData();
        SyncUtils.scheduleChargingReminder(view.getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        if (mRequestType == 2) {
            getLoaderManager().restartLoader(mRequestType, null, this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    private void initData() {
        getLoaderManager().restartLoader(mRequestType, null, this);
//        if (mRequestType != 2) {
//            if (NetworkUtils.isNetworkAvailableAndConnected(getContext())) {
//                showSuccessView();
//                getLoaderManager().initLoader(mRequestType, null, this);
//            } else {
//                showErrorView();
//            }
//        } else {
//            getLoaderManager().restartLoader(mRequestType, null, this);
//        }
    }

    @Override
    public Loader onCreateLoader(int requestType, Bundle args) {
        mMainRecyclerView.setVisibility(View.INVISIBLE);
        mMainLoading.setVisibility(View.VISIBLE);
        return new MainLoader(getContext(), requestType);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mMovieList = movies;
        updateUI();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMainAdapter.refreshMovieList(null);
    }

    private void updateUI() {
        mMainLoading.setVisibility(View.INVISIBLE);
        if (mMovieList != null && !mMovieList.isEmpty()) {
            mMainAdapter.refreshMovieList(mMovieList);
            showSuccessView();
        } else {
            showErrorView();
        }
    }

    private void showSuccessView() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mMainRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorView() {
        mMainRecyclerView.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        MenuItem spinnerItem = menu.findItem(R.id.action_spinner);
        Spinner spinner = (Spinner) spinnerItem.getActionView();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_order, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(mRequestType);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PreferenceUtils.setMainSortBy(view.getContext(), i);
                onSortOderChange(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void onSortOderChange(int requestType) {
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        if (loaderManager.getLoader(requestType) == null) {
            loaderManager.initLoader(requestType, null, this);
        } else {
            loaderManager.restartLoader(requestType, null, this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (NetworkUtils.isNetworkAvailableAndConnected(getContext())) {
            mCallbacks.onMovieSelected(mMovieList.get(clickedItemIndex));
        } else {
            Toast.makeText(getContext(), "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        initData();
        mMainRefresh.setRefreshing(false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (PreferenceUtils.KEY_MAIN_SORT_BY.equals(key)) {
            mRequestType = PreferenceUtils.getMainSortBy(getContext());
            initData();
        }
    }
}
