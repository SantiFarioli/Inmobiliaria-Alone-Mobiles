package com.santisoft.inmobiliariaalone.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.santisoft.inmobiliariaalone.model.LoginResponse;

public class SessionManager {

    private static final String PREF_NAME = "session_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NOMBRE = "nombre";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveSession(LoginResponse login) {
        if (login != null) {
            editor.putString(KEY_TOKEN, "Bearer " + login.getToken());
            editor.putString(KEY_EMAIL, login.getEmail());
            editor.putString(KEY_NOMBRE, login.getNombre());
            editor.apply();
        }
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getNombre() {
        return prefs.getString(KEY_NOMBRE, "");
    }

    public void clear() {
        editor.clear().apply();
    }
}
