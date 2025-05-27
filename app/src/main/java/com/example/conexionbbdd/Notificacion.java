package com.example.conexionbbdd;

import java.io.Serializable;

public class Notificacion implements Serializable {
    private int id;
    private Integer idUsuario;
    private String mensaje;
    private boolean leido;
    private String fecha;
    private String tipo_destino;
    private Integer idReporte; // Asegúrate que venga del backend

    // Getters y setters
    public int getId() { return id; }

    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensaje() { return mensaje; }
    public boolean isLeido() { return leido; }
    public String getFecha() { return fecha; }
    public String getTipo_destino() { return tipo_destino; }

    public void setId(int id) { this.id = id; }

    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public void setLeido(boolean leido) { this.leido = leido; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setTipo_destino(String tipo_destino) { this.tipo_destino = tipo_destino; }

    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }
}
