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
        setContentView(R.layout.inicio_admin); // Asegúrate de que este layout exista

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

        // 🔒 VALIDACIONES DETALLADAS
        if (usuario.isEmpty() && clave.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(this, "Por favor, introduce el usuario y la contraseña", Toast.LENGTH_SHORT).show());
            return;
        }

        if (usuario.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(this, "El campo usuario está vacío", Toast.LENGTH_SHORT).show());
            return;
        }

        if (clave.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(this, "El campo contraseña está vacío", Toast.LENGTH_SHORT).show());
            return;
        }

        // Crear el ProgressDialog en el hilo principal (UI Thread)
        ProgressDialog progressDialog = new ProgressDialog(LoginAdminActivity.this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);

        // Asegurarse de que se muestre en el hilo principal
        runOnUiThread(() -> progressDialog.show());

        // Realizar la solicitud en un hilo secundario (hilo de fondo)
        Call<String> call = administradorApi.loginAdmin(usuario, clave, origenApp);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Desmontar el ProgressDialog en el hilo principal
                runOnUiThread(() -> progressDialog.dismiss());

                if (response.isSuccessful() && response.body() != null) {
                    String resultado = response.body();
                    switch (resultado) {
                        case "ACCESO_CONCEDIDO":
                            runOnUiThread(() -> Toast.makeText(LoginAdminActivity.this, "Inicio de sesión exitoso como administrador", Toast.LENGTH_SHORT).show());

                            SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("nombre_admin", usuario);
                            editor.apply();

                            Intent intent = new Intent(LoginAdminActivity.this, MainActivityAdmin.class);
                            startActivity(intent);
                            finish();
                            break;

                        case "CONTRASENA_INCORRECTA":
                            runOnUiThread(() -> Toast.makeText(LoginAdminActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show());
                            break;

                        case "USUARIO_NO_EXISTE":
                            runOnUiThread(() -> Toast.makeText(LoginAdminActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show());
                            break;

                        case "ACCESO_DENEGADO_ORIGEN_APP":
                            runOnUiThread(() -> Toast.makeText(LoginAdminActivity.this, "No tienes permiso desde esta app", Toast.LENGTH_SHORT).show());
                            break;

                        default:
                            runOnUiThread(() -> Toast.makeText(LoginAdminActivity.this, "Error: " + resultado, Toast.LENGTH_SHORT).show());
                            break;
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginAdminActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Desmontar el ProgressDialog en el hilo principal
                runOnUiThread(() -> progressDialog.dismiss());

                Log.e("LOGIN_ADMIN_ERROR", t.getMessage());

                if (t instanceof IOException) {
                    runOnUiThread(() -> Toast.makeText(LoginAdminActivity.this, "Revisa tu conexión a Internet", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginAdminActivity.this, "Error inesperado en el servidor", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }


}
