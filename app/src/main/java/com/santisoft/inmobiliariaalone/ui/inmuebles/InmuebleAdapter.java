package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.ItemInmuebleBinding;
import com.santisoft.inmobiliariaalone.model.Inmueble;

import java.util.ArrayList;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.VH> {

    public interface OnToggle { void onToggle(Inmueble item, boolean checked); }
    public interface OnItemClick { void onClick(Inmueble item); }

    private final List<Inmueble> data = new ArrayList<>();
    private final OnToggle onToggle;
    private final OnItemClick onItemClick;

    public InmuebleAdapter(OnToggle onToggle, OnItemClick onItemClick){
        this.onToggle = onToggle;
        this.onItemClick = onItemClick;
    }

    public void submit(List<Inmueble> list){
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(ItemInmuebleBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Inmueble i = data.get(pos);

        h.b.tvDireccion.setText(i.getDireccion());
        h.b.tvPrecio.setText("$ " + i.getPrecio());

        // ¿Tiene contrato vigente? (el VM adjunta solo los vigentes)
        boolean tieneVigente = i.getContratos()!=null && !i.getContratos().isEmpty();

        // Evitar loops de listener
        h.b.swDisponible.setOnCheckedChangeListener(null);

        if (tieneVigente) {
            // Bloqueado por contrato: forzar apagado, deshabilitar e indicar visualmente
            h.b.swDisponible.setChecked(false);
            h.b.swDisponible.setEnabled(false);
            h.b.swDisponible.setAlpha(0.5f);
        } else {
            // Libre: respetar estado
            boolean disponible = "disponible".equalsIgnoreCase(i.getEstado());
            h.b.swDisponible.setEnabled(true);
            h.b.swDisponible.setAlpha(1f);
            h.b.swDisponible.setChecked(disponible);
            h.b.swDisponible.setOnCheckedChangeListener((btn, checked) -> onToggle.onToggle(i, checked));
        }

        // Imagen (URL o content:// del picker)
        String foto = i.getFoto();
        Glide.with(h.b.ivFoto.getContext())
                .load(foto == null || foto.trim().isEmpty() ? null : foto)
                .placeholder(R.drawable.casa1)
                .error(R.drawable.casa1)
                .into(h.b.ivFoto);

        // Botón "Ver detalle"
        h.b.btnVer.setText("Ver detalle");
        h.b.btnVer.setOnClickListener(v -> onItemClick.onClick(i));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemInmuebleBinding b;
        VH(@NonNull ItemInmuebleBinding b) { super(b.getRoot()); this.b = b; }
    }
}
