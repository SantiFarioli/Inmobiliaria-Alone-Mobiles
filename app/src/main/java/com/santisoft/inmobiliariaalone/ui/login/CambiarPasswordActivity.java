package com.santisoft.inmobiliariaalone.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.santisoft.inmobiliariaalone.databinding.ActivityCambiarPasswordBinding;

public class CambiarPasswordActivity extends AppCompatActivity {

    private ActivityCambiarPasswordBinding binding;
    private CambiarPasswordActivityViewModel viewModel;
    private String token;
    private int idPropietario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CambiarPassword", "Activity CambiarPasswordActivity iniciada");
        try {
            super.onCreate(savedInstanceState);
            binding = ActivityCambiarPasswordBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            viewModel = new ViewModelProvider(this).get(CambiarPasswordActivityViewModel.class);

            Intent intent = getIntent();
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                Uri uri = intent.getData();
                if (uri != null) {
                    token = uri.getQueryParameter("token");

                    // Verifica si el segmento anterior al último es un número válido
                    String[] pathSegments = uri.getPath().split("/");
                    if (pathSegments.length >= 2) {
                        try {
                            idPropietario = Integer.parseInt(pathSegments[pathSegments.length - 2]);
                            Log.d("CambiarPassword", "ID from URI: " + idPropietario);
                        } catch (NumberFormatException e) {
                            Log.e("CambiarPassword", "Error parsing ID from URI", e);
                        }
                    }

                    Log.d("CambiarPassword", "Token from URI: " + token);
                }
            }
        } catch (Exception e) {
            Log.e("CambiarPassword", "Error en onCreate", e);
        }

        binding.btnCambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = binding.etNewPassword.getText().toString();
                String confirmPassword = binding.etConfirmPassword.getText().toString();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getApplication(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getApplication(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                viewModel.cambiarPassword(idPropietario, token, newPassword);
            }
        });
    }
}
