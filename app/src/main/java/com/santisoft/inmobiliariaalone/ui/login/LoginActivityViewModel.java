package com.santisoft.inmobiliariaalone.ui.login;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.santisoft.inmobiliariaalone.MainActivity;
import com.santisoft.inmobiliariaalone.auth.TokenStore;          // ⬅️ IMPORTANTE
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
        // Login usa el service SIN interceptor (todavía no hay token)
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService();
        Log.d("salida", "Llamando a la API con el email: " + email);

        api.login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    // ⬇️ Guardamos el token para que el interceptor lo use en adelante
                    TokenStore.save(getApplication(), token);

                    // (Opcional) No loguees el token completo en producción
                    Log.d("Login", "Token guardado (prefix): " + (token != null ? token.substring(0, Math.min(12, token.length())) + "..." : "null"));
                    Toast.makeText(getApplication(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                    iniciarMainActivity();
                } else {
                    Log.d("Login", "Fallo: " + response.code() + " - " + response.message());
                    Toast.makeText(getApplication(), "Datos incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.d("Login", "Error: " + t.getMessage());
                Toast.makeText(getApplication(), "Error de servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarMainActivity() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        // Limpia el back stack para que no vuelva a Login con "Atrás"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplication().startActivity(intent);
    }
}
