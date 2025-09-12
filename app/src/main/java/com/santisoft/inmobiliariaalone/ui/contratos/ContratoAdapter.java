package com.santisoft.inmobiliariaalone.ui.contratos;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.santisoft.inmobiliariaalone.databinding.ItemContratoBinding;
import com.santisoft.inmobiliariaalone.model.Contrato;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.VH> {

    public interface OnClick { void onClick(Contrato c); }

    private final List<Contrato> data = new ArrayList<>();
    private final OnClick onClick;

    // salida en UI
    private final SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    // por si llegan como String (ISO del backend)
    private final SimpleDateFormat sdfIso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

    public ContratoAdapter(OnClick onClick) { this.onClick = onClick; }

    public void submit(List<Contrato> list){
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(ItemContratoBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Contrato c = data.get(pos);

        String dir = (c.getInmueble()!=null) ? c.getInmueble().getDireccion() : "Inmueble";
        String inq = (c.getInquilino()!=null) ? c.getInquilino().getNombreCompleto() : "Inquilino";

        h.b.tvDireccion.setText(dir);
        h.b.tvInquilino.setText(inq);

        // Fechas tolerantes: Date o String
        h.b.tvFechas.setText(formatAny(c.getFechaInicio()) + "  -  " + formatAny(c.getFechaFin()));

        h.b.getRoot().setOnClickListener(v -> onClick.onClick(c));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemContratoBinding b;
        VH(@NonNull ItemContratoBinding b) { super(b.getRoot()); this.b = b; }
    }

    private String formatAny(Object src) {
        if (src == null) return "—";
        try {
            if (src instanceof Date) {
                return sdfOut.format((Date) src);
            }
            if (src instanceof String) {
                Date d = sdfIso.parse((String) src);
                return (d != null) ? sdfOut.format(d) : "—";
            }
        } catch (ParseException ignore) {}
        return "—";
    }
}
