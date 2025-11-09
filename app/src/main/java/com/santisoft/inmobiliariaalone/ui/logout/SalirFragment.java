package com.santisoft.inmobiliariaalone.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.santisoft.inmobiliariaalone.R;
import com.santisoft.inmobiliariaalone.data.local.SessionManager;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SalirFragment extends Fragment {
    private SessionManager session;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        session = new SessionManager(requireContext());
        View root = inflater.inflate(R.layout.fragment_salir, container, false);
        mostrarDialogoConfirmacion();
        return root;
    }

    private void mostrarDialogoConfirmacion() {
        SweetAlertDialog dialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("Cerrar sesión");
        dialog.setContentText("¿Estás seguro de que deseas salir?");
        dialog.setConfirmText("Sí, cerrar sesión");
        dialog.setCancelText("Cancelar");

        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                session.clear();
                sweetAlertDialog.dismissWithAnimation();

                Intent intent = new Intent(requireContext(), com.santisoft.inmobiliariaalone.ui.login.AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();

            }
        });

        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                requireActivity().onBackPressed();
            }
        });

        dialog.show();
    }
}
