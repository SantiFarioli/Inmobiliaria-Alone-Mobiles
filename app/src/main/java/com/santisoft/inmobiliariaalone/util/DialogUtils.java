package com.santisoft.inmobiliariaalone.util;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Clase helper centralizada para mostrar diálogos SweetAlert.
 * Evita duplicar lógica en fragments.
 */
public class DialogUtils {

    // Éxito
    public static void showSuccess(Context ctx, @Nullable String titulo, @Nullable String mensaje) {
        new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titulo != null ? titulo : "¡Éxito!")
                .setContentText(mensaje != null ? mensaje : "Operación completada correctamente.")
                .setConfirmText("Aceptar")
                .show();
    }

    // Error
    public static void showError(Context ctx, @Nullable String titulo, @Nullable String mensaje) {
        new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titulo != null ? titulo : "Error")
                .setContentText(mensaje != null ? mensaje : "Ocurrió un problema inesperado.")
                .setConfirmText("Cerrar")
                .show();
    }

    // Advertencia
    public static void showWarning(Context ctx, @Nullable String titulo, @Nullable String mensaje) {
        new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo != null ? titulo : "Atención")
                .setContentText(mensaje != null ? mensaje : "Revisá los campos ingresados.")
                .setConfirmText("Ok")
                .show();
    }

    // Cargando
    public static SweetAlertDialog showLoading(Context ctx, @Nullable String mensaje) {
        SweetAlertDialog dialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(ContextCompat.getColor(ctx, android.R.color.holo_blue_light));
        dialog.setTitleText(mensaje != null ? mensaje : "Cargando...");
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void hideLoading(SweetAlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) dialog.dismissWithAnimation();
    }
}
