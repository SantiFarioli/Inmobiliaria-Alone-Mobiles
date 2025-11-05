package com.santisoft.inmobiliariaalone.ui.inquilinos;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.santisoft.inmobiliariaalone.model.Inquilino;

public class DetalleInquilinoViewModel extends AndroidViewModel {

    private final MutableLiveData<Inquilino> _inquilino = new MutableLiveData<>();
    private final Gson gson = new Gson();

    public DetalleInquilinoViewModel(@NonNull Application application) {
        super(application);
    }

    // LiveData observable desde el fragment
    public LiveData<Inquilino> getInquilino() {
        return _inquilino;
    }

    // Convierte el JSON recibido en un objeto Inquilino y actualiza el LiveData
    public void setInquilinoDesdeJson(String json) {
        try {
            Inquilino obj = gson.fromJson(json, Inquilino.class);
            _inquilino.postValue(obj);
        } catch (Exception e) {
            e.printStackTrace();
            _inquilino.postValue(null); // evita crasheos si el JSON viene mal
        }
    }
}
