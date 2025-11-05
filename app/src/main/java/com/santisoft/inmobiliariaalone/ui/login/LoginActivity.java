package com.santisoft.inmobiliariaalone.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.databinding.ActivityLoginBinding;
import com.santisoft.inmobiliariaalone.util.DialogUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity implements SensorEventListener {

    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;
    private SensorManager sensorManager;
    private Sensor acelerometro;
    private SweetAlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        // 🔹 Si ya hay sesión activa → ir directo a MainActivity
        SessionManager session = new SessionManager(this);
        if (session.getToken() != null && !session.getToken().isEmpty()) {
            Intent intent = new Intent(this, com.santisoft.inmobiliariaalone.MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        // === Observadores ===
        viewModel.getDialogEvent().observe(this, event -> {
            if (event == null) return;

            switch (event.getType()) {
                case SUCCESS:
                    DialogUtils.showSuccess(this, event.getTitle(), event.getMessage());
                    break;
                case ERROR:
                    DialogUtils.showError(this, event.getTitle(), event.getMessage());
                    break;
                case WARNING:
                    DialogUtils.showWarning(this, event.getTitle(), event.getMessage());
                    break;
                case CONFIRM:
                    DialogUtils.showConfirm(this, event.getTitle(), event.getMessage(),
                            () -> viewModel.ejecutarLlamada(this));
                    break;
                case LOADING:
                    loadingDialog = DialogUtils.showLoading(this, event.getMessage());
                    break;
                case HIDE_LOADING:
                    DialogUtils.hideLoading(loadingDialog);
                    break;
                default:
                    break;
            }
        });

        viewModel.getNavegacion().observe(this, destino -> {
            if ("main".equals(destino)) {
                Intent intent = new Intent(this, com.santisoft.inmobiliariaalone.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // === Botón Login ===
        binding.btLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String pass = binding.etPassword.getText().toString().trim();
            viewModel.iniciarSesion(email, pass);
        });

        // === Olvidé mi contraseña ===
        binding.tvOlvide.setOnClickListener(v -> viewModel.irARecuperar(this));

        // === Sensor de agitación ===
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // === Permiso de llamada ===
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        viewModel.detectarAgitacion(event.values[0], event.values[1], event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        viewModel.verificarPermisoLlamada(requestCode, grantResults);
    }
}
