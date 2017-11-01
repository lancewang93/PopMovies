package com.lance.popmovies.ui.fragment;

import android.content.Intent;
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

import com.lance.popmovies.R;
import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.sync.SyncUtils;
import com.lance.popmovies.ui.activity.DetailActivity;
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
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
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
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    private void initData() {
        if (NetworkUtils.isNetworkAvailableAndConnected(getContext())) {
            showSuccessView();
            if (mRequestType == 2) {
                getLoaderManager().restartLoader(mRequestType, null, this);
            } else {
                getLoaderManager().initLoader(mRequestType, null, this);
            }
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
    public Loader onCreateLoader(int requestType, Bundle args) {
        mMainRecyclerView.setVisibility(View.INVISIBLE);
        mMainLoading.setVisibility(View.VISIBLE);
        return new MainLoader(getContext(), requestType);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        mMainLoading.setVisibility(View.INVISIBLE);
        mMovieList = movies;
        if (mMovieList != null && !mMovieList.isEmpty()) {
            showSuccessView();
            mMainAdapter.refreshMovieList(mMovieList);
        } else {
            showErrorView();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mMainAdapter.refreshMovieList(null);
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
                if (NetworkUtils.isNetworkAvailableAndConnected(getContext())) {
                    onSortOderChange(i);
                } else {
                    showErrorView();
                }
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
        //单页面
        Intent intent = DetailActivity.newIntent(getContext(), mMovieList.get(clickedItemIndex));
        //滑动页面
        //Intent intent = DetailPagerActivity.newIntent(getContext(), mMovieList.get(clickedItemIndex));
        startActivity(intent);
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
