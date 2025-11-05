package com.santisoft.inmobiliariaalone.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.model.LoginResponse;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;
import com.santisoft.inmobiliariaalone.util.DialogEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private final MutableLiveData<DialogEvent> dialogEvent = new MutableLiveData<>();
    private final MutableLiveData<String> navegacion = new MutableLiveData<>();
    private long ultimoShake = 0;

    public LoginActivityViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<DialogEvent> getDialogEvent() { return dialogEvent; }
    public LiveData<String> getNavegacion() { return navegacion; }

    // === LOGIN REAL ===
    public void iniciarSesion(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.WARNING,
                    "Campos incompletos",
                    "Ingresá tu email y contraseña."));
            return;
        }

        dialogEvent.postValue(new DialogEvent(DialogEvent.Type.LOADING, null, "Iniciando sesión..."));

        ApClient.getInmobiliariaService().login(email, password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                dialogEvent.postValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));

                if (response.isSuccessful() && response.body() != null) {
                    SessionManager session = new SessionManager(getApplication());
                    session.saveSession(response.body());

                    dialogEvent.postValue(new DialogEvent(DialogEvent.Type.SUCCESS,
                            "Bienvenido",
                            "Inicio de sesión exitoso."));
                    navegacion.postValue("main");
                } else {
                    dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR,
                            "Error",
                            "Credenciales incorrectas o cuenta inactiva."));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR,
                        "Error de conexión",
                        "No se pudo conectar con el servidor: " + t.getMessage()));
            }
        });
    }

    // === Recuperar contraseña ===
    public void irARecuperar(Context ctx) {
        Intent intent = new Intent(ctx, RecuperarContraseniaActivity.class);
        ctx.startActivity(intent);
    }

    // === Agitación (shake) ===
    public void detectarAgitacion(float x, float y, float z) {
        double aceleracion = Math.sqrt(x * x + y * y + z * z);
        long ahora = System.currentTimeMillis();

        if (aceleracion > 14 && (ahora - ultimoShake) > 2000) {
            ultimoShake = ahora;
            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.CONFIRM,
                    "Llamar a la Inmobiliaria",
                    "¿Querés realizar la llamada a la inmobiliaria ahora?"));
        }
    }

    // === Llamada telefónica ===
    public void ejecutarLlamada(Context ctx) {
        String numero = "123456789"; // número ficticio de prueba
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));

        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            ctx.startActivity(intent);
        } else {
            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.WARNING,
                    "Permiso requerido",
                    "Debés otorgar permiso de llamadas para continuar."));
        }
    }

    // === Permiso de llamada ===
    public void verificarPermisoLlamada(int requestCode, int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dialogEvent.postValue(new DialogEvent(DialogEvent.Type.SUCCESS,
                    "Permiso otorgado",
                    "Podés llamar al agitar el teléfono"));
        }
    }
}
