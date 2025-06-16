package com.example.conexionbbdd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistroActivity extends AppCompatActivity {

    EditText nombreapellidos, email, telefono, usuario, clave, repetirClave;
    Button registrar;
    TextView ingresar;
    UsuarioApi usuarioApi;
    public RegistroActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuarios);

        nombreapellidos = findViewById(R.id.txtnomapellidos);
        email = findViewById(R.id.txtemail);
        telefono = findViewById(R.id.txttelefono);
        usuario = findViewById(R.id.txtusuario);
        clave = findViewById(R.id.txtclave);
        repetirClave = findViewById(R.id.txtrepetirclave);
        registrar = findViewById(R.id.btnregistrar);
        ingresar = findViewById(R.id.lbliniciarsesion);
        usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar(view);
            }
        });

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ingresar = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(ingresar);
                finish();
            }
        });
    }

    private void registrar(View view) {
        String nombreCompleto = nombreapellidos.getText().toString().trim();
        String usuarioInput = usuario.getText().toString().trim();
        String correoInput = email.getText().toString().trim();
        String telefonoInput = telefono.getText().toString().trim();
        String password = clave.getText().toString();
        String confirmPassword = repetirClave.getText().toString();

        int camposVacios = 0;
        if (nombreCompleto.isEmpty()) camposVacios++;
        if (usuarioInput.isEmpty()) camposVacios++;
        if (correoInput.isEmpty()) camposVacios++;
        if (telefonoInput.isEmpty()) camposVacios++;
        if (password.isEmpty()) camposVacios++;
        if (confirmPassword.isEmpty()) camposVacios++;

        if (camposVacios > 1) {
            showToast("Por favor, rellena todos los campos obligatorios.");
            return;
        }

        // 🔒 Validaciones de campos vacíos
        if (nombreCompleto.isEmpty()) {
            showToast("El nombre y apellidos no pueden estar vacíos");
            return;
        }

        if (usuarioInput.isEmpty()) {
            showToast("El nombre de usuario no puede estar vacío");
            return;
        }

        if (correoInput.isEmpty()) {
            showToast("El correo no puede estar vacío");
            return;
        }

        if (telefonoInput.isEmpty()) {
            showToast("El teléfono no puede estar vacío");
            return;
        }

        if (password.isEmpty() ) {
            showToast("La contraseña no puede estar vacía");
            return;
        }
        if (confirmPassword.isEmpty()){
            showToast("Confirma tu contraseña");
            return;
        }

        // 📏 Validaciones de longitud y formato
        if (usuarioInput.length() < 3) {
            showToast("El usuario debe tener al menos 3 caracteres");
            return;
        }

        if (!correoInput.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showToast("El correo debe ser válido");
            return;
        }

        if (!telefonoInput.matches("\\d{9}")) {
            showToast("El teléfono debe tener 9 dígitos numéricos");
            return;
        }

        // 🔐 Validaciones de contraseña
       if (!password.equals(confirmPassword)) {
            showToast("Las contraseñas no coinciden");
            return;
        }

        if (password.length() < 8) {
            showToast("La contraseña debe tener al menos 8 caracteres");
            return;
        }

        if (!password.matches(".*[A-Z].*")) {
            showToast("La contraseña debe contener al menos una letra mayúscula");
            return;
        }

        if (!password.matches(".*[a-z].*")) {
            showToast("La contraseña debe contener al menos una letra minúscula");
            return;
        }

        if (!password.matches(".*\\d.*")) {
            showToast("La contraseña debe contener al menos un número");
            return;
        }

        if (!password.matches(".*[!@#$%^&*()_+=<>?/{}~\\-].*")) {
            showToast("La contraseña debe incluir un carácter especial");
            return;
        }

        if (password.contains(" ")) {
            showToast("La contraseña no debe contener espacios");
            return;
        }


        // ✅ Si todas las validaciones pasan, proceder al registro
        String origenApp = "incidencias";

        Call<String> call = usuarioApi.registrarUsuario(
                nombreCompleto,
                correoInput,
                telefonoInput,
                usuarioInput,
                password,
                origenApp
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String mensaje = response.body();
                    Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_SHORT).show();

                    if ("Usuario registrado correctamente".equals(mensaje)) {
                        limpiarCampos();
                    }
                } else {
                    Toast.makeText(RegistroActivity.this, "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegistroActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método auxiliar para mostrar Toast en el hilo principal
    private void showToast(String mensaje) {
        runOnUiThread(() -> Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show());
    }


    private void limpiarCampos() {
        nombreapellidos.setText("");
        email.setText("");
        telefono.setText("");
        usuario.setText("");
        clave.setText("");
        repetirClave.setText("");
    }


}