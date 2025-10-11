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
import com.santisoft.inmobiliariaalone.util.ImageUtils;

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

        // Cache local primero (rápido)
        binding.tilNombre.getEditText().setText(session.getNombre());
        binding.tilEmail.getEditText().setText(session.getEmail());
        String avatar = session.getFotoPerfil();
        if (avatar != null && !avatar.isEmpty()) {
            Glide.with(this)
                    .load(avatar)
                    .placeholder(R.drawable.ic_person)
                    .into(binding.ivFotoPerfil);
        } else {
            binding.ivFotoPerfil.setImageResource(R.drawable.ic_person);
        }

        // Observers (clases anónimas, como pide el profe)
        pvm.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                if (propietario != null) {
                    safeSet(binding.tilNombre, propietario.getNombre());
                    safeSet(binding.tilApellido, propietario.getApellido());
                    safeSet(binding.tilDni, propietario.getDni());
                    safeSet(binding.tilTelefono, propietario.getTelefono());
                    safeSet(binding.tilEmail, propietario.getEmail());

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

        // Cambiar foto
        binding.btnCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        // Guardar
        binding.btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Propietario body = new Propietario();
                body.setNombre(getText(binding.tilNombre));
                body.setApellido(getText(binding.tilApellido));
                body.setDni(getText(binding.tilDni));
                body.setTelefono(getText(binding.tilTelefono));
                body.setEmail(getText(binding.tilEmail));

                // Si eligió nueva foto con el picker, la persistimos como file:// (evita error de permiso)
                if (fotoSeleccionada != null) {
                    String localPath = ImageUtils.saveToInternalStorage(requireContext(), fotoSeleccionada);
                    if (localPath != null) {
                        body.setFotoPerfil(localPath);
                    } else {
                        body.setFotoPerfil(fotoSeleccionada.toString()); // fallback
                    }
                } else if (session.getFotoPerfil() != null) {
                    // Si no cambió, mantengo la que había
                    body.setFotoPerfil(session.getFotoPerfil());
                }

                pvm.actualizar(body);
            }
        });

        return binding.getRoot();
    }

    private void safeSet(com.google.android.material.textfield.TextInputLayout til, String v){
        if (til != null && til.getEditText() != null) {
            til.getEditText().setText(v == null ? "" : v);
        }
    }

    private String getText(com.google.android.material.textfield.TextInputLayout til){
        return til!=null && til.getEditText()!=null ? til.getEditText().getText().toString().trim() : "";
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
