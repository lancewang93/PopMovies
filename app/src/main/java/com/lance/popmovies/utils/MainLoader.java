package com.lance.popmovies.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.lance.popmovies.db.Movie;
import com.lance.popmovies.db.MovieLab;
import com.lance.popmovies.network.okhttp.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class MainLoader extends AsyncTaskLoader<Void> {

    private String mRequestType;

    public MainLoader(Context context, String requestType) {
        super(context);
        mRequestType = requestType;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Void loadInBackground() {
        String responseData = NetworkUtil.sendRequestWithOkHttp(mRequestType);

        MovieLab movieLab = MovieLab.get(getContext());
        movieLab.setMovieList(parseJSON(responseData));
//        return movieLab.getMovieList();
        return null;
    }

    private List<Movie> parseJSON(String jsonData) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieObject = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setId(movieObject.getInt("id"));
                movie.setTitle(movieObject.getString("title"));
                movie.setBackdrop_path(movieObject.getString("backdrop_path"));
                movie.setOverview(movieObject.getString("overview"));
                movie.setVote_average(movieObject.getDouble("vote_average"));
                movie.setRelease_date(movieObject.getString("release_date"));
                movie.setPoster_path(movieObject.getString("poster_path"));
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
