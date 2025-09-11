package com.santisoft.inmobiliariaalone.ui.contratos;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.santisoft.inmobiliariaalone.databinding.ItemPagoBinding;
import com.santisoft.inmobiliariaalone.model.Pago;
import java.text.SimpleDateFormat;
import java.util.*;

public class PagoAdapter extends RecyclerView.Adapter<PagoAdapter.VH> {

    private final List<Pago> data = new ArrayList<>();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public void submit(List<Pago> list){
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(ItemPagoBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Pago p = data.get(pos);
        h.b.tvNumero.setText("Pago #" + p.getNumeroPago());
        h.b.tvFecha.setText(sdf.format(p.getFechaPago()));
        h.b.tvImporte.setText("$ " + p.getImporte());
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemPagoBinding b;
        VH(@NonNull ItemPagoBinding b) { super(b.getRoot()); this.b = b; }
    }
}

