package com.example.conexionbbdd;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ReporteApi {
    @GET("/api/reporte/reportes/ultimos")
    Call<List<ReporteDTO>> getUltimosReportes();

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

    @GET("/api/reporte/asignados")
    Call<List<ReporteDTO>> getReportesAsignados(
            @Query("id_usuario") long idUsuario
    );

    @GET("/api/reporte/listarReporteEstado")
    Call<List<ReporteDTO>> listarReportesDTOPorEstado(
            @Query("estado") String estado
    );

    @GET("/api/reporte/{id}")
    Call<Reporte> getReportePorId(@Path("id") int idReporte);
    @GET("/api/reporte/dto/{id}")
    Call<ReporteDTO> getReporteDtoPorId(@Path("id") int idReporte);




}


