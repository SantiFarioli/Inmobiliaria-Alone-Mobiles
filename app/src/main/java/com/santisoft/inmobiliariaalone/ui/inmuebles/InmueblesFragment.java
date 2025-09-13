package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentInmuebleBinding;
import com.santisoft.inmobiliariaalone.model.Inmueble;

import java.util.List;

public class InmueblesFragment extends Fragment {

    private FragmentInmuebleBinding binding;
    private InmueblesViewModel vm;
    private InmuebleAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vm = new ViewModelProvider(this).get(InmueblesViewModel.class);

        // Recycler + Adapter
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new InmuebleAdapter(
                // Toggle de disponibilidad -> PUT en el VM
                (item, checked) -> vm.toggleDisponibilidad(item, checked),
                // Click en el item -> navegar a DetalleInmueble
                (Inmueble item) -> {
                    Bundle args = new Bundle();
                    // usa la misma clave que definiste en el nav_graph
                    // (detalleInmuebleFragment tiene arg "inmueble" de tipo modelo)
                    args.putSerializable("inmueble", item);
                    Navigation.findNavController(view)
                            .navigate(R.id.action_nav_inmuebles_to_detalleInmuebleFragment, args);
                }
        );
        binding.recyclerView.setAdapter(adapter);

        // Espaciado entre cards
        int spacing = (int) (getResources().getDisplayMetrics().density * 12);
        binding.recyclerView.addItemDecoration(new VerticalSpaceDecoration(spacing));

        // Observers
        vm.getLista().observe(getViewLifecycleOwner(), this::renderLista);
        vm.getError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        // FAB -> navegar a AgregarInmuebleFragment
        binding.fabAgregarInmueble.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_nav_inmuebles_to_agregarInmuebleFragment)
        );

        // Cargar data inicial
        vm.cargar();
    }

    @Override
    public void onResume() {
        super.onResume();
        // refrescar la lista al volver de "Agregar"
        vm.cargar();
    }

    private void renderLista(List<Inmueble> list) {
        adapter.submit(list);
        boolean empty = (list == null || list.isEmpty());
        if (binding.textSlideshow != null) {
            binding.textSlideshow.setText(empty ? "No hay inmuebles cargados" : "");
            binding.textSlideshow.setVisibility(empty ? View.VISIBLE : View.GONE);
        }
        binding.recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Decoración simple para dejar espacio entre ítems
    public static class VerticalSpaceDecoration extends RecyclerView.ItemDecoration {
        private final int space;
        public VerticalSpaceDecoration(int space) { this.space = space; }
        @Override public void getItemOffsets(@NonNull Rect outRect, @NonNull View v, @NonNull RecyclerView p, @NonNull RecyclerView.State s) {
            outRect.bottom = space;
        }
    }
}
