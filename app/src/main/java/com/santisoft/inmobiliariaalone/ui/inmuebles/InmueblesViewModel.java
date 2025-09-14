package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.model.Contrato;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> lista = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public InmueblesViewModel(@NonNull Application app){ super(app); }

    public LiveData<List<Inmueble>> getLista(){ return lista; }
    public LiveData<String> getError(){ return error; }

    /** Carga inmuebles y cruza con contratos vigentes para:
     *  - marcar como NO disponible los que tienen contrato
     *  - adjuntar la lista de contratos vigentes al Inmueble (para que el adapter lo detecte y bloquee el switch)
     */
    public void cargar() {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());

        // 1) Traer inmuebles
        api.inmueblesGetAll().enqueue(new Callback<List<Inmueble>>() {
            @Override public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> rInm) {
                if (!rInm.isSuccessful() || rInm.body()==null) {
                    error.postValue("No se pudieron cargar los inmuebles");
                    return;
                }
                final List<Inmueble> inmuebles = rInm.body();

                // 2) Traer contratos vigentes del propietario (solo activos)
                api.contratosVigentesMios().enqueue(new Callback<List<Contrato>>() {
                    @Override public void onResponse(Call<List<Contrato>> call2, Response<List<Contrato>> rCtr) {
                        if (!rCtr.isSuccessful() || rCtr.body()==null) {
                            // si falla, al menos muestro la lista de inmuebles como venga
                            lista.postValue(inmuebles);
                            return;
                        }
                        List<Contrato> vigentes = rCtr.body();

                        // Mapear por idInmueble
                        Map<Integer, List<Contrato>> byInmueble = new HashMap<>();
                        for (Contrato c : vigentes) {
                            if (c == null) continue;
                            int idInm = c.getIdInmueble();
                            if (idInm == 0 && c.getInmueble()!=null) {
                                idInm = c.getInmueble().getIdInmueble();
                            }
                            if (idInm == 0) continue;

                            byInmueble.computeIfAbsent(idInm, k -> new ArrayList<>()).add(c);
                        }

                        // Cruzo: adjunto contratos vigentes y fuerzo estado "no disponible" si corresponde
                        for (Inmueble i : inmuebles) {
                            if (i == null) continue;
                            List<Contrato> cs = byInmueble.get(i.getIdInmueble());
                            if (cs != null && !cs.isEmpty()) {
                                i.setContratos(cs);                 // <— el adapter lo usa para bloquear
                                i.setEstado("no disponible");        // <— y de paso lo muestro como no disponible
                            } else {
                                // aseguro que no quede basura anterior
                                i.setContratos(null);
                            }
                        }

                        lista.postValue(inmuebles);
                    }

                    @Override public void onFailure(Call<List<Contrato>> call2, Throwable t) {
                        // si falla el cruce, al menos muestro lo que hay
                        lista.postValue(inmuebles);
                    }
                });
            }

            @Override public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }

    /** Evita actualizar si el inmueble tiene contratos vigentes (por seguridad adicional) */
    public void toggleDisponibilidad(Inmueble item, boolean disponible){
        // seguridad por si por algún motivo el switch quedó habilitado
        if (item.getContratos()!=null && !item.getContratos().isEmpty()){
            error.postValue("No se puede cambiar disponibilidad: el inmueble tiene un contrato vigente.");
            // disparo refresh para que el adapter vuelva a pintar el estado correcto
            cargar();
            return;
        }

        String nuevo = disponible ? "disponible" : "no disponible";
        String previo = item.getEstado();
        item.setEstado(nuevo);

        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.inmuebleUpdate(item.getIdInmueble(), item).enqueue(new Callback<ResponseBody>() {
            @Override public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {
                if (!r.isSuccessful()){
                    item.setEstado(previo);
                    error.postValue("No se pudo actualizar el estado");
                    lista.postValue(new ArrayList<>(lista.getValue())); // refrescar UI
                }
            }
            @Override public void onFailure(Call<ResponseBody> c, Throwable t) {
                item.setEstado(previo);
                error.postValue(t.getMessage());
                lista.postValue(new ArrayList<>(lista.getValue()));
            }
        });

        // refresco optimista
        lista.postValue(new ArrayList<>(lista.getValue()));
    }
}
