package com.santisoft.inmobiliariaalone.ui.contratos;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentContratosBinding;
import com.santisoft.inmobiliariaalone.model.Contrato;

public class ContratosFragment extends Fragment {

    private FragmentContratosBinding b;
    private ContratosViewModel vm;
    private ContratoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vm = new ViewModelProvider(this).get(ContratosViewModel.class);
        b  = FragmentContratosBinding.inflate(inflater, container, false);

        adapter = new ContratoAdapter(item -> {
            Bundle args = new Bundle();
            args.putInt("contratoId", item.getIdContrato());
            Navigation.findNavController(b.getRoot())
                    .navigate(R.id.pagosFragment, args);
        });

        b.rvContratos.setLayoutManager(new LinearLayoutManager(getContext()));
        b.rvContratos.setAdapter(adapter);

        vm.getLista().observe(getViewLifecycleOwner(), adapter::submit);
        vm.getError().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null && !msg.isEmpty()) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });

        return b.getRoot();
    }

    @Override public void onResume() {
        super.onResume();
        vm.cargar();
    }

    @Override public void onDestroyView() { super.onDestroyView(); b = null; }
}
