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

public class FragmentoPerfil extends Fragment {

    EditText etNombre, etTelefono, etCorreo, etNuevaContrasena, etRepetirContrasena;
    Button btnCambiarContrasena;
    Connection con;
    private String contrasenaActual;

    public FragmentoPerfil() {
        Conexion c = new Conexion();
        con = c.connect();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_perfil, container, false);

        etNombre = view.findViewById(R.id.et_nombre_usuario);
        etTelefono = view.findViewById(R.id.et_telefono_usuario);
        etCorreo = view.findViewById(R.id.et_correo_usuario);
        etNuevaContrasena = view.findViewById(R.id.et_nueva_contrasena_usuario);
        etRepetirContrasena = view.findViewById(R.id.et_repetir_contrasena_usuario);
        btnCambiarContrasena = view.findViewById(R.id.btn_cambiar_contrasena_usuario);

        cargarDatosUsuario();

        btnCambiarContrasena.setOnClickListener(v -> cambiarContrasena());

        return view;
    }

    private void cargarDatosUsuario() {
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", getActivity().MODE_PRIVATE);
            String usuario = prefs.getString("nombre_usuario", null);

            if (usuario == null || con == null) {
                Toast.makeText(getContext(), "Error cargando datos", Toast.LENGTH_SHORT).show();
                return;
            }

            PreparedStatement pst = con.prepareStatement("SELECT * FROM Usuarios WHERE usuario = ?");
            pst.setString(1, usuario);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                etNombre.setText(rs.getString("nombrecompleto"));
                etTelefono.setText(rs.getString("telefono"));
                etCorreo.setText(rs.getString("correo"));
                contrasenaActual = rs.getString("contrasena"); // Guardar contraseña actual
            }

        } catch (Exception e) {
            Log.e("CARGA_USUARIO_ERROR", e.getMessage());
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
            String usuario = prefs.getString("nombre_usuario", null);

            if (usuario == null || con == null) return;

            PreparedStatement pst = con.prepareStatement("UPDATE Usuarios SET contrasena = ? WHERE usuario = ?");
            pst.setString(1, nuevaPass);
            pst.setString(2, usuario);
            int filas = pst.executeUpdate();

            if (filas > 0) {
                Toast.makeText(getContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                etNuevaContrasena.setText("");
                etRepetirContrasena.setText("");
                contrasenaActual = nuevaPass;
            } else {
                Toast.makeText(getContext(), "No se pudo actualizar", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("CAMBIO_PASS_ERROR", e.getMessage());
        }
    }
}
