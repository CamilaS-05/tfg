package com.example.conexionbbdd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoPerfilAdmin extends Fragment {

    EditText etNombre, etTelefono, etCorreo;


    public FragmentoPerfilAdmin() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_perfil_admin, container, false);

        etNombre = view.findViewById(R.id.et_nombre_admin);
        etTelefono = view.findViewById(R.id.et_telefono_admin);
        etCorreo = view.findViewById(R.id.et_correo_admin);

        cargarDatosAdmin();



        return view;
    }

    private void cargarDatosAdmin() {
        SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", getActivity().MODE_PRIVATE);
        String usuarioAdmin = prefs.getString("nombre_admin", null);

        Log.d("PerfilAdmin", "Usuario desde SharedPreferences: " + usuarioAdmin);

        if (usuarioAdmin == null) {
            Toast.makeText(getContext(), "Usuario no encontrado en preferencias", Toast.LENGTH_SHORT).show();
            return;
        }

        AdministradorApi service = RetrofitClient.getRetrofitInstance().create(AdministradorApi.class);
        Call<Administrador> call = service.getAdministradorByUsuario(usuarioAdmin);

        call.enqueue(new Callback<Administrador>() {
            @Override
            public void onResponse(Call<Administrador> call, Response<Administrador> response) {
                Log.d("PerfilAdmin", "Código de respuesta: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Administrador admin = response.body();
                    Log.d("PerfilAdmin", "Nombre: " + admin.getNombrecompletoAdmin());

                    etNombre.setText(admin.getNombrecompletoAdmin());
                    etTelefono.setText(admin.getTelefonoAdmin());
                    etCorreo.setText(admin.getCorreoAdmin());
                } else {
                    Toast.makeText(getContext(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
                    Log.e("PerfilAdmin", "Respuesta fallida o vacía");
                }
            }

            @Override
            public void onFailure(Call<Administrador> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("PerfilAdmin", "Error de conexión: " + t.getMessage());
            }
        });
    }
}
