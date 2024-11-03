package com.santisoft.inmobiliariaalone.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.santisoft.inmobiliariaalone.model.LoginResponse;
import com.santisoft.inmobiliariaalone.model.Propietario;
import com.santisoft.inmobiliariaalone.model.RestablecerContrasenaRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ApClient {
    private static final String URL_BASE = "http://192.168.0.108:5157/api/";

    public static InmobliariaService getInmobiliariaService() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(InmobliariaService.class);
    }

    public interface InmobliariaService {
        @FormUrlEncoded
        @POST("Propietarios/login")
        Call<LoginResponse> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

        @GET("Propietarios/perfil")
        Call<Propietario> obtenerPerfil(@Header("Authorization") String token);

        @FormUrlEncoded
        @POST("Propietarios/solicitar-restablecimiento")
        Call<ResponseBody> solicitarRecuperacion(@Field("email") String email);

        @POST("Propietarios/{id}/restablecer-contrasena")
        Call<ResponseBody> restablecerContrasena(@Path("id") int id, @Body RestablecerContrasenaRequest request);
    }
}