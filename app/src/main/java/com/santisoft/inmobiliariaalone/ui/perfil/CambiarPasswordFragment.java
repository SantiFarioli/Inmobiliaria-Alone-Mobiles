package com.santisoft.inmobiliariaalone.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.santisoft.inmobiliariaalone.databinding.FragmentCambiarPasswordBinding;
import com.santisoft.inmobiliariaalone.model.EventoMensaje;
import com.santisoft.inmobiliariaalone.util.DialogUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CambiarPasswordFragment extends Fragment {
    private FragmentCambiarPasswordBinding binding;
    private CambiarPasswordViewModel vm;
    private SweetAlertDialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCambiarPasswordBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(CambiarPasswordViewModel.class);

        // Guardar
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actual = binding.etActual.getText().toString().trim();
                String nueva = binding.etNueva.getText().toString().trim();
                String confirmar = binding.etConfirmar.getText().toString().trim();

                loadingDialog = DialogUtils.showLoading(requireContext(), "Actualizando contraseña...");
                vm.cambiarPassword(actual, nueva, confirmar);
            }
        });

        // Observador
        vm.getEvento().observe(getViewLifecycleOwner(), new Observer<EventoMensaje>() {
            @Override
            public void onChanged(EventoMensaje e) {
                DialogUtils.hideLoading(loadingDialog);

                if (e == null) return;

                switch (e.getTipo()) {
                    case SUCCESS:
                        DialogUtils.showSuccess(requireContext(), "¡Listo!", e.getTexto());
                        break;
                    case WARNING:
                        DialogUtils.showWarning(requireContext(), "Atención", e.getTexto());
                        break;
                    case ERROR:
                        DialogUtils.showError(requireContext(), "Error", e.getTexto());
                        break;
                }
            }
        });

        vm.getExito().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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
