package com.santisoft.inmobiliariaalone.ui.perfil;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.databinding.FragmentPerfilBinding;
import com.santisoft.inmobiliariaalone.model.Propietario;

public class PerfilFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private PerfilViewModel pvm;
    private Uri fotoSeleccionada;
    private SessionManager session;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickImage =
            registerForActivityResult(
                    new ActivityResultContracts.PickVisualMedia(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {
                            if (uri != null) {
                                fotoSeleccionada = uri;
                                Glide.with(PerfilFragment.this)
                                        .load(uri)
                                        .placeholder(R.drawable.ic_person)
                                        .into(binding.ivFotoPerfil);
                            }
                        }
                    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        pvm = new ViewModelProvider(this).get(PerfilViewModel.class);
        session = new SessionManager(requireContext());

        // Mostrar cache local primero
        binding.etNombre.setText(session.getNombre());
        binding.etEmail.setText(session.getEmail());
        String avatar = session.getFotoPerfil();
        if (avatar != null && !avatar.isEmpty()) {
            Glide.with(this)
                    .load(avatar)
                    .placeholder(R.drawable.ic_person)
                    .into(binding.ivFotoPerfil);
        }

        // Observers
        pvm.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    binding.etNombre.setText(propietario.getNombre());
                    binding.etApellido.setText(propietario.getApellido());
                    binding.etDni.setText(propietario.getDni());
                    binding.etTelefono.setText(propietario.getTelefono());
                    binding.etEmail.setText(propietario.getEmail());

                    Glide.with(PerfilFragment.this)
                            .load(propietario.getFotoPerfil())
                            .placeholder(R.drawable.ic_person)
                            .into(binding.ivFotoPerfil);
                }
            }
        });

        pvm.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                if (msg != null && !msg.isEmpty()) {
                    toast(msg);
                }
            }
        });

        pvm.getExito().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean ok) {
                if (Boolean.TRUE.equals(ok)) {
                    toast("Perfil actualizado correctamente");
                }
            }
        });

        // Botón cambiar foto
        binding.btnCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        // Botón guardar
        binding.btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Propietario body = new Propietario();
                body.setNombre(binding.etNombre.getText().toString().trim());
                body.setApellido(binding.etApellido.getText().toString().trim());
                body.setDni(binding.etDni.getText().toString().trim());
                body.setTelefono(binding.etTelefono.getText().toString().trim());
                body.setEmail(binding.etEmail.getText().toString().trim());

                if (fotoSeleccionada != null) {
                    body.setFotoPerfil(fotoSeleccionada.toString());
                } else if (session.getFotoPerfil() != null) {
                    body.setFotoPerfil(session.getFotoPerfil());
                }

                pvm.actualizar(body);
            }
        });

        return binding.getRoot();
    }

    private void toast(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
