package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {

    private List<Inmueble> listaInmuebles;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Inmueble inmueble);
    }

    public InmuebleAdapter(List<Inmueble> listaInmuebles, OnItemClickListener listener) {
        this.listaInmuebles = listaInmuebles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inmueble, parent, false);
        return new InmuebleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {
        Inmueble inmueble = listaInmuebles.get(position);
        holder.bind(inmueble, listener);
    }

    @Override
    public int getItemCount() {
        return listaInmuebles.size();
    }

    public static class InmuebleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDireccion, tvPrecio;
        private ImageView ivImagenInmueble;

        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivImagenInmueble = itemView.findViewById(R.id.ivImagenInmueble);
        }

        public void bind(Inmueble inmueble, OnItemClickListener listener) {
            tvDireccion.setText(inmueble.getDireccion());
            tvPrecio.setText(String.format("$%.2f", inmueble.getPrecio()));

            Glide.with(itemView.getContext())
                    .load(inmueble.getImagen())
                    .placeholder(R.drawable.casa1)
                    .error(R.drawable.casa2)
                    .into(ivImagenInmueble);

            itemView.setOnClickListener(v -> listener.onItemClick(inmueble));
        }
    }
}
