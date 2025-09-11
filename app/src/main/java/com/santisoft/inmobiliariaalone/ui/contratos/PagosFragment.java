package com.santisoft.inmobiliariaalone.ui.contratos;

import android.os.Bundle;
import android.view.*;
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
        vm = new ViewModelProvider(this).get(PagosViewModel.class);
        b  = FragmentPagosBinding.inflate(inflater, container, false);

        adapter = new PagoAdapter();
        b.rvPagos.setLayoutManager(new LinearLayoutManager(getContext()));
        b.rvPagos.setAdapter(adapter);

        vm.getLista().observe(getViewLifecycleOwner(), adapter::submit);
        vm.getError().observe(getViewLifecycleOwner(),
                msg -> { if (msg!=null && !msg.isEmpty()) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show(); });

        int contratoId = getArguments()!=null ? getArguments().getInt("contratoId", 0) : 0;
        vm.cargar(contratoId);

        return b.getRoot();
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
