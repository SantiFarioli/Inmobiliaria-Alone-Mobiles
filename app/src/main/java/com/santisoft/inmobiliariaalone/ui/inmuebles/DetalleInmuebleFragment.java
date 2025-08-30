package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentDetalleInmuebleBinding;
import com.santisoft.inmobiliariaalone.model.Inmueble;

public class DetalleInmuebleFragment extends Fragment {

    private FragmentDetalleInmuebleBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            Inmueble inmueble = (Inmueble) getArguments().getSerializable("inmueble");
            if (inmueble != null) {
                binding.tvCodigo.setText(String.valueOf(inmueble.getIdInmueble()));
                binding.tvDireccion.setText(inmueble.getDireccion());
                binding.tvUso.setText(inmueble.getUso());
                binding.tvTipo.setText(inmueble.getTipo());
                binding.tvAmbientes.setText(String.valueOf(inmueble.getAmbientes()));
                binding.tvPrecio.setText(String.format("$%.2f", inmueble.getPrecio()));
                binding.tvEstado.setText(inmueble.getEstado());

                Glide.with(this)
                        .load(inmueble.getImagen())
                        .placeholder(R.drawable.casa1)
                        .error(R.drawable.casa2)
                        .into(binding.ivImagenInmueble);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
