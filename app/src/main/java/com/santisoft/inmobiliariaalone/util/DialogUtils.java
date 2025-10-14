package com.santisoft.inmobiliariaalone.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.santisoft.inmobiliariaalone.R;

import cn.pedant.SweetAlert.SweetAlertDialog;


 //Helper centralizado para mostrar diálogos SweetAlert con estilo unificado.

public class DialogUtils {

    private static SweetAlertDialog currentDialog = null;

    /** Éxito */
    public static void showSuccess(Context ctx, @Nullable String titulo, @Nullable String mensaje) {
        runOnUiThread(() -> {
            dismissCurrent();
            currentDialog = new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(titulo != null ? titulo : "¡Éxito!")
                    .setContentText(mensaje != null ? mensaje : "Operación completada correctamente.")
                    .setConfirmText("Aceptar");
            currentDialog.show();
        });
    }

    // Error
    public static void showError(Context ctx, @Nullable String titulo, @Nullable String mensaje) {
        runOnUiThread(() -> {
            dismissCurrent();
            currentDialog = new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(titulo != null ? titulo : "Error")
                    .setContentText(mensaje != null ? mensaje : "Ocurrió un problema inesperado.")
                    .setConfirmText("Cerrar");
            currentDialog.show();
        });
    }

    // Advertencia
    public static void showWarning(Context ctx, @Nullable String titulo, @Nullable String mensaje) {
        runOnUiThread(() -> {
            dismissCurrent();
            currentDialog = new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(titulo != null ? titulo : "Atención")
                    .setContentText(mensaje != null ? mensaje : "Revisá los campos ingresados.")
                    .setConfirmText("Ok");
            currentDialog.show();
        });
    }

    // Cargando
    public static SweetAlertDialog showLoading(Context ctx, @Nullable String mensaje) {
        SweetAlertDialog dialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(ContextCompat.getColor(ctx, R.color.teal_700));
        dialog.setTitleText(mensaje != null ? mensaje : "Cargando...");
        dialog.setCancelable(false);
        dialog.show();
        currentDialog = dialog;
        return dialog;
    }

    // Ocultar diálogo de carga
    public static void hideLoading(@Nullable SweetAlertDialog dialog) {
        runOnUiThread(() -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismissWithAnimation();
            }
        });
    }

    // Confirmación (Aceptar / Cancelar)
    public static void showConfirm(Context ctx,
                                   @Nullable String titulo,
                                   @Nullable String mensaje,
                                   Runnable onConfirm) {

        runOnUiThread(() -> {
            dismissCurrent();

            SweetAlertDialog dialog = new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText(titulo != null ? titulo : "¿Estás seguro?");
            dialog.setContentText(mensaje != null ? mensaje : "Esta acción no se puede deshacer.");
            dialog.setConfirmText("Sí, continuar");
            dialog.setCancelText("Cancelar");
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                    if (onConfirm != null) onConfirm.run();
                }
            });
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                }
            });
            dialog.show();
            currentDialog = dialog;
        });
    }

    // Evita que queden dos diálogos abiertos simultáneamente
    private static void dismissCurrent() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismissWithAnimation();
            currentDialog = null;
        }
    }

    // Ejecuta acciones seguras en el hilo principal (UI Thread)
    private static void runOnUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
