package com.santisoft.inmobiliariaalone.ui.login;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.inmobiliariaalone.databinding.ActivityRecuperarContraseniaBinding;

public class RecuperarContraseniaActivity extends AppCompatActivity {

    private ActivityRecuperarContraseniaBinding binding;
    private RecuperarContraseniaActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(RecuperarContraseniaActivityViewModel.class);
        binding = ActivityRecuperarContraseniaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSolicitarRecuperacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmailRecuperacion.getText().toString().trim();
                viewModel.solicitarRecuperacion(email);
            }
        });
    }
}
