package com.santisoft.inmobiliariaalone.ui.inquilinos;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.databinding.ItemInquilinoBinding;
import com.santisoft.inmobiliariaalone.model.Contrato;
import java.util.*;

public class InquilinoAdapter extends RecyclerView.Adapter<InquilinoAdapter.VH> {

    public interface OnClick { void onClick(Contrato c); }

    private final List<Contrato> data = new ArrayList<>();
    private final OnClick onClick;

    public InquilinoAdapter(OnClick onClick) { this.onClick = onClick; }

    public void submit(List<Contrato> list){
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(ItemInquilinoBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Contrato c = data.get(pos);
        String dir = (c.getInmueble()!=null) ? c.getInmueble().getDireccion() : "Inmueble";
        h.b.tvDireccion.setText(dir);

        String foto = (c.getInmueble()!=null) ? c.getInmueble().getFoto() : null; // ajustá el getter según tu modelo
        Glide.with(h.b.getRoot().getContext())
                .load(foto)
                .placeholder(com.santisoft.inmobiliariaalone.R.drawable.casa1)
                .into(h.b.ivFoto);

        h.b.btnVer.setOnClickListener(v -> onClick.onClick(c));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemInquilinoBinding b;
        VH(@NonNull ItemInquilinoBinding b) { super(b.getRoot()); this.b = b; }
    }
}
