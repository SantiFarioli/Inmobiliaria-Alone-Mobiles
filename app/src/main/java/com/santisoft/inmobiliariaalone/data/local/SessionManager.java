package com.santisoft.inmobiliariaalone.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.santisoft.inmobiliariaalone.model.LoginResponse;
import com.santisoft.inmobiliariaalone.model.Propietario;

public class SessionManager {

    private static final String PREF_NAME = "session_prefs";
    private static final String KEY_ID = "id_propietario";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_AVATAR = "foto_perfil";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // 🔹 Guardar sesión después del login
    public void saveSession(LoginResponse login) {
        if (login != null) {
            // Agregamos "Bearer" por compatibilidad con Retrofit
            editor.putString(KEY_TOKEN, "Bearer " + login.getToken());

            // Si tu backend devuelve más campos, los guardamos también
            if (login.getEmail() != null) editor.putString(KEY_EMAIL, login.getEmail());
            if (login.getNombre() != null) editor.putString(KEY_NOMBRE, login.getNombre());
            editor.apply();
        }
    }

    // 🔹 Guardar info adicional del perfil (cuando obtienes el Propietario)
    public void updateProfileData(Propietario p) {
        if (p != null) {
            editor.putInt(KEY_ID, p.getIdPropietario());
            if (p.getNombre() != null) editor.putString(KEY_NOMBRE, p.getNombre());
            if (p.getEmail() != null) editor.putString(KEY_EMAIL, p.getEmail());
            if (p.getFotoPerfil() != null) editor.putString(KEY_AVATAR, p.getFotoPerfil());
            editor.apply();
        }
    }

    // 🔹 Getters
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public int getIdPropietario() {
        return prefs.getInt(KEY_ID, 0);
    }

    public String getNombre() {
        return prefs.getString(KEY_NOMBRE, "");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getFotoPerfil() {
        return prefs.getString(KEY_AVATAR, null);
    }

    // 🔹 Limpiar sesión (logout)
    public void clear() {
        editor.clear().apply();
    }
}
