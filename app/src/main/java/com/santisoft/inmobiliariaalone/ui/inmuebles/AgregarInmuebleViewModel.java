package com.santisoft.inmobiliariaalone.ui.inmuebles;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.exifinterface.media.ExifInterface;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.retrofit.ApClient;
import com.santisoft.inmobiliariaalone.retrofit.ApClient.InmobliariaService;
import com.santisoft.inmobiliariaalone.util.DialogEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarInmuebleViewModel extends AndroidViewModel {

    private final MutableLiveData<Uri> uriFoto = new MutableLiveData<>();
    private final MutableLiveData<DialogEvent> dialogEvent = new MutableLiveData<>();

    public LiveData<Uri> getUriFoto() { return uriFoto; }
    public LiveData<DialogEvent> getDialogEvent() { return dialogEvent; }

    public AgregarInmuebleViewModel(Application app) { super(app); }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
            Uri dataUri = result.getData().getData();
            if (dataUri != null) {
                uriFoto.setValue(dataUri);
                Log.d("AgregarVM", " Foto seleccionada: " + dataUri);
            }
        }
    }

    public void guardarInmueble(String direccion, String precioStr, String uso, String tipo,
                                String ambientesStr, boolean disponible) {

        if (direccion.isEmpty() || precioStr.isEmpty() || uso.isEmpty() ||
                tipo.isEmpty() || ambientesStr.isEmpty()) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.ERROR, "Campos incompletos", "Debe completar todos los campos."));
            return;
        }

        if (uriFoto.getValue() == null) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.WARNING, "Imagen requerida", "Debe seleccionar una imagen antes de guardar."));
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);
            int ambientes = Integer.parseInt(ambientesStr);

            Inmueble inmueble = new Inmueble(
                    0,
                    direccion,
                    uso,
                    tipo,
                    ambientes,
                    precio,
                    disponible ? "Disponible" : "No disponible",
                    0,
                    null,
                    null,
                    ""
            );

            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.CONFIRM, "Confirmar publicación", "¿Desea crear el inmueble con los datos ingresados?"));

        } catch (NumberFormatException e) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.ERROR, "Formato incorrecto", "Revisar formato de precio o ambientes."));
        }
    }

    public void enviarInmueble(Inmueble inmueble) {
        dialogEvent.postValue(new DialogEvent(DialogEvent.Type.LOADING, "Subiendo inmueble...", null));
        Log.d("AgregarVM", " Enviando inmueble al servidor...");

        try {
            byte[] bytes = obtenerBytesImagen();
            RequestBody bodyImagen = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "foto.jpg", bodyImagen);

            Gson gson = new Gson();
            String json = gson.toJson(inmueble);
            RequestBody bodyInmueble = RequestBody.create(MediaType.parse("application/json"), json);

            InmobliariaService service = ApClient.getInmobiliariaService(getApplication().getApplicationContext());
            Call<Inmueble> call = service.cargarInmueble(imagenPart, bodyInmueble);

            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    dialogEvent.postValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
                    if (response.isSuccessful()) {
                        dialogEvent.postValue(new DialogEvent(DialogEvent.Type.SUCCESS, "Inmueble publicado", "El inmueble se creó correctamente."));
                        Log.d("AgregarVM", " Inmueble creado con éxito");
                    } else {
                        dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR, "Error al crear inmueble", "Código: " + response.code()));
                        Log.e("AgregarVM", " Error HTTP " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable t) {
                    dialogEvent.postValue(new DialogEvent(DialogEvent.Type.HIDE_LOADING, null, null));
                    dialogEvent.postValue(new DialogEvent(DialogEvent.Type.ERROR, "Error de conexión", t.getMessage()));
                    Log.e("AgregarVM", " Error de conexión: " + t.getMessage());
                }
            });

        } catch (IOException e) {
            dialogEvent.setValue(new DialogEvent(DialogEvent.Type.ERROR, "Error procesando imagen", e.getMessage()));
            Log.e("AgregarVM", " Error procesando imagen: " + e.getMessage());
        }
    }

    private byte[] obtenerBytesImagen() throws IOException {
        Uri uri = uriFoto.getValue();
        InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // Rotar imagen según metadatos EXIF
        InputStream exifStream = getApplication().getContentResolver().openInputStream(uri);
        ExifInterface exif = new ExifInterface(exifStream);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            bitmap = rotate(bitmap, 90);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            bitmap = rotate(bitmap, 180);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            bitmap = rotate(bitmap, 270);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    private Bitmap rotate(Bitmap b, int angle) {
        android.graphics.Matrix m = new android.graphics.Matrix();
        m.postRotate(angle);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
    }
}
