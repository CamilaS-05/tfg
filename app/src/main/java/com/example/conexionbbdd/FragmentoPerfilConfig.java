package com.example.conexionbbdd;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.conexionbbdd.Conexion;
import com.example.conexionbbdd.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class FragmentoPerfilConfig extends Fragment {

    private EditText editTextNombre, editTextCorreo, editTextTelefono;
    private EditText editTextNuevaContrasena, editTextConfirmarContrasena;
    private Button btnGuardarCambios;

    private Connection con;
    private String usuarioAdminOriginal;

    public FragmentoPerfilConfig() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_perfil_config, container, false);

        editTextNombre = view.findViewById(R.id.editTextNombreUsuario);
        editTextCorreo = view.findViewById(R.id.editTextCorreoUsuario);
        editTextTelefono = view.findViewById(R.id.editTextTelefonoUsuario);
        editTextNuevaContrasena = view.findViewById(R.id.editTextNuevaContrasenaUsuario);
        editTextConfirmarContrasena = view.findViewById(R.id.editTextConfirmarContrasenaUsuario);
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambiosUsuario);

        String nombreAdmin = getActivity().getIntent().getStringExtra("nombre_Admin");

        if (nombreAdmin != null) {
            usuarioAdminOriginal = nombreAdmin;
            obtenerDatosAdmin();
        }

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        return view;
    }

    private void obtenerDatosAdmin() {
        Conexion instanceConnection = new Conexion();
        con = instanceConnection.connect();

        if (con != null) {
            try {
                String query = "SELECT * FROM Administradores WHERE usuarioAdmin = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, usuarioAdminOriginal);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    editTextNombre.setText(rs.getString("usuarioAdmin"));
                    editTextCorreo.setText(rs.getString("correoAdmin"));
                    editTextTelefono.setText(rs.getString("telefonoAdmin"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarCambios() {
        String nombre = editTextNombre.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();
        String nuevaContrasena = editTextNuevaContrasena.getText().toString().trim();
        String confirmarContrasena = editTextConfirmarContrasena.getText().toString().trim();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(telefono)) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(nuevaContrasena) && !nuevaContrasena.equals(confirmarContrasena)) {
            Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        Conexion instanceConnection = new Conexion();
        con = instanceConnection.connect();

        if (con != null) {
            try {
                // Verifica duplicados
                String checkQuery = "SELECT COUNT(*) FROM Administradores WHERE (correoAdmin = ? OR telefonoAdmin = ? OR usuarioAdmin = ?) AND usuarioAdmin != ?";
                PreparedStatement checkPst = con.prepareStatement(checkQuery);
                checkPst.setString(1, correo);
                checkPst.setString(2, telefono);
                checkPst.setString(3, nombre);
                checkPst.setString(4, usuarioAdminOriginal);
                ResultSet checkRs = checkPst.executeQuery();

                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    Toast.makeText(getActivity(), "Ya existe un administrador con ese nombre, correo o teléfono", Toast.LENGTH_SHORT).show();
                    return;
                }

                String query;
                PreparedStatement pst;

                if (!TextUtils.isEmpty(nuevaContrasena)) {
                    query = "UPDATE Administradores SET usuarioAdmin=?, correoAdmin=?, telefonoAdmin=?, contrasenaAdmin=? WHERE usuarioAdmin=?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, nombre);
                    pst.setString(2, correo);
                    pst.setString(3, telefono);
                    pst.setString(4, nuevaContrasena);
                    pst.setString(5, usuarioAdminOriginal);
                } else {
                    query = "UPDATE Administradores SET usuarioAdmin=?, correoAdmin=?, telefonoAdmin=? WHERE usuarioAdmin=?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, nombre);
                    pst.setString(2, correo);
                    pst.setString(3, telefono);
                    pst.setString(4, usuarioAdminOriginal);
                }

                int result = pst.executeUpdate();

                if (result > 0) {
                    usuarioAdminOriginal = nombre; // actualiza la referencia
                    editTextNuevaContrasena.setText("");
                    editTextConfirmarContrasena.setText("");
                    Toast.makeText(getActivity(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
