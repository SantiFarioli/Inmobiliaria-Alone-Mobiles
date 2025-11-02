package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.santisoft.inmobiliariaalone.util.DialogEvent;
import com.santisoft.inmobiliariaalone.util.DialogUtils;

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

        // Configurar RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new InmuebleAdapter(
                (item, checked) -> vm.cambiarDisponibilidad(item, checked),
                item -> {
                    Bundle args = new Bundle();
                    args.putSerializable("inmueble", item);
                    Navigation.findNavController(view)
                            .navigate(R.id.action_nav_inmuebles_to_detalleInmuebleFragment, args);
                }
        );
        binding.recyclerView.setAdapter(adapter);

        int spacing = (int) (getResources().getDisplayMetrics().density * 12);
        binding.recyclerView.addItemDecoration(new VerticalSpaceDecoration(spacing));

        // Observadores
        vm.getLista().observe(getViewLifecycleOwner(), (List<Inmueble> inmuebles) -> {
            adapter.submit(inmuebles);
            boolean empty = (inmuebles == null || inmuebles.isEmpty());
            binding.textSlideshow.setText(empty ? "No hay inmuebles cargados" : "");
            binding.textSlideshow.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        });

        vm.getDialogEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;
            switch (event.getType()) {
                case SUCCESS:
                    DialogUtils.showSuccess(requireContext(), event.getTitle(), event.getMessage());
                    vm.limpiarMensajes();
                    break;
                case WARNING:
                    DialogUtils.showWarning(requireContext(), event.getTitle(), event.getMessage());
                    vm.limpiarMensajes();
                    break;
                case ERROR:
                    DialogUtils.showError(requireContext(), event.getTitle(), event.getMessage());
                    vm.limpiarMensajes();
                    break;
                case LOADING:
                    DialogUtils.showLoading(requireContext(), event.getTitle());
                    break;
            }
        });

        // Botón flotante
        binding.fabAgregarInmueble.setImageResource(R.drawable.ic_home_add);
        binding.fabAgregarInmueble.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_nav_inmuebles_to_agregarInmuebleFragment)
        );

        vm.cargar();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.cargar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Espaciado entre cards
    public static class VerticalSpaceDecoration extends RecyclerView.ItemDecoration {
        private final int space;
        public VerticalSpaceDecoration(int space) { this.space = space; }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View v,
                                   @NonNull RecyclerView p, @NonNull RecyclerView.State s) {
            outRect.bottom = space;
        }
    }
}
