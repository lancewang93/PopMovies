package com.lance.popmovies.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class MovieLab {
    private static MovieLab sMovieLab;

    private List<Movie> mMovieList;

    public MovieLab(Context context) {
        mMovieList = new ArrayList<>();
    }

    public static MovieLab get(Context context) {
        if (sMovieLab == null) {
            sMovieLab = new MovieLab(context);
        }
        return sMovieLab;
    }

    public void setMovieList(List<Movie> movieList) {
        mMovieList = movieList;
    }

    public List<Movie> getMovieList() {
        return mMovieList;

    }

    public Movie getMovie(int id) {
        for (Movie movie :
                mMovieList) {
            if (movie.getId() == id) {
                return movie;
            }
        }
        return null;
    }
}
