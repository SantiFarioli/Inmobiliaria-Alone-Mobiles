package com.santisoft.inmobiliariaalone.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentDetalleContratoBinding;
import com.santisoft.inmobiliariaalone.model.Contrato;

public class DetalleContratoFragment extends Fragment {

    private FragmentDetalleContratoBinding b;
    private DetalleContratoViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentDetalleContratoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(DetalleContratoViewModel.class);

        vm.getContrato().observe(getViewLifecycleOwner(), this::mostrarContrato);

        if (getArguments() != null) {
            String json = getArguments().getString("contratoJson");
            vm.setContratoDesdeJson(json);
        }

        return b.getRoot();
    }

    private void mostrarContrato(Contrato c) {
        if (c == null) return;

        b.tvDireccion.setText(c.getInmueble() != null ? c.getInmueble().getDireccion() : "Sin dirección");
        b.tvInquilino.setText(c.getInquilino() != null ? c.getInquilino().getNombreCompleto() : "Sin inquilino");
        b.tvFechas.setText(String.format("%s - %s",
                c.getFechaInicio() != null ? c.getFechaInicio().substring(0, 10) : "-",
                c.getFechaFin() != null ? c.getFechaFin().substring(0, 10) : "-"));
        b.tvMonto.setText("$ " + c.getMontoAlquiler());

        b.btnVerPagos.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putInt("contratoId", c.getIdContrato());
            Navigation.findNavController(b.getRoot())
                    .navigate(R.id.action_detalleContratoFragment_to_pagosFragment, args);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}
