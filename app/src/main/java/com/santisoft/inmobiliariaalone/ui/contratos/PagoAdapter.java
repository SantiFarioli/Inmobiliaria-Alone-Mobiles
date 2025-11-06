package com.santisoft.inmobiliariaalone.ui.contratos;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.inmobiliariaalone.databinding.ItemPagoBinding;
import com.santisoft.inmobiliariaalone.model.Pago;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.VH> {

    private final List<Pago> data = new ArrayList<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public void submit(List<Pago> list) {
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPagoBinding binding = ItemPagoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Pago p = data.get(position);

        holder.b.tvNumero.setText("Pago #" + p.getNumeroPago());
        holder.b.tvImporte.setText(String.format(Locale.getDefault(), "$ %.2f", p.getImporte()));

        // Java
        String fecha = (p.getFechaPago() != null) ? sdf.format(p.getFechaPago()) : "-";
        holder.b.tvFecha.setText(fecha);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemPagoBinding b;

        VH(ItemPagoBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }
    }
}
