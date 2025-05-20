package com.example.conexionbbdd;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Body;

import java.util.List;

public interface ReporteApi {

    @GET("/api/reporte/usuario")
    Call<List<Reporte>> obtenerReportesUsuario(@Query("id_usuario") int idUsuario);

    @PUT("/api/reporte/cambiarEstado")
    Call<String> cambiarEstado(
            @Query("id_reporte") int idReporte,
            @Query("nuevo_estado") String nuevoEstado);

    @GET("/api/reporte/listar")
    Call<List<Reporte>> obtenerReportes();
    @PUT("/api/reporte/asignar")
    Call<String> asignarUsuario(@Query("id_reporte") int idReporte, @Query("id_usuario") int idUsuario);
    @GET("usuarios")
    Call<List<Usuario>> obtenerUsuarios();

    @GET("reporte")
    Call<List<Reporte>> obtenerReportesPorEstado(@Query("estado") String estado);
}
