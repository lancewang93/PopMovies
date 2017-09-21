package com.lance.popmovies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.lance.popmovies.R;
import com.lance.popmovies.db.Movie;
import com.lance.popmovies.db.MovieLab;
import com.lance.popmovies.ui.fragment.DetailFragment;

import java.util.List;

public class DetailPagerActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = DetailActivity.class.getSimpleName();


    private ViewPager mViewPager;
    private List<Movie> mMovieList;

    public static Intent newIntent(Context context, Movie movie) {
        Intent i = new Intent(context, DetailPagerActivity.class);
        i.putExtra(EXTRA_MOVIE, movie);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_pager);
        Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        mViewPager = (ViewPager) findViewById(R.id.activity_movie_view_pager);
        mMovieList = MovieLab.get(this).getMovieList();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Movie movie = mMovieList.get(position);
                return DetailFragment.newInstance(movie);
            }

            @Override
            public int getCount() {
                return mMovieList.size();
            }
        });
        //以下的for循环作用是：ViewPager默认值显示PageAdapter中的第一个列表项
        //要显示列表项，可设置ViewPager当前要显示的列表项为movie数组中指定位置的列表项
        for (int i = 0; i < mMovieList.size(); i++) {
            if (mMovieList.get(i).getId() == movie.getId()) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
