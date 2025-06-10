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

public class FragmentoPerfilAdmin extends Fragment {

    EditText etNombre, etTelefono, etCorreo, etNuevaContrasena, etRepetirContrasena;
    Button btnCambiarContrasena;
    Connection con;
    private String contrasenaActual;

    public FragmentoPerfilAdmin() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_perfil_admin, container, false);

        etNombre = view.findViewById(R.id.et_nombre_admin);
        etTelefono = view.findViewById(R.id.et_telefono_admin);
        etCorreo = view.findViewById(R.id.et_correo_admin);
        etNuevaContrasena = view.findViewById(R.id.et_nueva_contrasena_admin);
        etRepetirContrasena = view.findViewById(R.id.et_repetir_contrasena_admin);
        btnCambiarContrasena = view.findViewById(R.id.btn_cambiar_contrasena_admin);

        cargarDatosAdmin();

        btnCambiarContrasena.setOnClickListener(v -> cambiarContrasena());

        return view;
    }

    private void cargarDatosAdmin() {
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", getActivity().MODE_PRIVATE);
            String nombreAdmin = prefs.getString("nombre_admin", null);

            if (nombreAdmin == null || con == null) {
                Toast.makeText(getContext(), "Error cargando datos", Toast.LENGTH_SHORT).show();
                return;
            }

            PreparedStatement pst = con.prepareStatement("SELECT * FROM Administradores WHERE nombreAdmin = ?");
            pst.setString(1, nombreAdmin);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                etNombre.setText(rs.getString("nombreAdmin"));
                etTelefono.setText(rs.getString("telefonoAdmin"));
                etCorreo.setText(rs.getString("correoAdmin"));
                contrasenaActual = rs.getString("contrasenaAdmin"); // Guardar la contraseña actual
            }

        } catch (Exception e) {
            Log.e("CARGA_ADMIN_ERROR", e.getMessage());
        }
    }

    private void cambiarContrasena() {
        String nuevaPass = etNuevaContrasena.getText().toString().trim();
        String repetirPass = etRepetirContrasena.getText().toString().trim();

        if (nuevaPass.isEmpty() || repetirPass.isEmpty()) {
            Toast.makeText(getContext(), "Ambos campos de contraseña son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nuevaPass.equals(repetirPass)) {
            Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nuevaPass.equals(contrasenaActual)) {
            Toast.makeText(getContext(), "La nueva contraseña debe ser diferente a la actual", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", getActivity().MODE_PRIVATE);
            String nombreAdmin = prefs.getString("nombre_admin", null);

            if (nombreAdmin == null || con == null) return;

            PreparedStatement pst = con.prepareStatement("UPDATE Administradores SET contrasenaAdmin = ? WHERE nombreAdmin = ?");
            pst.setString(1, nuevaPass);
            pst.setString(2, nombreAdmin);
            int filas = pst.executeUpdate();

            if (filas > 0) {
                Toast.makeText(getContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                etNuevaContrasena.setText("");
                etRepetirContrasena.setText("");
                contrasenaActual = nuevaPass; // Actualizar la contraseña almacenada
            } else {
                Toast.makeText(getContext(), "No se pudo actualizar", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("CAMBIO_PASS_ERROR", e.getMessage());
        }
    }
}
