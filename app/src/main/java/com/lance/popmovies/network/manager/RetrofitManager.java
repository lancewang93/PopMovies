package com.lance.popmovies.network.manager;

import com.lance.popmovies.Key;
import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.network.service.TheMovieDbService;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public class RetrofitManager {

    public static final String BASE_THEMOVIEDB_URL = "https://api.themoviedb.org/3/movie/";

    private final TheMovieDbService mTheMovieDbService;

    public static RetrofitManager builder() {
        return new RetrofitManager();
    }

    private RetrofitManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_THEMOVIEDB_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mTheMovieDbService = retrofit.create(TheMovieDbService.class);
    }

    public Observable<List<Movie>> getPopular() {
        return mTheMovieDbService.getPopular(Key.TheMovieDbKey);
    }

    public Observable<List<Movie>> getTopRated() {
        return mTheMovieDbService.getTopRated(Key.TheMovieDbKey);
    }

    public Observable<Movie> getMovieDetail(int id) {
        return mTheMovieDbService.getMovieDetail(id, Key.TheMovieDbKey);
    }
}
