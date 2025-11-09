package com.santisoft.inmobiliariaalone.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.inmobiliariaalone.databinding.ActivityRecuperarContraseniaBinding;

public class RecuperarFragment extends Fragment {

    private ActivityRecuperarContraseniaBinding binding;
    private RecuperarContraseniaActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityRecuperarContraseniaBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(RecuperarContraseniaActivityViewModel.class);

        binding.btnRecuperar.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            viewModel.solicitarRecuperacion(email);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
