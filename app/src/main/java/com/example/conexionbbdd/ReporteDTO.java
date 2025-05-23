package com.example.conexionbbdd;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ReporteDTO {
    private int id;
    private int idUsuario;
    private String asunto;
    private String descripcion;
    private String nombreAsignado;
    private String estado;
    private String fecha; // Aquí el string de fecha en formato ISO8601

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getNombreAsignado() { return nombreAsignado; }
    public void setNombreAsignado(String nombreAsignado) { this.nombreAsignado = nombreAsignado; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}

