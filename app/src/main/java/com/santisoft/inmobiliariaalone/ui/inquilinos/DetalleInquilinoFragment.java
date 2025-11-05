package com.santisoft.inmobiliariaalone.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.inmobiliariaalone.databinding.FragmentDetalleInquilinoBinding;
import com.santisoft.inmobiliariaalone.model.Inquilino;

public class DetalleInquilinoFragment extends Fragment {

    private FragmentDetalleInquilinoBinding binding;
    private DetalleInquilinoViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);

        // Observar el inquilino y actualizar UI
        vm.getInquilino().observe(getViewLifecycleOwner(), this::mostrarInquilino);

        // Cargar datos si vienen desde navegación
        if (getArguments() != null) {
            vm.setInquilinoDesdeJson(getArguments().getString("inquilinoJson"));
        }

        return binding.getRoot();
    }

    private void mostrarInquilino(Inquilino inq) {
        if (inq == null) return;

        binding.tvCodigo.setText(String.valueOf(inq.getIdInquilino()));
        binding.tvNombre.setText(inq.getNombreCompleto());
        binding.tvDni.setText(inq.getDni());
        binding.tvEmail.setText(inq.getEmail());
        binding.tvTelefono.setText(inq.getTelefono());
        binding.tvTrabajo.setText(inq.getLugarTrabajo());
        binding.tvGarante.setText(inq.getNombreGarante());
        binding.tvDniGarante.setText(inq.getDniGarante());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
