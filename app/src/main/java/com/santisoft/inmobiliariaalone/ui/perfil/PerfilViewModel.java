package com.santisoft.inmobiliariaalone.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.santisoft.inmobiliariaalone.model.Propietario;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> propietario;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        cargarDatosPerfil();
    }

    public LiveData<Propietario> getPropietario() {
        if (propietario == null) {
            propietario = new MutableLiveData<>();
        }
        return propietario;
    }

    private void cargarDatosPerfil() {
        SharedPreferences prefs = getApplication().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);
        if (token != null) {
            ApClient.InmobliariaService api = ApClient.getInmobiliariaService();
            Call<Propietario> call = api.obtenerPerfil("Bearer " + token);
            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        propietario.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    //Manejar fallo
                }
            });
        }
    }
}