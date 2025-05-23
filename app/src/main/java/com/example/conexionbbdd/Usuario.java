package com.example.conexionbbdd;


import com.google.gson.annotations.SerializedName;

public class Usuario {
    private int id;
    private String nombrecompleto;
    private String usuario;  // <-- AÑADIR ESTO

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombrecompleto() { return nombrecompleto; }
    public void setNombrecompleto(String nombrecompleto) { this.nombrecompleto = nombrecompleto; }

    public String getUsuario() { return usuario; }  // <-- GETTER
    public void setUsuario(String usuario) { this.usuario = usuario; }  // <-- SETTER

    @Override
    public String toString() {
        return usuario != null ? usuario : "Usuario sin nombre";  // <-- MOSTRAR USUARIO EN SPINNER
    }
}
