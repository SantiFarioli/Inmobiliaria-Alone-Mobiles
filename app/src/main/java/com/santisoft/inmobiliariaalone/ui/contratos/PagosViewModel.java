package com.santisoft.inmobiliariaalone.ui.contratos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.santisoft.inmobiliariaalone.model.Pago;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;
import java.util.*;
import retrofit2.*;

public class PagosViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Pago>> lista = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public PagosViewModel(@NonNull Application app) { super(app); }

    public LiveData<List<Pago>> getLista(){ return lista; }
    public LiveData<String> getError(){ return error; }

    public void cargar(int contratoId){
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.pagosPorContrato(contratoId).enqueue(new Callback<List<Pago>>() {
            @Override public void onResponse(Call<List<Pago>> c, Response<List<Pago>> r) {
                if (r.isSuccessful() && r.body()!=null) lista.postValue(r.body());
                else error.postValue("No se pudieron obtener los pagos.");
            }
            @Override public void onFailure(Call<List<Pago>> c, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }
}
