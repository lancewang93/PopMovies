package com.lance.popmovies.utils;

import com.lance.popmovies.bean.Movie;
import com.lance.popmovies.bean.Review;
import com.lance.popmovies.bean.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class JSONDataUtils {

    //将网上获取的JSON电影列表数据转换成电影列表
    public static List<Movie> parseJSONDataToMovieList(String jsonData) {
        List<Movie> movies = new ArrayList<>();
        //防止网上获取的json数据为空
        if (jsonData == null) {
            return movies;
        }
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

    //将网上获取的JSON电影预告片数据转换成预告片列表
    public static List<Trailer> parseJSONDataToTrailerList(String jsonData) {
        List<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject trailerObject = jsonArray.getJSONObject(i);
                Trailer trailer = new Trailer();
                trailer.setVideoType(trailerObject.getString("type"));
                trailer.setVideoName(trailerObject.getString("name"));
                trailer.setVideoKey(trailerObject.getString("key"));
                trailers.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }

    //将网上获取的JSON电影评论数据转换成评论列表
    public static List<Review> parseJSONDataToReviewList(String jsonData) {
        List<Review> reviews = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject trailerObject = jsonArray.getJSONObject(i);
                Review review = new Review();
                review.setReviewAuthor(trailerObject.getString("author"));
                review.setReviewContent(trailerObject.getString("content"));
                reviews.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
