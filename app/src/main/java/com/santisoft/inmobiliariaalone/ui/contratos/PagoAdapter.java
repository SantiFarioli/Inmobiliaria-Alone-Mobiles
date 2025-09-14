package com.santisoft.inmobiliariaalone.ui.contratos;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.inmobiliariaalone.databinding.ItemPagoBinding;
import com.santisoft.inmobiliariaalone.model.Pago;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.VH> {

    private final List<Pago> data = new ArrayList<>();
    private final SimpleDateFormat sdfOut = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final NumberFormat money = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    public void submit(List<Pago> list){
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPagoBinding b = ItemPagoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Pago p = data.get(position);

        // Número (fallback por si viene 0)
        int nro = p.getNumeroPago() > 0 ? p.getNumeroPago() : (position + 1);
        h.b.tvNumero.setText("Pago #" + nro);

        // Fecha
        Date f = p.getFechaPago();
        h.b.tvFecha.setText(f != null ? sdfOut.format(f) : "-");

        // Importe
        h.b.tvImporte.setText(money.format(p.getImporte()));
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemPagoBinding b;
        VH(@NonNull ItemPagoBinding b) { super(b.getRoot()); this.b = b; }
    }
}
