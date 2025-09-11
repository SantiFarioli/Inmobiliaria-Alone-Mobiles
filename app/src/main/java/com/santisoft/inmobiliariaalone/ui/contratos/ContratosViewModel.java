package com.santisoft.inmobiliariaalone.ui.contratos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.santisoft.inmobiliariaalone.model.Contrato;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;
import java.util.*;
import retrofit2.*;

public class ContratosViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Contrato>> lista = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public ContratosViewModel(@NonNull Application app) { super(app); }

    public LiveData<List<Contrato>> getLista(){ return lista; }
    public LiveData<String> getError(){ return error; }

    public void cargar(){
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(getApplication());
        api.contratosVigentesMios().enqueue(new Callback<List<Contrato>>() {
            @Override public void onResponse(Call<List<Contrato>> c, Response<List<Contrato>> r) {
                if (r.isSuccessful() && r.body()!=null) lista.postValue(r.body());
                else error.postValue("No se pudieron obtener los contratos.");
            }
            @Override public void onFailure(Call<List<Contrato>> c, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }
}
