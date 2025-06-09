package com.example.conexionbbdd;

public class UsuarioEditar {
    private Long id;
    private String nombrecompleto;
    private String correo;
    private String telefono;
    private String usuario;
    private String contrasena;
    private String origen_app;

    public UsuarioEditar(Long id, String nombrecompleto, String correo, String telefono, String usuario, String contrasena, String origen_app) {
        this.id = id;
        this.nombrecompleto = nombrecompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.origen_app = origen_app;
    }

    public Long getId() {
        return id;
    }

    public String getNombrecompleto() {
        return nombrecompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getOrigen_app() {
        return origen_app;
    }
}
