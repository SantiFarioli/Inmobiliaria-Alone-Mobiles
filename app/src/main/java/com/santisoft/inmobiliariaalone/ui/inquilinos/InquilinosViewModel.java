package com.santisoft.inmobiliariaalone.ui.inquilinos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.model.Contrato;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquilinosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Contrato>> _contratos = new MutableLiveData<>();
    public LiveData<List<Contrato>> getContratos() { return _contratos; }

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> getError() { return _error; }

    public InquilinosViewModel(@NonNull Application app) {
        super(app);
    }

    /** 🔹 Carga los contratos vigentes del propietario autenticado */
    public void cargarContratos() {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.contratosVigentesMios().enqueue(new Callback<List<Contrato>>() {
            @Override
            public void onResponse(Call<List<Contrato>> call, Response<List<Contrato>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    _contratos.postValue(response.body());
                } else {
                    _contratos.postValue(null);
                    _error.postValue("No hay inmuebles alquilados actualmente.");
                }
            }

            @Override
            public void onFailure(Call<List<Contrato>> call, Throwable t) {
                _error.postValue("Error al conectar con el servidor: " + t.getMessage());
            }
        });
    }
}
