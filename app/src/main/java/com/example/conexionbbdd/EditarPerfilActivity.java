package com.example.conexionbbdd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText etNombreCompleto, etCorreo, etTelefono, etUsuario, etContrasena, etConfirmarContrasena;
    private Button btnGuardar;

    private Long idUsuario;
    private UsuarioApi api;

    // Variables para guardar datos originales del usuario
    private String nombreOriginal, correoOriginal, telefonoOriginal, usuarioOriginal;

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

                    // Guardar datos originales
                    nombreOriginal = usuario.getNombreCompleto();
                    correoOriginal = usuario.getCorreo();
                    telefonoOriginal = usuario.getTelefono();
                    usuarioOriginal = usuario.getUsuario();

                    // Mostrar como hints o textos en campos
                    etNombreCompleto.setHint(nombreOriginal);
                    etCorreo.setHint(correoOriginal);
                    etTelefono.setHint(telefonoOriginal);
                    etUsuario.setHint(usuarioOriginal);

                    // Opcional: también puedes ponerlos como texto, para que sea editable directamente:
                    // etNombreCompleto.setText(nombreOriginal);
                    // etCorreo.setText(correoOriginal);
                    // etTelefono.setText(telefonoOriginal);
                    // etUsuario.setText(usuarioOriginal);

                    etContrasena.setText("");
                    etConfirmarContrasena.setText("");
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

        // Revisa que al menos tengamos datos originales o editados para estos campos obligatorios
        if (TextUtils.isEmpty(nombre) && (nombreOriginal == null || nombreOriginal.isEmpty())) {
            Toast.makeText(this, "Nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(correo) && (correoOriginal == null || correoOriginal.isEmpty())) {
            Toast.makeText(this, "Correo es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(usuario) && (usuarioOriginal == null || usuarioOriginal.isEmpty())) {
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
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String usuario = etUsuario.getText().toString().trim();
        String contrasena = etContrasena.getText().toString();

        String nombreActualizar = nombre.isEmpty() || nombre.equals(nombreOriginal) ? null : nombre;
        String correoActualizar = correo.isEmpty() || correo.equals(correoOriginal) ? null : correo;
        String telefonoActualizar = telefono.isEmpty() || telefono.equals(telefonoOriginal) ? null : telefono;
        String usuarioActualizar = usuario.isEmpty() || usuario.equals(usuarioOriginal) ? null : usuario;
        String contrasenaActualizar = contrasena.isEmpty() ? null : contrasena;

        UsuarioEditar usuarioEditar = new UsuarioEditar(
                idUsuario,
                nombreActualizar != null ? nombreActualizar : nombreOriginal,
                correoActualizar != null ? correoActualizar : correoOriginal,
                telefonoActualizar != null ? telefonoActualizar : telefonoOriginal,
                usuarioActualizar != null ? usuarioActualizar : usuarioOriginal,
                contrasenaActualizar, // solo la mandas si fue modificada
                "incidencias"
        );


        Call<ResponseBody> call = api.actualizarUsuario(idUsuario, usuarioEditar);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditarPerfilActivity.this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditarPerfilActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditarPerfilActivity.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
