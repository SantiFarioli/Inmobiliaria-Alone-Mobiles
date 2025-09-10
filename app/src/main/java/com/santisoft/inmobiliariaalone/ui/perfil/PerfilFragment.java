package com.santisoft.inmobiliariaalone.ui.perfil;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentPerfilBinding;
import com.santisoft.inmobiliariaalone.model.Propietario;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel pvm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pvm = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText etNombre   = binding.etNombre;
        final EditText etApellido = binding.etApellido;
        final EditText etDni      = binding.etDni;
        final EditText etTelefono = binding.etTelefono;
        final EditText etEmail    = binding.etEmail;
        final ImageView ivFoto    = binding.ivFotoPerfil;

        pvm.getPropietario().observe(getViewLifecycleOwner(), prop -> {
            if (prop != null) {
                etNombre.setText(prop.getNombre());
                etApellido.setText(prop.getApellido());
                etDni.setText(prop.getDni());
                etTelefono.setText(prop.getTelefono());
                etEmail.setText(prop.getEmail());

                String foto = prop.getFotoPerfil();
                if (TextUtils.isEmpty(foto)) {
                    ivFoto.setImageResource(R.drawable.ic_person); // fallback local
                } else {
                    Glide.with(requireContext())
                            .load(foto)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(ivFoto);
                }
            }
        });

        pvm.getMensaje().observe(getViewLifecycleOwner(), msg -> {
            if (!TextUtils.isEmpty(msg)) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                pvm.clearMensaje(); // evita que se repita al volver al fragment
            }
        });

        binding.btnActualizar.setOnClickListener(v -> {
            Propietario body = pvm.getPropietario().getValue();
            if (body == null) return;

            body.setNombre(etNombre.getText().toString().trim());
            body.setApellido(etApellido.getText().toString().trim());
            body.setDni(etDni.getText().toString().trim());
            body.setTelefono(etTelefono.getText().toString().trim());
            body.setEmail(etEmail.getText().toString().trim());
            // body.setFotoPerfil(...) // si luego agregás edición de foto

            pvm.actualizarPerfil(body);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
