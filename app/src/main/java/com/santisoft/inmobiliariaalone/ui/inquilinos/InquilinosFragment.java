package com.santisoft.inmobiliariaalone.ui.inquilinos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentInquilinosBinding;
import com.santisoft.inmobiliariaalone.model.Contrato;
import com.santisoft.inmobiliariaalone.model.Inquilino;

public class InquilinosFragment extends Fragment {

    private FragmentInquilinosBinding b;
    private InquilinosViewModel vm;
    private InquilinoAdapter adapter;
    private final Gson gson = new Gson();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentInquilinosBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(InquilinosViewModel.class);

        adapter = new InquilinoAdapter(contrato -> {
            Inquilino inq = contrato.getInquilino();
            if (inq == null) {
                Toast.makeText(getContext(), "Sin datos del inquilino", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle args = new Bundle();
            args.putString("inquilinoJson", gson.toJson(inq));
            Navigation.findNavController(b.getRoot())
                    .navigate(R.id.action_nav_inquilinos_to_inquilinoDetalleFragment, args);
        });

        b.rvInquilinos.setLayoutManager(new LinearLayoutManager(getContext()));
        b.rvInquilinos.setAdapter(adapter);

        // 🔹 Observadores
        vm.getContratos().observe(getViewLifecycleOwner(), list -> {
            if (list != null && !list.isEmpty()) {
                adapter.submit(list);
                b.emptyView.setVisibility(View.GONE);
            } else {
                b.emptyView.setVisibility(View.VISIBLE);
            }
        });

        vm.getError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty())
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        vm.cargarContratos();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}
