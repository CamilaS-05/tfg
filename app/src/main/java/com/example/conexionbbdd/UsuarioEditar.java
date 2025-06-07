package com.example.conexionbbdd;

public class UsuarioEditar {
    private Long id;
    private String nombrecompleto;
    private String correo;
    private String telefono;
    private String usuario;
    private String contrasena;

   private String origen_app;

    // Constructor completo
    public UsuarioEditar(Long id, String nombrecompleto, String correo, String telefono, String usuario, String contrasena, String origen_app) {
        this.id = id;
        this.nombrecompleto = nombrecompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.origen_app = origen_app;
    }

    // getters y setters

    public String getNombrecompleto() {
        return nombrecompleto;
    }

    public void setNombrecompleto(String nombrecompleto) {
        this.nombrecompleto = nombrecompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigen_app() {
        return origen_app;
    }

    public void setOrigen_app(String origen_app) {
        this.origen_app = origen_app;
    }
}
