package com.example.conexionbbdd;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AdministradorApi {

    @FormUrlEncoded
    @POST("/api/administradores/login")
    Call<String> loginAdmin(
            @Field("usuarioadmin") String usuario,
            @Field("contrasenaadmin") String contrasena,
            @Field("origen_app") String origenApp
    );

    @FormUrlEncoded
    @POST("/api/administradores/registro")
    Call<String> registrarAdmin(
            @Field("nombrecompletoadmin") String nombrecompleto,
            @Field("correoadmin") String correo,
            @Field("telefonoadmin") String telefono,
            @Field("usuarioadmin") String usuario,
            @Field("contrasenaadmin") String contrasena,
            @Field("origen_app") String origenApp
    );
}
