package com.avinash.mymovies.utils;

import com.google.gson.Gson;

public class AppUtils {

    private static Gson gson;

    public static Gson getGson() {
        if (null==gson) {
            gson = new Gson();
        }
        return gson;
    }

}
