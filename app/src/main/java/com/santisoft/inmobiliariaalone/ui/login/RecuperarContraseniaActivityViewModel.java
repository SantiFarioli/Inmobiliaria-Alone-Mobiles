package com.santisoft.inmobiliariaalone.ui.login;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarContraseniaActivityViewModel extends AndroidViewModel {

    public RecuperarContraseniaActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void solicitarRecuperacion(String email) {
        if (email.isEmpty()) {
            Toast.makeText(getApplication(), "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
            return;
        }

        ApClient.getInmobiliariaService().solicitarRecuperacion(email).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Correo enviado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "Error al solicitar recuperación", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplication(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
