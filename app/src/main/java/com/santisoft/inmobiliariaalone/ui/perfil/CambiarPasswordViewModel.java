package com.santisoft.inmobiliariaalone.ui.perfil;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.model.CambioPasswordRequest;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarPasswordViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private final MutableLiveData<String> tipoMensaje = new MutableLiveData<>(); // success, warning, error

    public CambiarPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getMensaje() { return mensaje; }
    public LiveData<String> getTipoMensaje() { return tipoMensaje; }

    //   Cambiar Contraseña

    public void cambiarPassword(String actual, String nueva, String repetir) {
        // Validaciones
        if (actual == null || actual.trim().isEmpty() ||
                nueva == null || nueva.trim().isEmpty() ||
                repetir == null || repetir.trim().isEmpty()) {
            mensaje.postValue("Todos los campos son obligatorios.");
            tipoMensaje.postValue("warning");
            return;
        }

        if (nueva.length() < 6) {
            mensaje.postValue("La nueva contraseña debe tener al menos 6 caracteres.");
            tipoMensaje.postValue("warning");
            return;
        }

        if (!nueva.equals(repetir)) {
            mensaje.postValue("Las contraseñas nuevas no coinciden.");
            tipoMensaje.postValue("warning");
            return;
        }

        if (nueva.equals(actual)) {
            mensaje.postValue("La nueva contraseña no puede ser igual a la actual.");
            tipoMensaje.postValue("warning");
            return;
        }

        // Llamada a la API
        loading.postValue(true);
        CambioPasswordRequest req = new CambioPasswordRequest(actual, nueva);

        ApClient.getInmobiliariaService(getApplication())
                .cambiarPassword(req)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        loading.postValue(false);
                        if (response.isSuccessful()) {
                            mensaje.postValue("Contraseña actualizada correctamente.");
                            tipoMensaje.postValue("success");
                        } else if (response.code() == 400) {
                            mensaje.postValue("La contraseña actual es incorrecta.");
                            tipoMensaje.postValue("error");
                        } else {
                            mensaje.postValue("No se pudo actualizar la contraseña.");
                            tipoMensaje.postValue("error");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        loading.postValue(false);
                        mensaje.postValue("Error de conexión: " + t.getMessage());
                        tipoMensaje.postValue("error");
                    }
                });
    }
}
