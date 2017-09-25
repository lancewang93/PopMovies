package com.lance.popmovies.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.lance.popmovies.db.Movie;
import com.lance.popmovies.db.MovieLab;
import com.lance.popmovies.network.okhttp.NetworkUtil;
import com.lance.popmovies.ui.activity.DetailPagerActivity;
import com.lance.popmovies.ui.adapter.MainAdapter;
import com.lance.popmovies.utils.MainLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Void>, MainAdapter.ListItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mMainRecyclerView;
    private MainAdapter mMainAdapter;
    private List<Movie> mMovieList;

    private SwipeRefreshLayout mMainRefresh;
    private TextView mMainErrorTextView;
    private ContentLoadingProgressBar mMainLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mMainErrorTextView = view.findViewById(R.id.tv_main_error);
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
        return view;
    }

    private void initData() {
        if (NetworkUtil.isNetworkAvailableAndConnected(getContext())) {
            showSuccessView();
            getLoaderManager().initLoader(0, null, this);
        } else {
            showErrorView();
        }
    }

    private void showSuccessView() {
        mMainErrorTextView.setVisibility(View.INVISIBLE);
        mMainRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorView() {
        mMainRecyclerView.setVisibility(View.INVISIBLE);
        mMainErrorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle args) {
        mMainRecyclerView.setVisibility(View.INVISIBLE);
        mMainLoading.setVisibility(View.VISIBLE);
        if (i == 0) {
            return new MainLoader(getContext(), getString(R.string.loader_popular));
        } else if (i == 1) {
            return new MainLoader(getContext(), getString(R.string.loader_top_rated));
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        mMainLoading.setVisibility(View.INVISIBLE);
        mMovieList = MovieLab.get(getContext()).getMovieList();
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
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (NetworkUtil.isNetworkAvailableAndConnected(getContext())) {
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

    private void onSortOderChange(int i) {
        switch (i) {
            case 0:
                getLoaderManager().restartLoader(i, null, this);
                break;
            case 1:
                getLoaderManager().restartLoader(i, null, this);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        //单页面
        //Intent intent = DetailActivity.newIntent(getContext(), mMovieList.get(clickedItemIndex));
        Intent intent = DetailPagerActivity.newIntent(getContext(), mMovieList.get(clickedItemIndex));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        initData();
        mMainRefresh.setRefreshing(false);
    }
}
