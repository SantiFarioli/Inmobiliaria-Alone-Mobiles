package com.santisoft.inmobiliariaalone.ui.login;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.santisoft.inmobiliariaalone.model.RestablecerContrasenaRequest;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarPasswordActivityViewModel extends AndroidViewModel {

    public CambiarPasswordActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void cambiarPassword(int idPropietario, String token, String newPassword) {
        RestablecerContrasenaRequest request = new RestablecerContrasenaRequest(token, newPassword);

        ApClient.getInmobiliariaService().restablecerContrasena(idPropietario, request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplication(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
