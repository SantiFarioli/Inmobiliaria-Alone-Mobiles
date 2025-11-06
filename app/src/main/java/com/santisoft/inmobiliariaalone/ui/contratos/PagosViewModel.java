package com.santisoft.inmobiliariaalone.ui.contratos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.model.Pago;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Pago>> _pagos = new MutableLiveData<>();
    public LiveData<List<Pago>> getPagos() { return _pagos; }

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> getError() { return _error; }

    public PagosViewModel(@NonNull Application app) {
        super(app);
    }

    public void cargarPagos(int contratoId) {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.pagosPorContrato(contratoId).enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    _pagos.postValue(response.body());
                } else {
                    _pagos.postValue(null);
                    _error.postValue("No hay pagos registrados para este contrato.");
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                _error.postValue("Error al conectar con el servidor: " + t.getMessage());
            }
        });
    }
}
