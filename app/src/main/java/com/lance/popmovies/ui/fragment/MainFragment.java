package com.lance.popmovies.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.lance.popmovies.R;
import com.lance.popmovies.db.Movie;
import com.lance.popmovies.db.MovieLab;
import com.lance.popmovies.ui.activity.DetailActivity;
import com.lance.popmovies.ui.adapter.MainAdapter;
import com.lance.popmovies.utils.MainLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Void>, MainAdapter.ListItemClickListener {

    private RecyclerView mMainRecyclerView;
    private MainAdapter mMainAdapter;
    private List<Movie> mMovieList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //想让Fragment中的onCreateOptionsMenu生效必须先调用setHasOptionsMenu方法
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mMainRecyclerView = view.findViewById(R.id.rcv_main);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mMainRecyclerView.setLayoutManager(layoutManager);
        mMainRecyclerView.setHasFixedSize(true);
        mMainAdapter = new MainAdapter(view.getContext(), this);
        mMainRecyclerView.setAdapter(mMainAdapter);

        getLoaderManager().initLoader(0, null, this);
        return view;
    }

    @Override
    public Loader onCreateLoader(int i, Bundle args) {
        if (i == 0) {
            return new MainLoader(getContext(), "popular");
        } else if (i == 1) {
            return new MainLoader(getContext(), "top_rated");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        mMovieList = MovieLab.get(getContext()).getMovieList();
        if (mMovieList != null && !mMovieList.isEmpty()) {
            mMainAdapter.refreshMovieList(mMovieList);
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
                onSortOderChange(i);
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
        Intent intent = DetailActivity.newIntent(getContext(), mMovieList.get(clickedItemIndex));
        startActivity(intent);
    }
}
