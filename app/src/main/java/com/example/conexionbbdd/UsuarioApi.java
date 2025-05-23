package com.example.conexionbbdd;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UsuarioApi {

    @FormUrlEncoded
    @POST("/api/usuarios/login")
    Call<LoginResponse> login(
            @Field("usuario") String usuario,
            @Field("contrasena") String contrasena,
            @Field("origen_app") String origenApp

    );
    @GET("/api/usuarios/incidencias")
    Call<List<Usuario>> obtenerUsuariosIncidencias();
    @FormUrlEncoded
    @POST("/api/usuarios/registro")
    Call<String> registrarUsuario(
            @Field("nombrecompleto") String nombrecompleto,
            @Field("correo") String correo,
            @Field("telefono") String telefono,
            @Field("usuario") String usuario,
            @Field("contrasena") String contrasena,
            @Field("origen_app") String origenApp
    );
}
//comentario
