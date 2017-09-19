package com.lance.popmovies.network.service;

import com.lance.popmovies.db.Movie;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/13 0013.
 */

public interface TheMovieDbService {

    @GET("popular")
    Observable<List<Movie>> getPopular(@Query("api_key") String apiKey);

    @GET("top_rated")
    Observable<List<Movie>> getTopRated(@Query("api_key") String apiKey);

    @GET("{id}")
    Observable<Movie> getMovieDetail(@Path("id") int id, @Query("api_key") String apiKey);
}
