package com.santisoft.inmobiliariaalone.ui.perfil;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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

    private Uri fotoSeleccionada; // content:// si elige una local

    private final ActivityResultLauncher<PickVisualMediaRequest> pickImage =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
                    uri -> {
                        if (uri != null) {
                            fotoSeleccionada = uri;
                            Glide.with(this).load(uri)
                                    .placeholder(R.drawable.ic_person)
                                    .into(binding.ivFotoPerfil);
                        }
                    });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pvm = new ViewModelProvider(this).get(PerfilViewModel.class);
        binding = FragmentPerfilBinding.inflate(inflater, container, false);

        // Observa perfil y errores
        pvm.getPropietario().observe(getViewLifecycleOwner(), propietario -> {
            if (propietario != null) {
                binding.etNombre.setText(propietario.getNombre());
                binding.etApellido.setText(propietario.getApellido());
                binding.etDni.setText(propietario.getDni());
                binding.etTelefono.setText(propietario.getTelefono());
                binding.etEmail.setText(propietario.getEmail());

                Glide.with(this)
                        .load(propietario.getFotoPerfil())
                        .placeholder(R.drawable.ic_person)
                        .into(binding.ivFotoPerfil);
            }
        });
        pvm.getError().observe(getViewLifecycleOwner(),
                msg -> { if (msg!=null && !msg.isEmpty()) toast(msg); });
        pvm.getExito().observe(getViewLifecycleOwner(),
                ok -> { if (Boolean.TRUE.equals(ok)) toast("Perfil actualizado"); });

        // Botón cambiar foto
        binding.btnCambiarFoto.setOnClickListener(v ->
                pickImage.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build())
        );

        // Guardar cambios
        binding.btnActualizar.setOnClickListener(v -> {
            Propietario body = new Propietario();
            body.setNombre(binding.etNombre.getText().toString().trim());
            body.setApellido(binding.etApellido.getText().toString().trim());
            body.setDni(binding.etDni.getText().toString().trim());
            body.setTelefono(binding.etTelefono.getText().toString().trim());
            body.setEmail(binding.etEmail.getText().toString().trim());
            if (fotoSeleccionada != null) {
                body.setFotoPerfil(fotoSeleccionada.toString()); // content:// o url
            }
            pvm.actualizar(body);
        });

        return binding.getRoot();
    }

    private void toast(String m){ Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show(); }

    @Override
    public void onDestroyView() { super.onDestroyView(); binding = null; }
}
