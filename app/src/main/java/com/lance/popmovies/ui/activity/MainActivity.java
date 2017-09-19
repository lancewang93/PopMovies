package com.lance.popmovies.ui.activity;

import android.support.v4.app.Fragment;

import com.lance.popmovies.ui.fragment.MainFragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }



}
