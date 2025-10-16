package com.santisoft.inmobiliariaalone.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.santisoft.inmobiliariaalone.MainActivity;
import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Si ya hay sesión guardada, salto directo al MainActivity
        SessionManager session = new SessionManager(this);
        if (session.getToken() != null && !session.getToken().isEmpty()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Evita volver atrás al login
            return;
        }

        // Si no hay sesión, sigo con el login normal
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString().trim();
                String pass = binding.etPassword.getText().toString().trim();
                viewModel.iniciarSesion(email, pass);
            }
        });

        binding.tvOlvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.irARecuperar(LoginActivity.this);
            }
        });
    }
}
