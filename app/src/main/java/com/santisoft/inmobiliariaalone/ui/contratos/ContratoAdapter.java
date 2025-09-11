package com.santisoft.inmobiliariaalone.ui.contratos;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.santisoft.inmobiliariaalone.databinding.ItemContratoBinding;
import com.santisoft.inmobiliariaalone.model.Contrato;

import java.text.SimpleDateFormat;
import java.util.*;

public class ContratoAdapter extends RecyclerView.Adapter<ContratoAdapter.VH> {

    public interface OnClick { void onClick(Contrato c); }

    private final List<Contrato> data = new ArrayList<>();
    private final OnClick onClick;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

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
        h.b.tvFechas.setText(sdf.format(c.getFechaInicio()) + "  -  " + sdf.format(c.getFechaFin()));
        h.b.getRoot().setOnClickListener(v -> onClick.onClick(c));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemContratoBinding b;
        VH(@NonNull ItemContratoBinding b) { super(b.getRoot()); this.b = b; }
    }
}
