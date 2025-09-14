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


                binding.tvTitulo.setText("Información del inmueble");
                binding.tvDireccion.setText(inmueble.getDireccion());
                binding.tvPrecio.setText("$ " + inmueble.getPrecio());
                binding.chUso.setText(inmueble.getUso());
                binding.chTipo.setText(inmueble.getTipo());
                binding.chAmbientes.setText(String.valueOf(inmueble.getAmbientes()));


                if (binding.tvEstado != null) {
                    String estado = inmueble.getEstado();
                    binding.tvEstado.setText(estado == null ? "-" : estado);
                }

                // Foto: soporta URL o content:// del picker
                String foto = inmueble.getFoto(); // alias de getImagen()
                Glide.with(this)
                        .load(foto == null || foto.isEmpty() ? null : foto)
                        .placeholder(R.drawable.casa1)
                        .error(R.drawable.casa2)
                        .into(binding.ivFoto);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
