package com.example.conexionbbdd;

public class Reporte {
    private int id;
    private int id_usuario;
    private String asunto;
    private String descripcion;
    private String estado;
    private String fecha_creacion;
    private int idUsuarioAsignado;


    public int getId() { return id; }
    public int getId_usuario() { return id_usuario; }
    public String getAsunto() { return asunto; }
    public String getDescripcion() { return descripcion; }
    public String getEstado() { return estado; }
    public String getFecha_creacion() { return fecha_creacion; }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuarioAsignado() {
        return idUsuarioAsignado;
    }

    public void setIdUsuarioAsignado(int idUsuarioAsignado) {
        this.idUsuarioAsignado = idUsuarioAsignado;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}
