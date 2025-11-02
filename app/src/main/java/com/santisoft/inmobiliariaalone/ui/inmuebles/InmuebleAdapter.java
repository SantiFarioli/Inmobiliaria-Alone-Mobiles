package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

    public InmuebleAdapter(OnToggle onToggle, OnItemClick onItemClick) {
        this.onToggle = onToggle;
        this.onItemClick = onItemClick;
    }

    public void submit(List<Inmueble> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(ItemInmuebleBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int pos) {
        Inmueble i = data.get(pos);

        holder.b.tvDireccion.setText(i.getDireccion());
        holder.b.tvPrecio.setText("$ " + i.getPrecio());

        boolean tieneVigente = i.getContratos() != null && !i.getContratos().isEmpty();
        boolean disponible = "disponible".equalsIgnoreCase(i.getEstado());
        boolean noDisponible = !disponible || tieneVigente;

        int colorRes = noDisponible ? R.color.card_unavail : R.color.card_avail;
        holder.b.getRoot().setCardBackgroundColor(
                ContextCompat.getColor(holder.b.getRoot().getContext(), colorRes)
        );

        holder.b.swDisponible.setOnCheckedChangeListener(null);

        if (tieneVigente) {
            holder.b.swDisponible.setChecked(false);
            holder.b.swDisponible.setEnabled(false);
            holder.b.swDisponible.setAlpha(0.5f);
        } else {
            holder.b.swDisponible.setEnabled(true);
            holder.b.swDisponible.setAlpha(1f);
            holder.b.swDisponible.setChecked(disponible);

            holder.b.swDisponible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onToggle.onToggle(i, isChecked);
                }
            });
        }

        // Imagen corregida: completa URL si falta esquema
        String url = i.getFoto();
        if (url != null && !url.startsWith("http")) {
            url = "http://192.168.0.100:5157" + url;
        }

        Glide.with(holder.b.ivFoto.getContext())
                .load(url)
                .placeholder(R.drawable.casa1)
                .error(R.drawable.casa1)
                .centerCrop()
                .into(holder.b.ivFoto);


        holder.b.btnVer.setText("Ver detalle");
        holder.b.btnVer.setOnClickListener(v -> onItemClick.onClick(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemInmuebleBinding b;
        VH(@NonNull ItemInmuebleBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
