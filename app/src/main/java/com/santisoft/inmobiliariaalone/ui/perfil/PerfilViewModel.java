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

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        cargarDatosPerfil();
    }

    public LiveData<Propietario> getPropietario() {
        return propietario;
    }

    private void cargarDatosPerfil() {
        // Usa el service con auth automática (interceptor)
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.obtenerPerfil().enqueue(new Callback<Propietario>() {
            @Override public void onResponse(Call<Propietario> call, Response<Propietario> res) {
                if (res.isSuccessful() && res.body()!=null) {
                    propietario.postValue(res.body());
                }
            }
            @Override public void onFailure(Call<Propietario> call, Throwable t) {
                // TODO: mostrar error si querés
            }
        });
    }
}
