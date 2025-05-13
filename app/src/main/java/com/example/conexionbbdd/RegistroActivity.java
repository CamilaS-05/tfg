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
        // Validar correo
        String correoInput = email.getText().toString().trim();
        if (!correoInput.contains("@")) {
            Toast.makeText(this, "El correo debe contener @", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar contraseñas
        String password = clave.getText().toString();
        String confirmPassword = repetirClave.getText().toString();
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        String origenApp = "incidencias";  // <- aquí defines el origen

        Call<String> call = usuarioApi.registrarUsuario(
                nombreapellidos.getText().toString().trim(),
                correoInput,
                telefono.getText().toString().trim(),
                usuario.getText().toString().trim(),
                password,
                "incidencias"
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


    private void limpiarCampos() {
        nombreapellidos.setText("");
        email.setText("");
        telefono.setText("");
        usuario.setText("");
        clave.setText("");
        repetirClave.setText("");
    }


}