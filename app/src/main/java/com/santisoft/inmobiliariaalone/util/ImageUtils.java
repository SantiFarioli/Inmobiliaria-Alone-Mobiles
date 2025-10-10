package com.santisoft.inmobiliariaalone.util;

import android.content.Context;
import android.net.Uri;
import java.io.*;

public class ImageUtils {

    // Guarda la imagen elegida (content://) en almacenamiento interno y devuelve su ruta file://
    public static String saveToInternalStorage(Context context, Uri uri) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String fileName = "inmueble_" + System.currentTimeMillis() + ".jpg";

        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            File directory = new File(context.getFilesDir(), "inmuebles");
            if (!directory.exists()) directory.mkdirs();

            File file = new File(directory, fileName);
            outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.flush();
            return file.toURI().toString(); // Devuelve "file:///data/user/0/.../inmueble_1234.jpg"

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException ignored) {}
        }
    }
}
