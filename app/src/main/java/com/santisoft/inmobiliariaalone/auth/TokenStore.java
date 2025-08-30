package com.santisoft.inmobiliariaalone.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStore {
    private static final String PREF = "MyAppPrefs";
    private static final String KEY  = "jwt_token";

    public static void save(Context c, String token){
        c.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().putString(KEY, token).apply();
    }
    public static String get(Context c){
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY, "");
    }
    public static void clear(Context c){
        c.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit().remove(KEY).apply();
    }
}
