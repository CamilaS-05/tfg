package com.example.conexionbbdd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText etNombreCompleto, etCorreo, etTelefono, etUsuario, etContrasena, etConfirmarContrasena;
    private Button btnGuardar;

    private Long idUsuario;
    private UsuarioApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        etNombreCompleto = findViewById(R.id.etNombreCompleto);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo = findViewById(R.id.etCorreo);
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        btnGuardar = findViewById(R.id.btnGuardar);

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        idUsuario = prefs.getLong("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Usuario no identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        api = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);

        // Llamamos a backend para obtener datos actuales y cargar como hint
        cargarDatosUsuarioDesdeApi();

        btnGuardar.setOnClickListener(v -> {
            if (validarCampos()) {
                actualizarUsuario();
            }
        });
    }

    private void cargarDatosUsuarioDesdeApi() {
        Call<Usuario> call = api.getUsuarioPorId(idUsuario);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario usuario = response.body();

                    // Setear hints con datos actuales
                    etNombreCompleto.setHint(usuario.getNombreCompleto());
                    etCorreo.setHint(usuario.getCorreo());
                    etTelefono.setHint(usuario.getTelefono());
                    etUsuario.setHint(usuario.getUsuario());

                    // Contraseñas siempre vacías y hint genérico
                    etContrasena.setHint("Contraseña");
                    etConfirmarContrasena.setHint("Confirmar contraseña");
                } else {
                    Toast.makeText(EditarPerfilActivity.this, "Error al cargar datos del usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validarCampos() {
        String nombre = etNombreCompleto.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String usuario = etUsuario.getText().toString().trim();
        String contrasena = etContrasena.getText().toString();
        String confirmar = etConfirmarContrasena.getText().toString();

        if (TextUtils.isEmpty(nombre) && etNombreCompleto.getHint() == null) {
            Toast.makeText(this, "Nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(correo) && etCorreo.getHint() == null) {
            Toast.makeText(this, "Correo es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(usuario) && etUsuario.getHint() == null) {
            Toast.makeText(this, "Usuario es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!TextUtils.isEmpty(contrasena) || !TextUtils.isEmpty(confirmar)) {
            if (!contrasena.equals(confirmar)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void actualizarUsuario() {
        String nombre = etNombreCompleto.getText().toString().trim();
        if (nombre.isEmpty()) nombre = etNombreCompleto.getHint().toString();

        String correo = etCorreo.getText().toString().trim();
        if (correo.isEmpty()) correo = etCorreo.getHint().toString();

        String telefono = etTelefono.getText().toString().trim();
        if (telefono.isEmpty()) telefono = etTelefono.getHint() != null ? etTelefono.getHint().toString() : "";

        String usuario = etUsuario.getText().toString().trim();
        if (usuario.isEmpty()) usuario = etUsuario.getHint().toString();

        String contrasena = etContrasena.getText().toString();

        UsuarioEditar usuarioEditar = new UsuarioEditar(
                idUsuario,
                nombre,
                correo,
                telefono,
                usuario,
                contrasena.isEmpty() ? null : contrasena,
                "incidencias"
        );

        Call<String> call = api.actualizarUsuario(usuarioEditar);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarPerfilActivity.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditarPerfilActivity.this, "Error al actualizar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
