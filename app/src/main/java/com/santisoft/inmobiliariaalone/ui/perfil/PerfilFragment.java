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

        // Observadores
        pvm.getPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario p) {
                if (p != null) {
                    binding.tilNombre.getEditText().setText(p.getNombre());
                    binding.tilApellido.getEditText().setText(p.getApellido());
                    binding.tilDni.getEditText().setText(p.getDni());
                    binding.tilTelefono.getEditText().setText(p.getTelefono());
                    binding.tilEmail.getEditText().setText(p.getEmail());

                    Glide.with(PerfilFragment.this)
                            .load(p.getFotoPerfil())
                            .placeholder(R.drawable.ic_person)
                            .into(binding.ivFotoPerfil);
                }
            }
        });

        pvm.getLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading != null && isLoading) {
                    loadingDialog = DialogUtils.showLoading(requireContext(), "Guardando cambios...");
                } else {
                    DialogUtils.hideLoading(loadingDialog);
                }
            }
        });

        pvm.getMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                if (msg == null || msg.isEmpty()) return;
                String tipo = pvm.getTipoMensaje().getValue();
                if ("success".equals(tipo)) {
                    DialogUtils.showSuccess(requireContext(), "¡Listo!", msg);
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).refrescarHeader();
                    }
                } else if ("warning".equals(tipo)) {
                    DialogUtils.showWarning(requireContext(), "Atención", msg);
                } else {
                    DialogUtils.showError(requireContext(), "Error", msg);
                }
            }
        });

        // Botones
        binding.btnCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        binding.btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Propietario body = new Propietario();
                body.setNombre(binding.tilNombre.getEditText().getText().toString().trim());
                body.setApellido(binding.tilApellido.getEditText().getText().toString().trim());
                body.setDni(binding.tilDni.getEditText().getText().toString().trim());
                body.setTelefono(binding.tilTelefono.getEditText().getText().toString().trim());
                body.setEmail(binding.tilEmail.getEditText().getText().toString().trim());

                if (fotoSeleccionada != null) {
                    String localPath = ImageUtils.saveToInternalStorage(requireContext(), fotoSeleccionada);
                    if (localPath != null) body.setFotoPerfil(localPath);
                    else body.setFotoPerfil(fotoSeleccionada.toString());
                } else {
                    SessionManager sm = new SessionManager(requireContext());
                    body.setFotoPerfil(sm.getFotoPerfil());
                }

                pvm.actualizarPerfil(body);
            }
        });

        binding.btnCambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PerfilFragment.this)
                        .navigate(R.id.cambiarPasswordFragment);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
