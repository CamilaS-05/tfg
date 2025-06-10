package com.example.conexionbbdd;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;

public class Administrador {

    private long id;

    @SerializedName("origenApp")
    private String origenApp;

    @SerializedName("contrasenaAdmin")
    private String contrasenaAdmin;

    @SerializedName("telefonoAdmin")
    private String telefonoAdmin;

    @SerializedName("nombrecompletoAdmin")
    private String nombrecompletoAdmin;

    @SerializedName("correoAdmin")
    private String correoAdmin;

    @SerializedName("usuarioAdmin")
    private String usuarioAdmin;

    // Getters y setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrigenApp() {
        return origenApp;
    }

    public void setOrigenApp(String origenApp) {
        this.origenApp = origenApp;
    }

    public String getContrasenaAdmin() {
        return contrasenaAdmin;
    }

    public void setContrasenaAdmin(String contrasenaAdmin) {
        this.contrasenaAdmin = contrasenaAdmin;
    }

    public String getTelefonoAdmin() {
        return telefonoAdmin;
    }

    public void setTelefonoAdmin(String telefonoAdmin) {
        this.telefonoAdmin = telefonoAdmin;
    }

    public String getNombrecompletoAdmin() {
        return nombrecompletoAdmin;
    }

    public void setNombrecompletoAdmin(String nombrecompletoAdmin) {
        this.nombrecompletoAdmin = nombrecompletoAdmin;
    }

    public String getCorreoAdmin() {
        return correoAdmin;
    }

    public void setCorreoAdmin(String correoAdmin) {
        this.correoAdmin = correoAdmin;
    }

    public String getUsuarioAdmin() {
        return usuarioAdmin;
    }

    public void setUsuarioAdmin(String usuarioAdmin) {
        this.usuarioAdmin = usuarioAdmin;
    }
}
