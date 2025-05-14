package com.example.conexionbbdd;

import android.os.Bundle;
/*import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroAdminActivity extends AppCompatActivity {

    EditText nombre, correo, telefono, usuario, clave, repetirClave;
    Button registrar;
    AdministradorApi administradorApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_admin); // Asegúrate de crear este layout también

        nombre = findViewById(R.id.txtnombreadmin);
        correo = findViewById(R.id.txtcorreoadmin);
        telefono = findViewById(R.id.txttelefonoadmin);
        usuario = findViewById(R.id.txtusuarioadmin);
        clave = findViewById(R.id.txtclaveadmin);
        repetirClave = findViewById(R.id.txtrepetirclaveadmin);
        registrar = findViewById(R.id.btnregistraradmin);

        administradorApi = RetrofitClient.getRetrofitInstance().create(AdministradorApi.class);

        registrar.setOnClickListener(v -> registrarAdmin());
    }

    private void registrarAdmin() {
        String password = clave.getText().toString();
        String confirmPassword = repetirClave.getText().toString();
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<String> call = administradorApi.registrarAdmin(
                nombre.getText().toString().trim(),
                correo.getText().toString().trim(),
                telefono.getText().toString().trim(),
                usuario.getText().toString().trim(),
                password,
                "incidencias"
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroAdminActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistroAdminActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RegistroAdminActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
*/