package com.example.conexionbbdd;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificacionApi {

    @GET("/api/notificacion/usuario")
    Call<List<Notificacion>> obtenerNotificaciones(
            @Query("id_usuario") int idUsuario,
            @Query("tipoDestino") String tipoDestino
    );

    @GET("/api/notificacion/admin")
    Call<List<Notificacion>> obtenerNotificacionesAdmin(
            @Query("tipoDestino") String tipoDestino
    );

    @PUT("/api/notificacion/{id}/leida")
    Call<Void> marcarComoLeida(@Path("id") int id);
    @GET("/api/notificacion/{id}/detalle-reporte")
    Call<Reporte> getReporteYMarcarLeido(@Path("id") int idNotificacion);
}
