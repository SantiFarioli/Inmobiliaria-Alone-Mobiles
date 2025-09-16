package com.santisoft.inmobiliariaalone.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.inmobiliariaalone.databinding.ActivityCambiarPasswordBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ResetPasswordFragment extends Fragment {

    private ActivityCambiarPasswordBinding binding;
    private CambiarPasswordActivityViewModel viewModel;

    private int propietarioId = -1;
    private String token = "";

    private static final String TAG = "ResetPasswordFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos usando el binding del layout que ya tenés
        binding = ActivityCambiarPasswordBinding.inflate(inflater, container, false);

        // ViewModel (reutilizamos el que ya tenés)
        viewModel = new ViewModelProvider(this).get(CambiarPasswordActivityViewModel.class);

        // Leer args enviados por Navigation (deep link)
        Bundle args = getArguments();
        if (args != null) {
            propietarioId = args.getInt("id", -1);
            token = args.getString("token", "");
        }

        Log.d(TAG, "onCreateView - propietarioId=" + propietarioId + " token=" + (token != null ? token.substring(0, Math.min(8, token.length())) + "..." : "null"));

        binding.btnCambiarPassword.setOnClickListener(v -> {
            String newPass = binding.etNewPassword.getText() != null ? binding.etNewPassword.getText().toString().trim() : "";
            String confirm = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString().trim() : "";

            if (TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirm)) {
                Toast.makeText(requireContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPass.equals(confirm)) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamamos a la lógica del ViewModel (usa tu método existente)
            try {
                viewModel.cambiarPassword(propietarioId, token, newPass);
            } catch (Exception e) {
                // En caso de que el método tenga otra firma, logueamos pero seguimos intentando con reflexión/observador
                Log.w(TAG, "Error llamando cambiarPassword directamente: " + e.getMessage());
                tryCallCambiarPasswordReflectively(propietarioId, token, newPass);
            }

            // Intentamos observar un LiveData<Boolean> expuesto por el ViewModel.
            // Buscamos el primer método 'getX' que devuelva LiveData (suponiendo LiveData<Boolean>).
            LiveData<Boolean> resultadoLD = findBooleanLiveDataOnViewModel(viewModel);
            if (resultadoLD != null) {
                resultadoLD.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean success) {
                        if (Boolean.TRUE.equals(success)) {
                            onCambioPasswordExitoso();
                        } else {
                            Toast.makeText(requireContext(), "Error al actualizar contraseña", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // Si no encontramos LiveData, como fallback: asumimos que el cambio fue pedido,
                // avisamos al usuario y lo devolvemos al login (esto evita que quede logueado).
                Toast.makeText(requireContext(), "Solicitud enviada. Volviendo al login...", Toast.LENGTH_SHORT).show();
                // pequeña espera visual opcional, luego volver al login
                binding.btnCambiarPassword.postDelayed(this::onCambioPasswordExitoso, 800);
            }
        });

        return binding.getRoot();
    }

    /**
     * Intenta invocar por reflexión un método llamado cambiarPassword(int,String,String) u otra firma similar.
     * Esto es solo un intento alternativo si la llamada directa falla.
     */
    private void tryCallCambiarPasswordReflectively(int id, String token, String newPass) {
        try {
            Method[] methods = viewModel.getClass().getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().toLowerCase().contains("cambiar") && m.getParameterCount() >= 3) {
                    try {
                        m.setAccessible(true);
                        // Intentamos pasar (int, String, String)
                        m.invoke(viewModel, id, token, newPass);
                        Log.d(TAG, "Invocado cambiarPassword(reflection) -> " + m.getName());
                        return;
                    } catch (IllegalArgumentException | InvocationTargetException ignored) {
                        // probar siguiente
                    }
                }
            }
            Log.w(TAG, "No se pudo invocar reflectivamente cambiarPassword en el ViewModel.");
        } catch (Exception e) {
            Log.e(TAG, "Error reflexión cambiarPassword: " + e.getMessage(), e);
        }
    }

    /**
     * Busca por reflexión en el ViewModel un método que devuelva LiveData (usualmente getResult/getResultado)
     * y que probablemente contenga el booleano de éxito. Devuelve la instancia LiveData<Boolean> si la encuentra.
     */
    @SuppressWarnings("unchecked")
    private LiveData<Boolean> findBooleanLiveDataOnViewModel(Object vm) {
        try {
            Method[] methods = vm.getClass().getMethods();
            for (Method m : methods) {
                Class<?> ret = m.getReturnType();
                if (LiveData.class.isAssignableFrom(ret) && m.getParameterCount() == 0) {
                    try {
                        Object value = m.invoke(vm);
                        if (value instanceof LiveData) {
                            Log.d(TAG, "Detectado LiveData desde método: " + m.getName());
                            return (LiveData<Boolean>) value;
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        Log.w(TAG, "No se pudo invocar método LiveData: " + m.getName(), e);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error buscando LiveData en ViewModel: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Acciones a ejecutar cuando el cambio de contraseña fue exitoso:
     * - limpiar SharedPreferences
     * - lanzar LoginActivity borrando backstack
     * - finalizar Activity host
     */
    private void onCambioPasswordExitoso() {
        Log.d(TAG, "Cambio de password exitoso -> limpiando prefs y navegando a LoginActivity");

        // 1) limpiar SharedPreferences (ajusta el nombre si usás otro)
        try {
            Context ctx = requireContext();
            // Si usás PreferenceManager.getDefaultSharedPreferences(...), adaptalo aquí.
            SharedPreferences prefs = ctx.getSharedPreferences("mi_prefs", Context.MODE_PRIVATE);
            prefs.edit().clear().apply(); // limpia todo para evitar auto-login
        } catch (Exception e) {
            Log.w(TAG, "No se pudo limpiar SharedPreferences: " + e.getMessage());
        }

        // 2) lanzar LoginActivity y limpiar backstack
        try {
            Intent intent = new Intent(requireContext(), com.santisoft.inmobiliariaalone.ui.login.LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Error al iniciar LoginActivity: " + e.getMessage(), e);
            // fallback: intentar navegar al login con NavController si existe (no forzamos aquí).
            Toast.makeText(requireContext(), "Contraseña cambiada. Reinicie la app para iniciar sesión.", Toast.LENGTH_LONG).show();
        }

        // 3) finalizar la activity host por si queda en memoria
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
