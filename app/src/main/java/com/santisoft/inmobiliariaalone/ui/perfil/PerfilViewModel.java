package com.santisoft.inmobiliariaalone.ui.perfil;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.model.Propietario;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final MutableLiveData<Propietario> propietario = new MutableLiveData<>();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        cargarDatosPerfil();
    }

    public LiveData<Propietario> getPropietario() { return propietario; }
    public LiveData<String> getMensaje() { return mensaje; }
    public void clearMensaje() { mensaje.setValue(null); }

    private void publicarExito(Propietario bodyFallback) {
        // si el server devolvió NoContent o body==null, usamos lo que ya tenemos
        if (bodyFallback != null) propietario.postValue(bodyFallback);
        mensaje.postValue("Perfil actualizado");
    }

    private void publicarError() {
        mensaje.postValue("No se pudo actualizar el perfil");
    }

    private void cargarDatosPerfil() {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.obtenerPerfil().enqueue(new Callback<Propietario>() {
            @Override public void onResponse(Call<Propietario> call, Response<Propietario> res) {
                if (res.isSuccessful()) {
                    if (res.body() != null) propietario.postValue(res.body());
                } else {
                    mensaje.postValue("No se pudo cargar el perfil");
                }
            }
            @Override public void onFailure(Call<Propietario> call, Throwable t) {
                mensaje.postValue("No se pudo cargar el perfil");
            }
        });
    }

    /** Intenta actualizar primero sin id; si falla, prueba con id. */
    public void actualizarPerfil(Propietario body) {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());

        api.propietarioUpdate(body).enqueue(new Callback<Propietario>() {
            @Override public void onResponse(Call<Propietario> call, Response<Propietario> r) {
                if (r.isSuccessful()) {
                    publicarExito(r.body() != null ? r.body() : body);
                } else {
                    // fallback por id
                    api.propietarioUpdateById(body.getIdPropietario(), body)
                            .enqueue(new Callback<Propietario>() {
                                @Override public void onResponse(Call<Propietario> c2, Response<Propietario> r2) {
                                    if (r2.isSuccessful()) {
                                        publicarExito(r2.body() != null ? r2.body() : body);
                                    } else {
                                        publicarError();
                                    }
                                }
                                @Override public void onFailure(Call<Propietario> c2, Throwable t2) {
                                    publicarError();
                                }
                            });
                }
            }
            @Override public void onFailure(Call<Propietario> call, Throwable t) {
                // también probamos fallback si hubo fallo de red/serialización
                api.propietarioUpdateById(body.getIdPropietario(), body)
                        .enqueue(new Callback<Propietario>() {
                            @Override public void onResponse(Call<Propietario> c2, Response<Propietario> r2) {
                                if (r2.isSuccessful()) {
                                    publicarExito(r2.body() != null ? r2.body() : body);
                                } else {
                                    publicarError();
                                }
                            }
                            @Override public void onFailure(Call<Propietario> c2, Throwable t2) {
                                publicarError();
                            }
                        });
            }
        });
    }
}
