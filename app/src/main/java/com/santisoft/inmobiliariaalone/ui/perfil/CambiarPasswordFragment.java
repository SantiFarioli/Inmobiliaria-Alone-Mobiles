package com.santisoft.inmobiliariaalone.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.santisoft.inmobiliariaalone.databinding.FragmentCambiarPasswordBinding;

public class CambiarPasswordFragment extends Fragment {
    private FragmentCambiarPasswordBinding binding;
    private CambiarPasswordViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCambiarPasswordBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(CambiarPasswordViewModel.class);

        // Guardar
        binding.btnGuardar.setOnClickListener(v -> {
            String actual = binding.tilActual.getEditText().getText().toString().trim();
            String nueva = binding.tilNueva.getEditText().getText().toString().trim();
            String confirmar = binding.tilConfirmar.getEditText().getText().toString().trim();
            vm.cambiarPassword(actual, nueva, confirmar);
        });

        // Observers (como pide el profe, con clases anónimas)
        vm.getMensaje().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<String>() {
            @Override
            public void onChanged(String msg) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        vm.getExito().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean ok) {
                if (Boolean.TRUE.equals(ok)) {
                    NavHostFragment.findNavController(CambiarPasswordFragment.this).popBackStack();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
