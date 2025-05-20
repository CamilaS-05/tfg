package com.example.conexionbbdd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private TextView textoBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);


        // Configurar el nombre en el encabezado del NavigationView
        View headerView = navigationView.getHeaderView(0);
        textoBienvenida = headerView.findViewById(R.id.txtNombreUsuario);
        long idUsuario = prefs.getLong("id_usuario", -1);
        String nombreUsuario = prefs.getString("nombre_usuario", "Usuario");

        Log.d("MAIN_DEBUG", "ID cargado en MainActivity: " + idUsuario);

        textoBienvenida.setText("Bienvenido, " + nombreUsuario);

        // Fragmento por defecto al iniciar
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new PantallaPrincipal())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new PantallaPrincipal())
                            .commit();
                }else if (id == R.id.nav_profile) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new FragmentoPerfil())
                            .commit();
                } else if (id == R.id.nav_settings) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new FragmentoConfig())
                            .commit();
                } else if (id == R.id.nav_logout) {
                    // Limpiar SharedPreferences para cerrar sesión
                    SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();  // Elimina todas las preferencias
                    editor.apply();

                    // Mostrar mensaje de cierre de sesión
                    Toast.makeText(MainActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();

                    // Redirigir al usuario al login (InicioUsuarios)
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);

                    // Opcionalmente, puedes terminar esta actividad si no deseas que se pueda regresar a ella con el botón de retroceso
                    finish();  // Esto es opcional, ya que al hacer startActivity() ya cambiarás a otra actividad
                }

                drawerLayout.closeDrawers();
                return true;
            }

        });
    }
}
