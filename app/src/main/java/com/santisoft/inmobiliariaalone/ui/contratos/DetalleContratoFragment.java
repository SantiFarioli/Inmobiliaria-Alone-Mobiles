package com.santisoft.inmobiliariaalone.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentDetalleContratoBinding;
import com.santisoft.inmobiliariaalone.model.Contrato;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.model.Inquilino;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetalleContratoFragment extends Fragment {

    private FragmentDetalleContratoBinding b;

    private final SimpleDateFormat sdfIso  =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat sdfOut  =
            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentDetalleContratoBinding.inflate(inflater, container, false);

        String json = getArguments()!=null ? getArguments().getString("contratoJson","") : "";
        Contrato c = new Gson().fromJson(json, Contrato.class);

        if (c != null) {
            b.tvCodigo.setText(String.valueOf(c.getIdContrato()));
            b.tvInicio.setText(formatAny(c.getFechaInicio()));
            b.tvFin.setText(formatAny(c.getFechaFin()));
            b.tvMonto.setText("$ " + c.getMontoAlquiler());

            Inquilino inq = c.getInquilino();
            Inmueble inm = c.getInmueble();

            b.tvInquilino.setText(
                    (inq!=null) ? inq.getNombreCompleto() : "-"
            );
            b.tvInmueble.setText(
                    (inm!=null) ? inm.getDireccion() : "-"
            );

            b.btnPagos.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putInt("contratoId", c.getIdContrato());
                Navigation.findNavController(b.getRoot())
                        .navigate(R.id.pagosFragment, args);
            });
        }

        return b.getRoot();
    }

    private String formatAny(Object src) {
        if (src == null) return "-";
        try {
            if (src instanceof Date) return sdfOut.format((Date) src);
            if (src instanceof String) {
                Date d = sdfIso.parse((String) src);
                return (d != null) ? sdfOut.format(d) : "-";
            }
        } catch (ParseException ignore) {}
        return "-";
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
