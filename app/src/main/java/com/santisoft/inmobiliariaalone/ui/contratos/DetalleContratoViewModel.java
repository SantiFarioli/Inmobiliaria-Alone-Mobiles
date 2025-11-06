package com.santisoft.inmobiliariaalone.ui.contratos;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.santisoft.inmobiliariaalone.model.Contrato;

public class DetalleContratoViewModel extends AndroidViewModel {

    private final MutableLiveData<Contrato> _contrato = new MutableLiveData<>();
    private final Gson gson = new Gson();

    public DetalleContratoViewModel(@NonNull Application app) {
        super(app);
    }

    public LiveData<Contrato> getContrato() {
        return _contrato;
    }

    public void setContratoDesdeJson(String json) {
        try {
            Contrato c = gson.fromJson(json, Contrato.class);
            _contrato.postValue(c);
        } catch (Exception e) {
            e.printStackTrace();
            _contrato.postValue(null);
        }
    }
}
