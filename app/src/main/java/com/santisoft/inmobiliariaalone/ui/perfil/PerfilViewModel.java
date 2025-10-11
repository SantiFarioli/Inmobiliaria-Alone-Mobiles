package com.santisoft.inmobiliariaalone.ui.perfil;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.model.Propietario;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private final MutableLiveData<Propietario> propietario = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> exito = new MutableLiveData<>(false);

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        cargarDatosPerfil();
    }

    public LiveData<Propietario> getPropietario() { return propietario; }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getExito() { return exito; }

    public void cargarDatosPerfil() {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.obtenerPerfil().enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> res) {
                if (res.isSuccessful() && res.body() != null) {
                    Propietario p = res.body();
                    propietario.postValue(p);

                    SessionManager session = new SessionManager(getApplication());
                    session.updateProfileData(p);
                } else {
                    error.postValue("No se pudo cargar el perfil.");
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                error.postValue("Error al conectar: " + t.getMessage());
            }
        });
    }

    public void actualizar(Propietario body) {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.propietarioUpdate(body).enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> c, Response<Propietario> r) {
                if (r.isSuccessful() && r.body() != null) {
                    Propietario actualizado = r.body();
                    propietario.postValue(actualizado);
                    exito.postValue(true);

                    SessionManager session = new SessionManager(getApplication());
                    session.updateProfileData(actualizado);
                } else {
                    error.postValue("No se pudo actualizar el perfil");
                }
            }

            @Override
            public void onFailure(Call<Propietario> c, Throwable t) {
                error.postValue("Error de conexión: " + t.getMessage());
            }
        });
    }
}
