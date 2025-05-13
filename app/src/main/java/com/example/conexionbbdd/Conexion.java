package com.example.conexionbbdd;

import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Conexion {

    private String ip = "192.168.1.170";  // Dirección IP del servidor SQL
    private String puerto = "1433";       // Puerto de SQL Server
    private String basedatos = "prueba";  // Nombre de la base de datos
    private String usuario = "sa";        // Usuario de SQL Server
    private String password = "260505Ca."; // Contraseña de SQL Server

    public Connection connect() {
        Connection conexion = null;
        String connectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + puerto + ";"
                + "databaseName=" + basedatos + ";"
                + "user=" + usuario + ";"
                + "password=" + password + ";"
                + "encrypt=false;trustServerCertificate=true;";

        try {
            // Habilitar política de hilos (para evitar error en conexiones en el hilo principal)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Cargar el driver JDBC de SQL Server
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            // Establecer conexión
            conexion = DriverManager.getConnection( connectionURL);
            Log.i("ConexionBD", "Conexión a la base de datos establecida correctamente");

        } catch (ClassNotFoundException e) {
            Log.e("Error", "No se encontró el driver JDBC: " + e.getMessage());
        } catch (Exception e) {
            Log.e("Error", "No se pudo conectar a la base de datos: " + e.getMessage());
        }

        return conexion;
    }
    public boolean insertarPersona(String nombre, String apellido) {
        boolean exito = false;
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = connect();
            if (connection != null) {
                String query = "INSERT INTO personas (nombre, apellido) VALUES (?, ?)";
                stmt = connection.prepareStatement(query);
                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    Log.d("SQL", "Persona insertada correctamente");
                    exito = true;
                } else {
                    Log.d("SQL", "No se insertaron filas.");
                }
            } else {
                Log.e("Conexion", "La conexión es nula");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error al insertar", "Error al ejecutar la consulta: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (Exception ex) {
                Log.e("Error al cerrar conexión", "Error al cerrar recursos: " + ex.getMessage());
            }
        }
        return exito;
    }

}
