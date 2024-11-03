package com.santisoft.inmobiliariaalone.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.santisoft.inmobiliariaalone.MainActivity;
import com.santisoft.inmobiliariaalone.model.LoginResponse;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void llamarIniciarSesion(String email, String password) {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService();
        Call<LoginResponse> call = api.login(email, password);
        Log.d("salida", "Llamando a la API con el email: " + email);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d("salida", "Inicio de sesión exitoso, token: " + loginResponse.getToken());
                    guardarToken(loginResponse.getToken());
                    Toast.makeText(getApplication(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                    iniciarMainActivity();
                } else {
                    Log.d("LoginActivityViewModel", "Inicio de sesión fallido: " + response.code() + " - " + response.message());
                    Toast.makeText(getApplication(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable throwable) {
                Log.d("LoginActivityViewModel", "Fallo en la llamada a la API: " + throwable.getMessage());
                Toast.makeText(getApplication(), "Error de servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarToken(String token) {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jwt_token", token);
        editor.apply();
        Log.d("salida", "Token guardado: " + token);
    }

    private void iniciarMainActivity() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);
    }
}