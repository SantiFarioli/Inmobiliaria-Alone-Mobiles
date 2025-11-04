package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentDetalleInmuebleBinding;
import com.santisoft.inmobiliariaalone.model.Inmueble;

public class DetalleInmuebleFragment extends Fragment {

    private FragmentDetalleInmuebleBinding binding;
    private DetalleInmuebleViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        // Observar cambios en el inmueble
        viewModel.getInmueble().observe(getViewLifecycleOwner(), this::mostrarInmueble);

        // Recibir argumentos
        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            viewModel.setInmuebleInicial(inmueble);
            if (inmueble != null) {
                viewModel.refrescarDetalle(inmueble.getIdInmueble());
            }
        }
    }

    // Solo UI (sin lógica)
    private void mostrarInmueble(Inmueble inmueble) {
        if (inmueble == null) return;

        binding.tvTitulo.setText("Información del inmueble");
        binding.tvDireccion.setText(inmueble.getDireccion() != null ? inmueble.getDireccion() : "Sin dirección");
        binding.tvPrecio.setText("$ " + inmueble.getPrecio());
        binding.chUso.setText(inmueble.getUso() != null ? inmueble.getUso() : "-");
        binding.chTipo.setText(inmueble.getTipo() != null ? inmueble.getTipo() : "-");
        binding.chAmbientes.setText(String.valueOf(inmueble.getAmbientes()));

        boolean disponible = "disponible".equalsIgnoreCase(inmueble.getEstado());
        binding.tvEstado.setText(disponible ? "Disponible" : "No disponible");
        binding.tvEstado.setTextColor(ContextCompat.getColor(requireContext(),
                disponible ? R.color.estado_ok : R.color.estado_error));

        Glide.with(this)
                .load(inmueble.getFoto())
                .placeholder(R.drawable.casa1)
                .error(R.drawable.casa2)
                .into(binding.ivFoto);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
