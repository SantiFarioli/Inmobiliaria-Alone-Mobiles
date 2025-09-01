package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.text.TextUtils;
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

    public interface OnToggle { void onToggle(Inmueble i, boolean checked); }
    public interface OnClick  { void onClick(Inmueble i); }

    private final List<Inmueble> data = new ArrayList<>();
    private final OnToggle onToggle;
    private final OnClick  onClick;

    // Tus imágenes locales de respaldo
    private final int[] localFotos = {
            R.drawable.casa1,
            R.drawable.casa2,
            R.drawable.casa3
    };

    public InmuebleAdapter(OnToggle onToggle, OnClick onClick){
        this.onToggle = onToggle;
        this.onClick  = onClick;
    }

    public void submit(List<Inmueble> list){
        data.clear();
        if (list != null) data.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInmuebleBinding b = ItemInmuebleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Inmueble it = data.get(position);

        // Textos
        h.b.tvDireccion.setText(it.getDireccion());
        h.b.tvPrecio.setText("$ " + it.getPrecio());

        // Chip de estado (si existe en el layout)
        if (h.b.chEstado != null) {
            boolean disponible = "disponible".equalsIgnoreCase(it.getEstado());
            h.b.chEstado.setText(disponible ? "Disponible" : "No disponible");
            h.b.chEstado.setChipBackgroundColorResource(disponible ? R.color.teal_200 : R.color.purple_200);
        }

        // Switch Disponible
        if (h.b.switchDisponible != null) {
            boolean disponible = "disponible".equalsIgnoreCase(it.getEstado());
            h.b.switchDisponible.setOnCheckedChangeListener(null);
            h.b.switchDisponible.setChecked(disponible);
            h.b.switchDisponible.setOnCheckedChangeListener((btn, checked) -> onToggle.onToggle(it, checked));
        }

        // ====== IMAGEN ======
        // Intenta con URL, luego con nombre de drawable, y al final usa una de respaldo.
        String foto = getFotoFromModel(it); // ajustá este getter si tu modelo usa otro nombre
        if (!TextUtils.isEmpty(foto) && (foto.startsWith("http://") || foto.startsWith("https://"))) {
            // 1) URL remota
            Glide.with(h.b.getRoot())
                    .load(foto)
                    .placeholder(R.drawable.casa1) // placeholder local
                    .centerCrop()
                    .into(h.b.ivImagenInmueble);
        } else if (!TextUtils.isEmpty(foto)) {
            // 2) Nombre de recurso local (ej. "casa1" o "casa1.jpg")
            String resName = foto.toLowerCase()
                    .replace(".jpg","")
                    .replace(".jpeg","")
                    .replace(".png","");
            int resId = h.b.getRoot().getResources().getIdentifier(
                    resName, "drawable", h.b.getRoot().getContext().getPackageName()
            );
            if (resId != 0) {
                Glide.with(h.b.getRoot())
                        .load(resId)
                        .centerCrop()
                        .into(h.b.ivImagenInmueble);
            } else {
                // Fallback cíclico
                int pick = position % localFotos.length;
                Glide.with(h.b.getRoot())
                        .load(localFotos[pick])
                        .centerCrop()
                        .into(h.b.ivImagenInmueble);
            }
        } else {
            // 3) Sin info → fallback cíclico
            int pick = position % localFotos.length;
            Glide.with(h.b.getRoot())
                    .load(localFotos[pick])
                    .centerCrop()
                    .into(h.b.ivImagenInmueble);
        }

        // Click en toda la card
        h.b.getRoot().setOnClickListener(v -> onClick.onClick(it));
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        final ItemInmuebleBinding b;
        VH(@NonNull ItemInmuebleBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }
    }

    // 👉 Ajustá este método según tu modelo.
    // Si en tu clase Inmueble tenés otro campo (ej. getImagen(), getFoto(), etc.), usalo acá.
    private String getFotoFromModel(Inmueble it) {
        // return it.getImagen();  // ejemplo alternativo
        try {
            return it.getImagen(); // lo que tenías en el adapter anterior
        } catch (Exception e) {
            return null;
        }
    }
}
