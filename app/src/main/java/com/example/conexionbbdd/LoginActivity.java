package com.example.conexionbbdd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText usuario, contrasena;
    TextView textviewRegistro, textviewAdmin;
    Button botoningresar;
    ExecutorService executorService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usuario = findViewById(R.id.usuario);
        contrasena = findViewById(R.id.contrasena);
        textviewRegistro = findViewById(R.id.textviewRegistro);
        textviewAdmin = findViewById(R.id.textviewAdmin);
        botoningresar = findViewById(R.id.botoniniciarsesion);

        executorService = Executors.newSingleThreadExecutor();

        botoningresar.setOnClickListener(v -> executorService.execute(this::login));

        textviewRegistro.setOnClickListener(v -> {
            Intent registro = new Intent(getApplicationContext(), RegistroActivity.class);
            startActivity(registro);
        });

        textviewAdmin.setOnClickListener(v -> {
            Intent admin = new Intent(getApplicationContext(), LoginAdminActivity.class);
            startActivity(admin);
        });
    }

    private void login() {
        String usuarioInput = usuario.getText().toString().trim();
        String contrasenaInput = contrasena.getText().toString().trim();
        String origenApp = "incidencias";

        // ✅ VALIDACIONES DETALLADAS
        if (usuarioInput.isEmpty() && contrasenaInput.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(this, "Por favor, introduce el usuario y la contraseña", Toast.LENGTH_SHORT).show());
            return;
        }

        if (usuarioInput.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(this, "El campo usuario está vacío", Toast.LENGTH_SHORT).show());
            return;
        }

        if (contrasenaInput.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(this, "El campo contraseña está vacío", Toast.LENGTH_SHORT).show());
            return;
        }

        // Crear el ProgressDialog en el hilo principal (UI Thread)
        runOnUiThread(() -> {
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Iniciando sesión...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Llamar al API en el hilo de fondo (sin bloquear la UI)
            UsuarioApi usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);
            Call<String> call = usuarioApi.login(usuarioInput, contrasenaInput, origenApp);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    progressDialog.dismiss();

                    if (response.isSuccessful() && response.body() != null) {
                        String resultado = response.body();

                        switch (resultado) {
                            case "ACCESO_CONCEDIDO":
                                Toast.makeText(LoginActivity.this, "Acceso exitoso", Toast.LENGTH_SHORT).show();

                                SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                                prefs.edit().putString("nombre_usuario", usuarioInput).apply();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("nombre_usuario", usuarioInput);
                                startActivity(intent);
                                finish();
                                break;

                            case "CONTRASENA_INCORRECTA":
                                Toast.makeText(LoginActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                break;

                            case "USUARIO_NO_EXISTE":
                                Toast.makeText(LoginActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                                break;

                            case "ACCESO_DENEGADO_ORIGEN_APP":
                                Toast.makeText(LoginActivity.this, "Acceso denegado desde esta app", Toast.LENGTH_SHORT).show();
                                break;

                            default:
                                Toast.makeText(LoginActivity.this, "Respuesta desconocida: " + resultado, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

}

