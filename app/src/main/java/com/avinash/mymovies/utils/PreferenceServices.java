package com.avinash.mymovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;

import static com.avinash.mymovies.utils.GetPrefs.PREFS_NAME;


public class PreferenceServices {

    public static final String BOOKMARLLIST = "BOOKMARLLIST";

    private static PreferenceServices mSingleton = new PreferenceServices();
    private static Context mContext;
    private boolean showShimmer;

    public PreferenceServices() {
    }

    public static PreferenceServices instance() {
        return mSingleton;
    }

    public static PreferenceServices getInstance() {
        return mSingleton;
    }

    public static void init(Context context) {
        mContext = context;
    }

    public SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void clearAllPreferance() {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.clear();
        editor.apply();
    }

    public ArrayList getBookmarllist() {
        Gson gson = new Gson();
        String arrayListString = getPrefs().getString(BOOKMARLLIST, "");
        Type type = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();
        ArrayList<HashMap<String, String>> arrayList = gson.fromJson(arrayListString, type);
        return arrayList;
    }

    public void setBookmarllist(ArrayList arrayList) {
        SharedPreferences.Editor editor = getPrefs().edit();
        Gson gson = new Gson();
        String arrayList1 = gson.toJson(arrayList);
        editor.putString(BOOKMARLLIST, arrayList1);
        editor.apply();
    }
}
