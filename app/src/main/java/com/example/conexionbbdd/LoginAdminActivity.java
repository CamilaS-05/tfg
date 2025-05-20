package com.example.conexionbbdd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAdminActivity extends AppCompatActivity {

    EditText usuarioAdmin, contrasenaAdmin;
    MaterialButton botonIniciarSesionAdmin;
    TextView iniciarComoUsuario;
    AdministradorApi administradorApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_admin);

        usuarioAdmin = findViewById(R.id.usuario_admin);
        contrasenaAdmin = findViewById(R.id.contrasena_admin);
        botonIniciarSesionAdmin = findViewById(R.id.boton_iniciar_sesion_admin);
        iniciarComoUsuario = findViewById(R.id.textviewIniciousuario);

        administradorApi = RetrofitClient.getRetrofitInstance().create(AdministradorApi.class);

        botonIniciarSesionAdmin.setOnClickListener(v -> iniciarSesionAdmin());

        iniciarComoUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(LoginAdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void iniciarSesionAdmin() {
        String usuario = usuarioAdmin.getText().toString().trim();
        String clave = contrasenaAdmin.getText().toString().trim();
        String origenApp = "incidencias";

        if (usuario.isEmpty() && clave.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce el usuario y la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (usuario.isEmpty()) {
            Toast.makeText(this, "El campo usuario está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        if (clave.isEmpty()) {
            Toast.makeText(this, "El campo contraseña está vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(LoginAdminActivity.this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<LoginResponse> call = administradorApi.loginAdmin(usuario, clave, origenApp);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if ("ACCESO_CONCEDIDO".equals(loginResponse.getMensaje())) {
                        Toast.makeText(LoginAdminActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("nombre_admin", loginResponse.getUsuario());
                        editor.putLong("id_admin", loginResponse.getId()); // ← ESTA LÍNEA
                        editor.apply();


                        Intent intent = new Intent(LoginAdminActivity.this, MainActivityAdmin.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginAdminActivity.this, "Error: " + loginResponse.getMensaje(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginAdminActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginAdminActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
