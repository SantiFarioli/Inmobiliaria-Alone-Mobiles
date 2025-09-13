package com.santisoft.inmobiliariaalone.ui.inmuebles;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentAgregarInmuebleBinding;
import com.santisoft.inmobiliariaalone.model.Inmueble;

public class AgregarInmuebleFragment extends Fragment {
    private FragmentAgregarInmuebleBinding b;
    private AgregarInmuebleViewModel vm;

    @Override public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentAgregarInmuebleBinding.inflate(inf, c, false);
        vm = new ViewModelProvider(this).get(AgregarInmuebleViewModel.class);

        b.btnConfirmarCrearInmueble.setOnClickListener(v -> {
            String dir   = b.etDireccion.getText().toString().trim();
            String uso   = b.etUso.getText().toString().trim();
            String tipo  = b.etTipo.getText().toString().trim();
            String est   = b.etEstado.getText().toString().trim();
            String foto  = b.etImagen.getText().toString().trim();
            int amb      = safeInt(b.etAmbientes.getText().toString());
            double precio= safeDouble(b.etPrecio.getText().toString());

            if (dir.isEmpty() || uso.isEmpty() || tipo.isEmpty() || amb<=0 || precio<=0) {
                Toast.makeText(getContext(),"Completá todos los datos", Toast.LENGTH_SHORT).show();
                return;
            }

            Inmueble body = new Inmueble();
            body.setDireccion(dir);
            body.setUso(uso);
            body.setTipo(tipo);
            body.setAmbientes(amb);
            body.setPrecio(precio);
            body.setEstado(est.isEmpty() ? "Disponible" : est);
            try { body.getClass().getMethod("setFoto", String.class); body.setFoto(foto); } catch (Exception ignore) {}

            vm.crear(requireContext(), body);
        });

        vm.getExito().observe(getViewLifecycleOwner(), ok -> {
            if (Boolean.TRUE.equals(ok)) {
                Toast.makeText(getContext(),"Inmueble creado",Toast.LENGTH_SHORT).show();
                Navigation.findNavController(b.getRoot()).navigateUp();
            }
        });
        vm.getError().observe(getViewLifecycleOwner(), e -> {
            if (e!=null && !e.isEmpty()) Toast.makeText(getContext(), e, Toast.LENGTH_SHORT).show();
        });
        return b.getRoot();
    }

    private int safeInt(String s){ try { return Integer.parseInt(s); } catch(Exception e){ return -1; } }
    private double safeDouble(String s){ try { return Double.parseDouble(s); } catch(Exception e){ return -1; } }
    @Override public void onDestroyView(){ super.onDestroyView(); b=null; }
}
