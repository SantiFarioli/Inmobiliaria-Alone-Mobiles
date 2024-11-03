package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.santisoft.inmobiliariaalone.databinding.FragmentInmuebleBinding;


public class InmueblesFragment extends Fragment {

    private FragmentInmuebleBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InmueblesViewModel inmueblesViewModel =
                new ViewModelProvider(this).get(InmueblesViewModel.class);

        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        inmueblesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}