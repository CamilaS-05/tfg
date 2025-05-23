package com.example.conexionbbdd;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

import java.util.List;

public interface ReporteApi {

    @PUT("/api/reporte/cambiarEstado")
    Call<String> cambiarEstado(
            @Query("id_reporte") int idReporte,
            @Query("nuevo_estado") String nuevoEstado);
    @GET("/api/reporte/listarReporte")
    Call<List<ReporteDTO>> listarReportesDTO();

    // Asignar usuario a un reporte
    @PUT("api/reporte/asignar")
    Call<String> asignarUsuario(
            @Query("id_reporte") int idReporte,
            @Query("id_usuario") int idUsuario
    );

    @GET("/api/reporte/listarReporteEstado")
    Call<List<ReporteDTO>> listarReportesDTOPorEstado(
            @Query("estado") String estado
    );





}


