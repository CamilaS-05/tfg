package com.example.conexionbbdd;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InicioAdmin extends AppCompatActivity {

    EditText usuarioAdmin, contrasenaAdmin;
    MaterialButton botonIniciarSesionAdmin;
    TextView iniciarComoUsuario;
    Connection con;

    public InicioAdmin() {
            com.example.conexionbbdd.Conexion instanceConnection = new com.example.conexionbbdd.Conexion();
            con = instanceConnection.connect();

        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_admin);

        usuarioAdmin = findViewById(R.id.usuario_admin);
        contrasenaAdmin = findViewById(R.id.contrasena_admin);
        botonIniciarSesionAdmin = findViewById(R.id.boton_iniciar_sesion_admin);
        iniciarComoUsuario = findViewById(R.id.textviewIniciousuario);

        botonIniciarSesionAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesionAdmin();
            }
        });

        iniciarComoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioAdmin.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void iniciarSesionAdmin() {
        try {
            String usuario = usuarioAdmin.getText().toString().trim();
            String clave = contrasenaAdmin.getText().toString().trim();

            if (usuario.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (con == null) {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar si el usuario existe
            PreparedStatement pst = con.prepareStatement("SELECT * FROM Administradores WHERE usuarioAdmin = ?");
            pst.setString(1, usuario);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar contraseña
            String contrasenaCorrecta = rs.getString("contrasenaAdmin");
            if (!clave.equals(contrasenaCorrecta)) {
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar el nombre del administrador en SharedPreferences
            String nombreAdmin = rs.getString("nombreAdmin"); // Asegúrate que "nombreAdmin" existe en tu tabla
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nombre_admin", nombreAdmin);  // Guardamos el nombre del administrador
            editor.apply();

            Toast.makeText(this, "Inicio de sesión exitoso como administrador", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(InicioAdmin.this, MainActivityAdmin.class);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Log.e("LOGIN_ADMIN_ERROR", e.getMessage());
        }
    }

}
