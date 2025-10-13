package com.santisoft.inmobiliariaalone.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.santisoft.inmobiliariaalone.MainActivity;
import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.model.LoginResponse;
import com.santisoft.inmobiliariaalone.model.Propietario;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void iniciarSesion(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplication(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApClient.getInmobiliariaService().login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SessionManager session = new SessionManager(getApplication());
                    session.saveSession(response.body()); // guarda token

                    //  Luego pedimos el perfil autorizado
                    ApClient.getInmobiliariaService(getApplication())
                            .obtenerPerfil()
                            .enqueue(new Callback<Propietario>() {
                                @Override
                                public void onResponse(@NonNull Call<Propietario> call, @NonNull Response<Propietario> res) {
                                    if (res.isSuccessful() && res.body() != null) {
                                        session.updateProfileData(res.body());
                                    }
                                    Toast.makeText(getApplication(), "Bienvenido", Toast.LENGTH_SHORT).show();
                                    irAMain();
                                }

                                @Override
                                public void onFailure(@NonNull Call<Propietario> call, @NonNull Throwable t) {
                                    Toast.makeText(getApplication(), "Error al cargar perfil", Toast.LENGTH_SHORT).show();
                                    irAMain();
                                }
                            });

                } else {
                    Toast.makeText(getApplication(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void irARecuperar(Context context) {
        Intent intent = new Intent(context, RecuperarContraseniaActivity.class);
        context.startActivity(intent);
    }

    private void irAMain() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplication().startActivity(intent);
    }
}
