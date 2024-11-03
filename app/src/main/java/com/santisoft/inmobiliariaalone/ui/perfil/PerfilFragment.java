package com.santisoft.inmobiliariaalone.ui.perfil;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.inmobiliariaalone.databinding.FragmentPerfilBinding;
import com.santisoft.inmobiliariaalone.model.Propietario;

import com.bumptech.glide.Glide;

public class PerfilFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private PerfilViewModel pvm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pvm = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText etNombre = binding.etNombre;
        final EditText etApellido = binding.etApellido;
        final EditText etDni = binding.etDni;
        final EditText etTelefono = binding.etTelefono;
        final EditText etEmail = binding.etEmail;
        final ImageView ivFotoPerfil = binding.ivFotoPerfil;

        pvm.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    etNombre.setText(propietario.getNombre());
                    etApellido.setText(propietario.getApellido());
                    etDni.setText(propietario.getDni());
                    etTelefono.setText(propietario.getTelefono());
                    etEmail.setText(propietario.getEmail());
                    Glide.with(getContext())
                        .load(propietario.getFotoPerfil())
                        .into(ivFotoPerfil);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}