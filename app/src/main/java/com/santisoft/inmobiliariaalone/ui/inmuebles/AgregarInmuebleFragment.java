package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.databinding.FragmentAgregarInmuebleBinding;
import com.santisoft.inmobiliariaalone.model.Inmueble;

public class AgregarInmuebleFragment extends Fragment {
    private FragmentAgregarInmuebleBinding b;
    private AgregarInmuebleViewModel vm;

    private Uri fotoSeleccionadaUri = null;

    // Photo Picker (AndroidX) -> en Android 13+ no requiere permisos
    private final ActivityResultLauncher<PickVisualMediaRequest> pickImage =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),
                    uri -> {
                        if (uri != null) {
                            fotoSeleccionadaUri = uri;
                            Glide.with(this).load(uri).into(b.ivPreview);
                        }
                    });

    @Override public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        b = FragmentAgregarInmuebleBinding.inflate(inf, c, false);
        vm = new ViewModelProvider(this).get(AgregarInmuebleViewModel.class);

        b.btnElegirFoto.setOnClickListener(v ->
                pickImage.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build())
        );

        b.btnConfirmarCrearInmueble.setOnClickListener(v -> {
            String dir   = safeTrim(b.etDireccion.getText());
            String uso   = safeTrim(b.etUso.getText());
            String tipo  = safeTrim(b.etTipo.getText());
            String est   = safeTrim(b.etEstado.getText());
            String url   = safeTrim(b.etImagen.getText());
            int amb      = safeInt(safeTrim(b.etAmbientes.getText()));
            double precio= safeDouble(safeTrim(b.etPrecio.getText()));

            if (dir.isEmpty() || uso.isEmpty() || tipo.isEmpty() || amb<=0 || precio<=0) {
                toast("Completá todos los datos obligatorios"); return;
            }

            Inmueble body = new Inmueble();
            body.setDireccion(dir);
            body.setUso(uso);
            body.setTipo(tipo);
            body.setAmbientes(amb);
            body.setPrecio(precio);
            body.setEstado(est.isEmpty() ? "disponible" : est.toLowerCase());

            if (fotoSeleccionadaUri != null) {
                // Guardar una copia segura en memoria interna
                String localPath = com.santisoft.inmobiliariaalone.util.ImageUtils.saveToInternalStorage(requireContext(), fotoSeleccionadaUri);
                if (localPath != null) {
                    body.setFoto(localPath);
                } else {
                    body.setFoto(fotoSeleccionadaUri.toString()); // fallback
                }
            } else if (!url.isEmpty()) {
                body.setFoto(url);
            }


            vm.crear(requireContext(), body);
        });

        vm.getExito().observe(getViewLifecycleOwner(), ok -> {
            if (Boolean.TRUE.equals(ok)) {
                toast("Inmueble creado");
                Navigation.findNavController(b.getRoot()).navigateUp();
            }
        });
        vm.getError().observe(getViewLifecycleOwner(), e -> {
            if (e!=null && !e.isEmpty()) toast(e);
        });
        return b.getRoot();
    }

    private String safeTrim(CharSequence s){ return s==null? "": s.toString().trim(); }
    private int safeInt(String s){ try { return Integer.parseInt(s); } catch(Exception e){ return -1; } }
    private double safeDouble(String s){ try { return Double.parseDouble(s); } catch(Exception e){ return -1; } }
    private void toast(String m){ Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show(); }

    @Override public void onDestroyView(){ super.onDestroyView(); b=null; }
}
