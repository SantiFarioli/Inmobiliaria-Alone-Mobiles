package com.santisoft.inmobiliariaalone.ui.perfil;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.model.CambioPasswordRequest;
import com.santisoft.inmobiliariaalone.model.EventoMensaje;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarPasswordViewModel extends AndroidViewModel {

    private final MutableLiveData<EventoMensaje> evento = new MutableLiveData<>();
    private final MutableLiveData<Boolean> exito = new MutableLiveData<>(false);

    public CambiarPasswordViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<EventoMensaje> getEvento() { return evento; }
    public LiveData<Boolean> getExito() { return exito; }

    public void cambiarPassword(String actual, String nueva, String confirmar) {
        exito.setValue(false);

        //  Validaciones
        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            evento.setValue(new EventoMensaje("Todos los campos son obligatorios", EventoMensaje.Tipo.WARNING));
            return;
        }
        if (!nueva.equals(confirmar)) {
            evento.setValue(new EventoMensaje("Las contraseñas no coinciden", EventoMensaje.Tipo.WARNING));
            return;
        }
        if (nueva.length() < 6) {
            evento.setValue(new EventoMensaje("La nueva contraseña debe tener al menos 6 caracteres", EventoMensaje.Tipo.WARNING));
            return;
        }

        //  Petición API
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.cambiarPassword(new CambioPasswordRequest(actual, nueva))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> c, Response<Void> r) {
                        if (r.isSuccessful()) {
                            evento.postValue(new EventoMensaje("Contraseña actualizada correctamente", EventoMensaje.Tipo.SUCCESS));
                            exito.postValue(true);
                        } else {
                            evento.postValue(new EventoMensaje("Contraseña actual incorrecta", EventoMensaje.Tipo.ERROR));
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> c, Throwable t) {
                        evento.postValue(new EventoMensaje("Error de conexión: " + t.getMessage(), EventoMensaje.Tipo.ERROR));
                    }
                });
    }
}
