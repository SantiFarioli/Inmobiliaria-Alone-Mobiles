package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Response;

public class AgregarInmuebleViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> exito = new MutableLiveData<>(false);
    private final MutableLiveData<String>  error = new MutableLiveData<>("");

    public AgregarInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getExito() { return exito; }
    public LiveData<String>  getError() { return error; }

    public void crear(Context ctx, Inmueble nuevo) {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(ctx);
        api.inmuebleCreate(nuevo).enqueue(new retrofit2.Callback<Inmueble>() {
            @Override public void onResponse(Call<Inmueble> call, Response<Inmueble> res) {
                if (res.isSuccessful()) { exito.postValue(true); }
                else { error.postValue("No se pudo crear el inmueble"); }
            }
            @Override public void onFailure(Call<Inmueble> call, Throwable t) {
                error.postValue("Error de red: " + t.getMessage());
            }
        });
    }
}
