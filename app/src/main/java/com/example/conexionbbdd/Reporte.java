package com.example.conexionbbdd;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Reporte {
    private int id;
    private int id_usuario;
    private String asunto;
    private String descripcion;
    private String estado;
    @SerializedName("fechaCreacion")
    private String fechaCreacion;
    private int idUsuarioAsignado;

    private String nombreCreador;


    public int getId() { return id; }
    public int getId_usuario() { return id_usuario; }
    public String getAsunto() { return asunto; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }

    public String getFechaCreacion() {
        return fechaCreacion;
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getNombreCreador() { return nombreCreador; }
    public void setNombreCreador(String nombreCreador) { this.nombreCreador = nombreCreador; }

}
