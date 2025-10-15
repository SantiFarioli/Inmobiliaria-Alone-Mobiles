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

    public enum TipoMensaje { SUCCESS, WARNING, ERROR }

    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private final MutableLiveData<Boolean> exito = new MutableLiveData<>(false);
    private final MutableLiveData<TipoMensaje> tipoMensaje = new MutableLiveData<>();

    public CambiarPasswordViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<String> getMensaje() { return mensaje; }
    public LiveData<Boolean> getExito() { return exito; }
    public LiveData<TipoMensaje> getTipoMensaje() { return tipoMensaje; }

    public void cambiarPassword(String actual, String nueva, String confirmar) {
        if (actual.isEmpty() || nueva.isEmpty() || confirmar.isEmpty()) {
            tipoMensaje.setValue(TipoMensaje.WARNING);
            mensaje.setValue("Todos los campos son obligatorios");
            return;
        }
        if (!nueva.equals(confirmar)) {
            tipoMensaje.setValue(TipoMensaje.WARNING);
            mensaje.setValue("Las contraseñas no coinciden");
            return;
        }
        if (nueva.length() < 6) {
            tipoMensaje.setValue(TipoMensaje.WARNING);
            mensaje.setValue("La nueva contraseña debe tener al menos 6 caracteres");
            return;
        }

        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.cambiarPassword(new CambioPasswordRequest(actual, nueva))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> c, Response<Void> r) {
                        if (r.isSuccessful()) {
                            tipoMensaje.postValue(TipoMensaje.SUCCESS);
                            mensaje.postValue("Contraseña actualizada correctamente");
                            exito.postValue(true);
                        } else {
                            tipoMensaje.postValue(TipoMensaje.ERROR);
                            mensaje.postValue("Contraseña actual incorrecta");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> c, Throwable t) {
                        tipoMensaje.postValue(TipoMensaje.ERROR);
                        mensaje.postValue("Error de conexión: " + t.getMessage());
                    }
                });

        }
    }
