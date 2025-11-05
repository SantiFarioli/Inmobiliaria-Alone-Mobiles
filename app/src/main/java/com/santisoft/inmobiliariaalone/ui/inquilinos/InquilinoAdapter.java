package com.santisoft.inmobiliariaalone.ui.inquilinos;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.ItemInquilinoBinding;
import com.santisoft.inmobiliariaalone.model.Contrato;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.model.Inquilino;

import java.util.ArrayList;
import java.util.List;

public class InquilinoAdapter extends RecyclerView.Adapter<InquilinoAdapter.VH> {

    public interface OnClick { void onClick(Contrato contrato); }

    private final List<Contrato> data = new ArrayList<>();
    private final OnClick onClick;

    public InquilinoAdapter(OnClick onClick) {
        this.onClick = onClick;
    }

    public void submit(List<Contrato> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInquilinoBinding binding =
                ItemInquilinoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Contrato contrato = data.get(position);
        Inmueble inmueble = contrato.getInmueble();
        Inquilino inquilino = contrato.getInquilino();

        // Dirección del inmueble
        if (inmueble != null && inmueble.getDireccion() != null) {
            holder.b.tvDireccion.setText(inmueble.getDireccion());
        } else {
            holder.b.tvDireccion.setText("Sin dirección");
        }

        // Nombre del inquilino
        if (inquilino != null && inquilino.getNombreCompleto() != null) {
            holder.b.tvInquilino.setText("Inquilino: " + inquilino.getNombreCompleto());
        } else {
            holder.b.tvInquilino.setText("Inquilino no asignado");
        }

        // Teléfono
        if (inquilino != null && inquilino.getTelefono() != null) {
            holder.b.tvTelefono.setText("Tel: " + inquilino.getTelefono());
        } else {
            holder.b.tvTelefono.setText("Tel: -");
        }

        // Imagen del inmueble
        String fotoUrl = (inmueble != null) ? inmueble.getFoto() : null;
        Glide.with(holder.b.getRoot().getContext())
                .load(fotoUrl)
                .placeholder(R.drawable.casa1)
                .error(R.drawable.casa1)
                .centerCrop()
                .into(holder.b.ivFoto);

        // Evento del botón Ver detalle
        holder.b.btnVer.setOnClickListener(v -> onClick.onClick(contrato));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemInquilinoBinding b;

        VH(@NonNull ItemInquilinoBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }
    }
}
