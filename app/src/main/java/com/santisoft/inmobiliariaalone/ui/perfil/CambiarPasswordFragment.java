package com.santisoft.inmobiliariaalone.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.inmobiliariaalone.databinding.FragmentCambiarPasswordBinding;
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

        // Observadores del ViewModel
        vm.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading != null && isLoading) {
                    loadingDialog = DialogUtils.showLoading(requireContext(), "Procesando...");
                } else {
                    DialogUtils.hideLoading(loadingDialog);
                }
            }
        });

        vm.getMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                if (msg == null || msg.isEmpty()) return;

                String tipo = vm.getTipoMensaje().getValue();
                if ("success".equals(tipo)) {
                    DialogUtils.showSuccess(requireContext(), "¡Listo!", msg);
                    limpiarCampos();
                } else if ("warning".equals(tipo)) {
                    DialogUtils.showWarning(requireContext(), "Atención", msg);
                } else {
                    DialogUtils.showError(requireContext(), "Error", msg);
                }
            }
        });

        // Botón "Guardar cambios"
        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actual = binding.tilActual.getEditText().getText().toString().trim();
                String nueva = binding.tilNueva.getEditText().getText().toString().trim();
                String confirmar = binding.tilConfirmar.getEditText().getText().toString().trim();

                vm.cambiarPassword(actual, nueva, confirmar);
            }
        });

        return binding.getRoot();
    }

    private void limpiarCampos() {
        binding.tilActual.getEditText().setText("");
        binding.tilNueva.getEditText().setText("");
        binding.tilConfirmar.getEditText().setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
