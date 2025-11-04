package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {

    private final MutableLiveData<Inmueble> inmuebleLiveData = new MutableLiveData<>();
    private static final String BASE_URL = "http://192.168.0.100:5157"; // Ajustar si cambia IP
    private static final String TAG = "DetalleInmuebleVM";

    public DetalleInmuebleViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<Inmueble> getInmueble() {
        return inmuebleLiveData;
    }

    // Carga inicial desde los argumentos
    public void setInmuebleInicial(Inmueble inmueble) {
        if (inmueble != null) {
            inmueble.setFoto(normalizarUrl(inmueble.getFoto()));
            inmuebleLiveData.setValue(inmueble);
            Log.d(TAG, " Inmueble inicial cargado: " + inmueble.getDireccion());
        }
    }

    // Refresca los datos del inmueble desde la API
    public void refrescarDetalle(int idInmueble) {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.inmuebleGet(idInmueble).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Inmueble inmuebleActualizado = response.body();
                    inmuebleActualizado.setFoto(normalizarUrl(inmuebleActualizado.getFoto()));
                    inmuebleLiveData.postValue(inmuebleActualizado);
                    Log.d(TAG, " Detalle actualizado desde API para ID: " + idInmueble);
                } else {
                    Log.e(TAG, "️ Respuesta vacía o inválida al cargar detalle");
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Log.e(TAG, " Error al cargar detalle: " + t.getMessage());
            }
        });
    }

    // Normaliza la URL de la imagen
    private String normalizarUrl(String fotoUrl) {
        if (fotoUrl == null || fotoUrl.trim().isEmpty()) {
            return null;
        }
        if (!fotoUrl.startsWith("http")) {
            return BASE_URL + fotoUrl;
        }
        return fotoUrl;
    }
}
