package com.lance.popmovies.network.okhttp;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.lance.popmovies.Key.TheMovieDbKey;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class NetworkUtil {
    //// TODO: Change the variable to your The Movie Db Key
    public static final String API_KEY = TheMovieDbKey;

    public static String BASE_THEMOVIEDB_URL = "https://api.themoviedb.org/3/movie/";

    public static String sendRequestWithOkHttp(String typeString) {
        String requestURL = BASE_THEMOVIEDB_URL + typeString;

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL + "?api_key=" + API_KEY)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            return responseData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
