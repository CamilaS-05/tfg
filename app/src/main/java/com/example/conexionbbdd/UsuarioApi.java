package com.example.conexionbbdd;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @DELETE("api/usuarios/{id}")
    Call<ResponseBody> eliminarUsuario(
            @Path("id") Long id,
            @Query("origenApp") String origenApp
    );

    @PUT("api/usuarios/actualizar")
    Call<String> actualizarUsuario(@Body UsuarioEditar usuarioEditar);

    @GET("api/usuarios/{id}")
    Call<Usuario> getUsuarioPorId(@Path("id") Long id);



    @GET("/api/usuarios/buscar/{usuario}")
    Call<Usuario> getUsuarioPorNombre(@Path("usuario") String usuario);

}
