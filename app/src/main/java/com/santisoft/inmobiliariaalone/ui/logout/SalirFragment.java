package com.santisoft.inmobiliariaalone.ui.logout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.santisoft.inmobiliariaalone.MainActivity;
import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.databinding.FragmentSalirBinding;


public class SalirFragment extends Fragment {
    private FragmentSalirBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSalirBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Mostrar el diálogo de confirmación cuando se acceda a esta vista
        showExitConfirmationDialog();

        return root;
    }

    private void showExitConfirmationDialog() {
        // Crear el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmación de salida");
        builder.setMessage("¿Está seguro de que desea salir?");

        // Opción "Sí"
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Salir de la aplicación
                getActivity().finishAffinity(); // Cierra todas las actividades
            }
        });

        // Opción "No"
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Redirigir al fragmento de Farmacias
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragment", "nav_pharmacy"); // Opcional: indica qué fragmento mostrar
                startActivity(intent);
            }
        });

        // Mostrar el cuadro de diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}