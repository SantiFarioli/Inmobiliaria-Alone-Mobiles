package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentDetalleInmuebleBinding;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleFragment extends Fragment {

    private FragmentDetalleInmuebleBinding binding;
    private int idInmueble;

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
                idInmueble = inmueble.getIdInmueble();
                cargarDetalle(idInmueble);
            }
        }
    }

    private void cargarDetalle(int id) {
        ApClient.InmobliariaService api = ApClient.getInmobiliariaService(requireContext());
        api.inmuebleGet(id).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mostrarInmueble(response.body());
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) { }
        });
    }

    private void mostrarInmueble(Inmueble inmueble) {
        binding.tvTitulo.setText("Información del inmueble");
        binding.tvDireccion.setText(inmueble.getDireccion());
        binding.tvPrecio.setText("$ " + inmueble.getPrecio());
        binding.chUso.setText(inmueble.getUso());
        binding.chTipo.setText(inmueble.getTipo());
        binding.chAmbientes.setText(String.valueOf(inmueble.getAmbientes()));

        boolean disponible = "disponible".equalsIgnoreCase(inmueble.getEstado());
        binding.tvEstado.setText(disponible ? "disponible" : "no disponible");
        int color = ContextCompat.getColor(requireContext(),
                disponible ? R.color.estado_ok : R.color.estado_error);
        binding.tvEstado.setTextColor(color);

        String foto = inmueble.getFoto();
        Glide.with(this)
                .load(foto == null || foto.isEmpty() ? null : foto)
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
