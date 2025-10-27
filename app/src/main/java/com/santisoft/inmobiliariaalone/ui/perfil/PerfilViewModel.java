package com.santisoft.inmobiliariaalone.ui.perfil;

import android.app.Application;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.model.Propietario;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {

    private final MutableLiveData<Propietario> propietario = new MutableLiveData<>();
    private final MutableLiveData<String> mensaje = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> tipoMensaje = new MutableLiveData<>(); // "success", "warning", "error"

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        cargarPerfil();
    }

    public LiveData<Propietario> getPropietario() { return propietario; }
    public LiveData<String> getMensaje() { return mensaje; }
    public LiveData<String> getTipoMensaje() { return tipoMensaje; }
    public LiveData<Boolean> getLoading() { return loading; }

    //   Cargar datos de perfil

    private void cargarPerfil() {
        ApClient.getInmobiliariaService(getApplication())
                .obtenerPerfil()
                .enqueue(new Callback<Propietario>() {
                    @Override
                    public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            propietario.postValue(response.body());
                        } else {
                            mensaje.postValue("No se pudo cargar el perfil.");
                            tipoMensaje.postValue("error");
                        }
                    }

                    @Override
                    public void onFailure(Call<Propietario> call, Throwable t) {
                        mensaje.postValue("Error de conexión: " + t.getMessage());
                        tipoMensaje.postValue("error");
                    }
                });
    }

    //   Validar y actualizar perfil

    public void actualizarPerfil(Propietario body) {
        // Validaciones
        if (body.getNombre() == null || body.getNombre().trim().isEmpty() ||
                body.getApellido() == null || body.getApellido().trim().isEmpty() ||
                body.getEmail() == null || body.getEmail().trim().isEmpty()) {
            mensaje.postValue("Los campos Nombre, Apellido y Email son obligatorios.");
            tipoMensaje.postValue("warning");
            return;
        }

        if (!body.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            mensaje.postValue("El nombre solo puede contener letras y espacios.");
            tipoMensaje.postValue("warning");
            return;
        }

        if (!body.getApellido().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            mensaje.postValue("El apellido solo puede contener letras y espacios.");
            tipoMensaje.postValue("warning");
            return;
        }

        if (body.getDni() != null && !body.getDni().isEmpty() && !body.getDni().matches("^\\d{7,9}$")) {
            mensaje.postValue("El DNI debe tener entre 7 y 9 dígitos numéricos.");
            tipoMensaje.postValue("warning");
            return;
        }

        if (body.getTelefono() != null && !body.getTelefono().isEmpty()
                && !body.getTelefono().matches("^[0-9+() -]{6,}$")) {
            mensaje.postValue("Ingrese un número de teléfono válido.");
            tipoMensaje.postValue("warning");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(body.getEmail()).matches()) {
            mensaje.postValue("Ingrese un correo electrónico válido.");
            tipoMensaje.postValue("warning");
            return;
        }

        // Verificar cambios con respecto al perfil actual
        Propietario actual = propietario.getValue();
        if (actual != null && !huboCambios(actual, body)) {
            mensaje.postValue("No se detectaron cambios en los datos del perfil.");
            tipoMensaje.postValue("warning");
            return;
        }

        // Mostrar loading
        loading.postValue(true);

        // Llamada a la API
        ApClient.getInmobiliariaService(getApplication())
                .propietarioUpdate(body)
                .enqueue(new Callback<Propietario>() {
                    @Override
                    public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                        loading.postValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            Propietario actualizado = response.body();
                            propietario.postValue(actualizado);

                            // Actualizamos cache local
                            SessionManager sm = new SessionManager(getApplication());
                            sm.updateProfileData(actualizado);

                            mensaje.postValue("Perfil actualizado correctamente.");
                            tipoMensaje.postValue("success");
                        } else {
                            mensaje.postValue("No se pudo actualizar el perfil.");
                            tipoMensaje.postValue("error");
                        }
                    }

                    @Override
                    public void onFailure(Call<Propietario> call, Throwable t) {
                        loading.postValue(false);
                        mensaje.postValue("Error de conexión: " + t.getMessage());
                        tipoMensaje.postValue("error");
                    }
                });
    }

    private boolean huboCambios(Propietario actual, Propietario nuevo) {
        return !(safeEquals(actual.getNombre(), nuevo.getNombre()) &&
                safeEquals(actual.getApellido(), nuevo.getApellido()) &&
                safeEquals(actual.getDni(), nuevo.getDni()) &&
                safeEquals(actual.getTelefono(), nuevo.getTelefono()) &&
                safeEquals(actual.getEmail(), nuevo.getEmail()) &&
                safeEquals(actual.getFotoPerfil(), nuevo.getFotoPerfil()));
    }

    private boolean safeEquals(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";
        return a.trim().equals(b.trim());
    }
}
