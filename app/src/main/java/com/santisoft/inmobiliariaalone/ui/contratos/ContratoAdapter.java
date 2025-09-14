package com.santisoft.inmobiliariaalone.ui.contratos;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.santisoft.inmobiliariaalone.databinding.ItemContratoBinding;
import com.santisoft.inmobiliariaalone.model.Contrato;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.model.Inquilino;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.VH> {

    public interface OnItemClick { void onClick(Contrato c); }

    private final List<Contrato> data = new ArrayList<>();
    private final OnItemClick onItemClick;
    private final SimpleDateFormat sdfIso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public ContratoAdapter(OnItemClick cb){ this.onItemClick = cb; }

    public void submit(List<Contrato> list){
        data.clear();
        if (list!=null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(ItemContratoBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Contrato c = data.get(pos);

        Inmueble inm = c.getInmueble();
        Inquilino inq = c.getInquilino();

        String dir = inm != null ? nullToDash(inm.getDireccion()) : "Inmueble";
        String inqNom = inq != null ? nullToDash(inq.getNombreCompleto()) : "Inquilino";

        h.b.tvDireccion.setText(dir);
        h.b.tvInquilino.setText(inqNom);
        h.b.tvFechas.setText(formatAny(c.getFechaInicio()) + "  -  " + formatAny(c.getFechaFin()));
        h.b.tvMonto.setText("$ " + c.getMontoAlquiler());

        h.b.btnVerDetalle.setOnClickListener(v -> onItemClick.onClick(c));
    }

    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemContratoBinding b;
        VH(@NonNull ItemContratoBinding b){ super(b.getRoot()); this.b = b; }
    }

    // helpers
    private String nullToDash(String s){ return (s==null || s.isEmpty()) ? "-" : s; }
    private String formatAny(Object src){
        if (src==null) return "-";
        try{
            if (src instanceof Date) return sdfOut.format((Date) src);
            if (src instanceof String){
                Date d;
                try { d = sdfIso.parse((String) src); }
                catch (ParseException ignore){ d = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse((String) src); }
                return d!=null ? sdfOut.format(d) : "-";
            }
        }catch (Exception ignore){}
        return "-";
    }
}
