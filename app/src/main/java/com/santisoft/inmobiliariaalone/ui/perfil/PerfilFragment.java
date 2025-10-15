package com.santisoft.inmobiliariaalone.ui.perfil;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.MainActivity;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.databinding.FragmentPerfilBinding;
import com.santisoft.inmobiliariaalone.model.Propietario;
import com.santisoft.inmobiliariaalone.util.DialogUtils;
import com.santisoft.inmobiliariaalone.util.ImageUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PerfilFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private PerfilViewModel pvm;
    private Uri fotoSeleccionada;
    private SessionManager session;
    private SweetAlertDialog loadingDialog;

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

        // Cache local primero
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

        // Observador: perfil cargado desde la API
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

        // Observador: error
        pvm.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                if (msg != null && !msg.isEmpty()) {
                    DialogUtils.hideLoading(loadingDialog);
                    DialogUtils.showError(requireContext(), "Error", msg);
                }
            }
        });

        // Observador: éxito
        pvm.getExito().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean ok) {
                if (Boolean.TRUE.equals(ok)) {
                    DialogUtils.hideLoading(loadingDialog);
                    DialogUtils.showSuccess(requireContext(), "¡Listo!", "Perfil actualizado correctamente");

                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).refrescarHeader();
                    }
                    pvm.resetExito();
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

        // Guardar cambios
        binding.btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Propietario body = new Propietario();
                body.setNombre(getText(binding.tilNombre));
                body.setApellido(getText(binding.tilApellido));
                body.setDni(getText(binding.tilDni));
                body.setTelefono(getText(binding.tilTelefono));
                body.setEmail(getText(binding.tilEmail));

                if (fotoSeleccionada != null) {
                    String localPath = ImageUtils.saveToInternalStorage(requireContext(), fotoSeleccionada);
                    if (localPath != null) {
                        body.setFotoPerfil(localPath);
                    } else {
                        body.setFotoPerfil(fotoSeleccionada.toString());
                    }
                } else if (session.getFotoPerfil() != null) {
                    body.setFotoPerfil(session.getFotoPerfil());
                }

                loadingDialog = DialogUtils.showLoading(requireContext(), "Guardando cambios...");
                pvm.actualizar(body);
            }
        });

        // Botón de cambio de contraseña
        binding.btnCambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PerfilFragment.this)
                        .navigate(R.id.cambiarPasswordFragment);
            }
        });

        return binding.getRoot();
    }

    // Utilidades auxiliares
    private void safeSet(com.google.android.material.textfield.TextInputLayout til, String v) {
        if (til != null && til.getEditText() != null) {
            til.getEditText().setText(v == null ? "" : v);
        }
    }

    private String getText(com.google.android.material.textfield.TextInputLayout til) {
        if (til != null && til.getEditText() != null) {
            return til.getEditText().getText().toString().trim();
        }
        return "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
