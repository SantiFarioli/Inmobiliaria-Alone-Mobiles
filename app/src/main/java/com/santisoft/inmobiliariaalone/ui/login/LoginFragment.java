package com.santisoft.inmobiliariaalone.ui.login;

import android.Manifest;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.santisoft.inmobiliariaalone.MainActivity;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.databinding.ActivityLoginBinding;
import com.santisoft.inmobiliariaalone.util.DialogUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginFragment extends Fragment implements SensorEventListener {

    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;
    private SensorManager sensorManager;
    private Sensor acelerometro;
    private SweetAlertDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        if (getActivity() != null) {
            sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 100);
        }

        // 🔹 Si ya hay sesión activa → ir directo al MainActivity
        SessionManager session = new SessionManager(requireContext());
        if (session.getToken() != null && !session.getToken().isEmpty()) {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        }

        // === Observadores ===
        viewModel.getDialogEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;

            switch (event.getType()) {
                case SUCCESS:
                    DialogUtils.showSuccess(requireContext(), event.getTitle(), event.getMessage());
                    break;
                case ERROR:
                    DialogUtils.showError(requireContext(), event.getTitle(), event.getMessage());
                    break;
                case WARNING:
                    DialogUtils.showWarning(requireContext(), event.getTitle(), event.getMessage());
                    break;
                case CONFIRM:
                    DialogUtils.showConfirm(requireContext(), event.getTitle(), event.getMessage(),
                            () -> viewModel.ejecutarLlamada(requireContext()));
                    break;
                case LOADING:
                    loadingDialog = DialogUtils.showLoading(requireContext(), event.getMessage());
                    break;
                case HIDE_LOADING:
                    DialogUtils.hideLoading(loadingDialog);
                    break;
            }
        });

        // 🔹 Navegación cuando el login es exitoso
        viewModel.getNavegacion().observe(getViewLifecycleOwner(), destino -> {
            if ("main".equals(destino)) {
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        // === Botón Login ===
        binding.btLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String pass = binding.etPassword.getText().toString().trim();
            viewModel.iniciarSesion(email, pass);
        });

        // === Olvidé mi contraseña ===
        binding.tvOlvide.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_recuperarFragment)
        );

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null && acelerometro != null)
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        viewModel.detectarAgitacion(event.values[0], event.values[1], event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
