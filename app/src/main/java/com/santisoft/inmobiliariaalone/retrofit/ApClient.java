package com.santisoft.inmobiliariaalone.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.santisoft.inmobiliariaalone.data.local.SessionManager;
import com.santisoft.inmobiliariaalone.model.CambioPasswordRequest;
import com.santisoft.inmobiliariaalone.model.Contrato;
import com.santisoft.inmobiliariaalone.model.Inmueble;
import com.santisoft.inmobiliariaalone.model.Inquilino;
import com.santisoft.inmobiliariaalone.model.LoginResponse;
import com.santisoft.inmobiliariaalone.model.Pago;
import com.santisoft.inmobiliariaalone.model.Propietario;
import com.santisoft.inmobiliariaalone.model.RestablecerContrasenaRequest;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApClient {

    private static final String URL_BASE = "http://192.168.0.100:5157/api/";

    private static Gson gson() {
        return new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
    }


    public static InmobliariaService getInmobiliariaService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .build();
        return retrofit.create(InmobliariaService.class);
    }


    public static InmobliariaService getInmobiliariaService(Context ctx) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        SessionManager session = new SessionManager(ctx);
                        String token = session.getToken();

                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();

                        builder.header("Accept", "application/json");
                        if (token != null && !token.isEmpty()) {
                            // Evitamos doble prefijo Bearer
                            if (token.startsWith("Bearer ")) {
                                builder.header("Authorization", token);
                            } else {
                                builder.header("Authorization", "Bearer " + token);
                            }
                            android.util.Log.d("TOKEN_DEBUG", "Authorization header => [" + token + "]");
                        }

                        return chain.proceed(builder.build());

                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson()))
                .build();

        return retrofit.create(InmobliariaService.class);
    }


    public interface InmobliariaService {
        // ---------- Propietarios ----------
        @FormUrlEncoded
        @POST("Propietarios/login")
        Call<LoginResponse> login(
                @Field("Usuario") String usuario,
                @Field("Clave") String clave
        );

        @GET("Propietarios/perfil")
        Call<Propietario> obtenerPerfil();

        @PUT("Propietarios/cambiar-password")
        Call<Void> cambiarPassword(@Body CambioPasswordRequest body);


        @FormUrlEncoded
        @POST("Propietarios/solicitar-restablecimiento")
        Call<ResponseBody> solicitarRecuperacion(@Field("email") String email);

        @POST("Propietarios/{id}/restablecer-contrasena")
        Call<ResponseBody> restablecerContrasena(
                @Path("id") int id,
                @Body RestablecerContrasenaRequest request
        );

        @PUT("Propietarios/perfil")
        Call<Propietario> propietarioUpdate(@Body Propietario body);

        @PUT("Propietarios/{id}")
        Call<Propietario> propietarioUpdateById(@Path("id") int id, @Body Propietario body);

        // ---------- Inmuebles ----------
        @GET("Inmuebles")
        Call<List<Inmueble>> inmueblesGetAll();

        @GET("Inmuebles/{id}")
        Call<Inmueble> inmuebleGet(@Path("id") int id);

        @PUT("Inmuebles/{id}")
        Call<ResponseBody> inmuebleUpdate(@Path("id") int id, @Body Inmueble body);

        @Multipart
        @POST("Inmuebles/cargar")
        Call<Inmueble> cargarInmueble(
                @Part MultipartBody.Part imagen,
                @Part("inmueble") RequestBody inmueble
        );

        // ---------- Contratos ----------
        @GET("Contratos")
        Call<List<Contrato>> contratosGetAll();

        // coincide con tu controlador [HttpGet("vigentes/mios")]
        @GET("Contratos/vigentes/mios")
        Call<List<Contrato>> contratosVigentesMios();

        // ---------- Inquilinos ----------
        @GET("Inquilinos")
        Call<List<Inquilino>> inquilinosGetAll();

        // ---------- Pagos ----------
        @GET("Pagos/por-contrato/{id}")
        Call<List<Pago>> pagosPorContrato(@Path("id") int contratoId);
    }
}
