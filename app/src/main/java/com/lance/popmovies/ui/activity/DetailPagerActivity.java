package com.lance.popmovies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.lance.popmovies.R;
import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.bean.MovieLab;
import com.lance.popmovies.ui.fragment.DetailFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailPagerActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE = DetailPagerActivity.class.getSimpleName();

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mBackdropImageView;

    private ViewPager mViewPager;
    private List<Movie> mMovieList;
    private Movie mMovie;

    public static Intent newIntent(Context context, Movie movie) {
        Intent i = new Intent(context, DetailPagerActivity.class);
        i.putExtra(EXTRA_MOVIE, movie);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_pager);
        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBackdropImageView = (ImageView) findViewById(R.id.iv_detail_backdrop);
        mViewPager = (ViewPager) findViewById(R.id.activity_movie_view_pager);

        initCollapsingToolBar();
        initViewPager();
    }

    private void initViewPager() {
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

        //ViewPager滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMovie = mMovieList.get(position);
                Picasso.with(DetailPagerActivity.this)
                        .load("http://image.tmdb.org/t/p/w780/" + mMovieList.get(position).getBackdrop_path())
                        .into(mBackdropImageView);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //以下的for循环作用是：ViewPager默认值显示PageAdapter中的第一个列表项
        //要显示列表项，可设置ViewPager当前要显示的列表项为movie数组中指定位置的列表项
        for (int i = 0; i < mMovieList.size(); i++) {
            if (mMovieList.get(i).getId() == mMovie.getId()) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    private void initCollapsingToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitleEnabled(true);
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w780/" + mMovie.getBackdrop_path())
                .into(mBackdropImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        String mineType = "text/plain";
        String title = getString(R.string.share_title);
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mineType)
                .setText(getString(R.string.share_from)
                        + mMovie.getTitle()
                        + getString(R.string.share_release_date)
                        + mMovie.getRelease_date()
//                        + getString(R.string.share_overview)
                        + mMovie.getOverview())
                .setChooserTitle(title)
                .startChooser();
    }
}
