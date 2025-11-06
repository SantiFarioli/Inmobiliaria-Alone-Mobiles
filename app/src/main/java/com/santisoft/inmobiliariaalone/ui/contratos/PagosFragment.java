package com.santisoft.inmobiliariaalone.ui.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.santisoft.inmobiliariaalone.databinding.FragmentPagosBinding;

public class PagosFragment extends Fragment {

    private FragmentPagosBinding b;
    private PagosViewModel vm;
    private PagoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentPagosBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(PagosViewModel.class);

        adapter = new PagoAdapter();
        b.rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        b.rvPagos.setAdapter(adapter);

        vm.getPagos().observe(getViewLifecycleOwner(), pagos -> {
            if (pagos != null && !pagos.isEmpty()) {
                adapter.submit(pagos);
                b.emptyView.setVisibility(View.GONE);
            } else {
                b.emptyView.setVisibility(View.VISIBLE);
            }
        });

        vm.getError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty())
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        // 📦 Obtener contratoId desde navegación
        if (getArguments() != null) {
            int contratoId = getArguments().getInt("contratoId", -1);
            if (contratoId != -1) {
                b.tvTitulo.setText("Pagos del Contrato #" + contratoId);
                vm.cargarPagos(contratoId);
            }
        }

        return b.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}
