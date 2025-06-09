package com.example.conexionbbdd;

import com.google.gson.annotations.SerializedName;

public class Usuario {


    @SerializedName("id")
    private int id;

    @SerializedName("nombreCompleto") // Cambiado para que coincida con el backend
    private String nombreCompleto;
    @Override
    public String toString() {
        return nombreCompleto;
    }


    @SerializedName("telefono")
    private String telefono;

    @SerializedName("correo")
    private String correo;

    @SerializedName("usuario")
    private String usuario;

    // getters y setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
}
