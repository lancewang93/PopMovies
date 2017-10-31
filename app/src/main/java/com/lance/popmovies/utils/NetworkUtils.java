package com.lance.popmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.lance.popmovies.Key.TheMovieDbKey;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class NetworkUtils {
    //// TODO: Change the variable to your The Movie Db Key
    private static final String API_KEY = TheMovieDbKey;

    private static String BASE_THEMOVIEDB_URL = "https://api.themoviedb.org/3/movie/";

    public static String sendMainRequest(String movieType) {
        String requestURL = BASE_THEMOVIEDB_URL + movieType + "?api_key=" + API_KEY;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String sendDetailRequest(String typeString, int movieId) {
        String requestURL = BASE_THEMOVIEDB_URL + movieId + "/" + typeString + "?api_key=" + API_KEY;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            return responseData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendTrailerRequest(int movieId) {
        return sendDetailRequest("videos", movieId);
    }

    public static String sendReviewRequest(int movieId) {
        return sendDetailRequest("reviews", movieId);
    }

    public static boolean isNetworkAvailableAndConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
