package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;
import java.util.*;
import retrofit2.*;

public class InmueblesViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Inmueble>> lista = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public InmueblesViewModel(@NonNull Application app){ super(app); }

    public LiveData<List<Inmueble>> getLista(){ return lista; }
    public LiveData<String> getError(){ return error; }

    public void cargar() {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.inmueblesGetAll().enqueue(new Callback<List<Inmueble>>() {
            @Override public void onResponse(Call<List<Inmueble>> c, Response<List<Inmueble>> r) {
                if (r.isSuccessful() && r.body()!=null) lista.postValue(r.body());
                else error.postValue("No se pudieron cargar los inmuebles");
            }
            @Override public void onFailure(Call<List<Inmueble>> c, Throwable t) { error.postValue(t.getMessage()); }
        });
    }

    public void toggleDisponibilidad(Inmueble item, boolean disponible){
        String nuevo = disponible ? "disponible" : "no disponible";
        String previo = item.getEstado();
        item.setEstado(nuevo);

        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.inmuebleUpdate(item.getIdInmueble(), item).enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override public void onResponse(Call<okhttp3.ResponseBody> c, Response<okhttp3.ResponseBody> r) {
                if (!r.isSuccessful()){
                    item.setEstado(previo); // revertir
                    error.postValue("No se pudo actualizar el estado");
                    lista.postValue(new ArrayList<>(lista.getValue())); // refrescar
                }
            }
            @Override public void onFailure(Call<okhttp3.ResponseBody> c, Throwable t) {
                item.setEstado(previo);
                error.postValue(t.getMessage());
                lista.postValue(new ArrayList<>(lista.getValue()));
            }
        });
        // refresco optimista
        lista.postValue(new ArrayList<>(lista.getValue()));
    }
}
