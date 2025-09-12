package com.santisoft.inmobiliariaalone.ui.inquilinos;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.santisoft.inmobiliariaalone.databinding.FragmentDetalleInquilinoBinding;
import com.santisoft.inmobiliariaalone.model.Inquilino;

public class DetalleInquilinoFragment extends Fragment {

    private FragmentDetalleInquilinoBinding b;
    private final Gson gson = new Gson();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentDetalleInquilinoBinding.inflate(inflater, container, false);

        String json = getArguments()!=null ? getArguments().getString("inquilinoJson", "") : "";
        Inquilino i = gson.fromJson(json, Inquilino.class);

        if (i != null) {
            b.tvCodigo.setText(String.valueOf(i.getIdInquilino()));
            b.tvNombre.setText(i.getNombreCompleto());
            b.tvDni.setText(i.getDni());
            b.tvEmail.setText(i.getEmail());
            b.tvTelefono.setText(i.getTelefono());
            b.tvTrabajo.setText(i.getLugarTrabajo());
            b.tvGarante.setText(i.getNombreGarante());
            b.tvDniGarante.setText(i.getDniGarante());
        }
        return b.getRoot();
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
