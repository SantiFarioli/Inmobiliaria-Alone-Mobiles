package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.model.Contrato;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;
import com.santisoft.inmobiliariaalone.util.DialogEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmueblesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Inmueble>> lista = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<DialogEvent> dialogEvent = new MutableLiveData<>();

    public InmueblesViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<List<Inmueble>> getLista() { return lista; }
    public LiveData<DialogEvent> getDialogEvent() { return dialogEvent; }

    // Cargar inmuebles
    public void cargar() {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());

        api.inmueblesGetAll().enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    dialogEvent.postValue(new DialogEvent(
                            DialogEvent.Type.ERROR,
                            "Error",
                            "No se pudieron cargar los inmuebles."
                    ));
                    return;
                }
                List<Inmueble> inmuebles = response.body();
                cargarContratosYActualizarEstados(api, inmuebles);
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                dialogEvent.postValue(new DialogEvent(
                        DialogEvent.Type.ERROR,
                        "Error de conexión",
                        t.getMessage()
                ));
            }
        });
    }

    // Asocia contratos y actualiza estados
    private void cargarContratosYActualizarEstados(ApClient.InmobliariaService api, List<Inmueble> inmuebles) {
        api.contratosVigentesMios().enqueue(new Callback<List<Contrato>>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    lista.postValue(inmuebles);
                    return;
                }

                Map<Integer, List<Contrato>> contratosPorInmueble = new HashMap<>();
                for (Contrato c : response.body()) {
                    if (c == null) continue;
                    int idInm = c.getIdInmueble();
                    if (idInm == 0 && c.getInmueble() != null) {
                        idInm = c.getInmueble().getIdInmueble();
                    }
                    if (idInm != 0) {
                        contratosPorInmueble
                                .computeIfAbsent(idInm, k -> new ArrayList<>())
                                .add(c);
                    }
                }

                for (Inmueble i : inmuebles) {
                    List<Contrato> contratos = contratosPorInmueble.get(i.getIdInmueble());
                    if (contratos != null && !contratos.isEmpty()) {
                        i.setContratos(contratos);
                        i.setEstado("no disponible");
                    }
                }

                lista.postValue(inmuebles);
            }

            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable t) {
                lista.postValue(inmuebles);
            }
        });
    }

    // Cambiar disponibilidad
    public void cambiarDisponibilidad(Inmueble item, boolean disponible) {
        if (item.getContratos() != null && !item.getContratos().isEmpty()) {
            dialogEvent.postValue(new DialogEvent(
                    DialogEvent.Type.WARNING,
                    "No permitido",
                    "No se puede cambiar disponibilidad: el inmueble tiene contrato vigente."
            ));
            refrescarLista();
            return;
        }

        if ((disponible && "disponible".equalsIgnoreCase(item.getEstado())) ||
                (!disponible && "no disponible".equalsIgnoreCase(item.getEstado()))) {
            dialogEvent.postValue(new DialogEvent(
                    DialogEvent.Type.WARNING,
                    "Atención",
                    "El inmueble ya está en ese estado."
            ));
            return;
        }

        String nuevoEstado = disponible ? "disponible" : "no disponible";
        String previo = item.getEstado();
        item.setEstado(nuevoEstado);

        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.inmuebleUpdate(item.getIdInmueble(), item).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> c, Response<ResponseBody> r) {
                if (!r.isSuccessful()) {
                    item.setEstado(previo);
                    dialogEvent.postValue(new DialogEvent(
                            DialogEvent.Type.ERROR,
                            "Error",
                            "No se pudo actualizar el estado."
                    ));
                } else {
                    dialogEvent.postValue(new DialogEvent(
                            DialogEvent.Type.SUCCESS,
                            "Éxito",
                            "Estado actualizado correctamente."
                    ));
                }
                refrescarLista();
            }

            @Override
            public void onFailure(Call<ResponseBody> c, Throwable t) {
                item.setEstado(previo);
                dialogEvent.postValue(new DialogEvent(
                        DialogEvent.Type.ERROR,
                        "Error de conexión",
                        t.getMessage()
                ));
                refrescarLista();
            }
        });
    }

    private void refrescarLista() {
        if (lista.getValue() != null) {
            lista.postValue(new ArrayList<>(lista.getValue()));
        }
    }

    // solo se agrega limpieza de mensajes
    public void limpiarMensajes() {
        dialogEvent.postValue(null);
        Log.d("InmueblesVM", "🧹 Mensajes limpiados para evitar repetición");
    }
}
