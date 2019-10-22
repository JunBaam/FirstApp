package com.example.afinal;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared {


    public static String SHARED_NAME = "rebulid_pref";
    private static final String DEFAULT_VALUE_STRING="";

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
    }

    //데이터를저장
    public static void setString(Context context, String key, String value) {
        SharedPreferences preferences = getPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    //데이터를 가져옴.
    public static String getString(Context context, String key) {
        SharedPreferences preferences = getPreference(context);
        String value = preferences.getString(key, DEFAULT_VALUE_STRING);
        return value;
    }

    //데이터를 지움.
    public static void removeKey(Context context, String key) {
        SharedPreferences preferences = getPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }

//초기화
    public static void clear(Context context) {
        SharedPreferences preferences = getPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}