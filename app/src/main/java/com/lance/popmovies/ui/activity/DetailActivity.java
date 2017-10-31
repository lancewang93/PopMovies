package com.lance.popmovies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.ui.fragment.DetailFragment;

public class DetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_MOVIE = DetailActivity.class.getSimpleName();

    private Movie mMovie;

    @Override
    protected Fragment createFragment() {
        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        return DetailFragment.newInstance(mMovie);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra(EXTRA_MOVIE, movie);
        return i;
    }
}
