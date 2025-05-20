package com.example.conexionbbdd;

public class LoginResponse {
    private long id;          // ID del admin
    private String usuario;   // Nombre de usuario (o como se llame en tu JSON)
    private String mensaje;   // Mensaje de estado, ejemplo "ACCESO_CONCEDIDO"

    // Constructor vacío necesario para Retrofit/Gson
    public LoginResponse() {}

    // Getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
