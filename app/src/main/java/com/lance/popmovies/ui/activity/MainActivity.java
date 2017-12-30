package com.lance.popmovies.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.lance.popmovies.R;
import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.ui.fragment.DetailFragment;
import com.lance.popmovies.ui.fragment.MainFragment;

public class MainActivity extends SingleFragmentActivity implements MainFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = DetailActivity.newIntent(this, movie);
            startActivity(intent);
        } else {
            Fragment newDetail = DetailFragment.newInstance(movie);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }
}
