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

public class MainActivityAdmin extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private ActionBarDrawerToggle toggle;
    TextView textoBienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_admin);
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

        View headerView = navigationView.getHeaderView(0);
        textoBienvenida = headerView.findViewById(R.id.txtNombreAdmin);
        long idUsuario = prefs.getLong("id_admin", -1);
        String nombreAdmin = prefs.getString("nombre_admin", "Administrador");

        Log.d("MAIN_DEBUG", "ID cargado en MainActivity: " + idUsuario);

        textoBienvenida.setText("Bienvenido, " + nombreAdmin);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new PantallaPrincipalAdmin())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home_admin);
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home_admin) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new PantallaPrincipalAdmin())
                        .commit();
            } else if (id == R.id.nav_profile_admin) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentoPerfilAdmin())
                        .commit();
            } else if (id == R.id.nav_usuarios_admin) {
                Toast.makeText(MainActivityAdmin.this, "Gestión de usuarios", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_notificaciones_admin) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentoNotificacionesAdmin())
                        .commit();

            } else if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new FragmentoUsuariosAdmin()) // Asegúrate de que 'fragment_container' exista en tu activity_main.xml
                            .commit();

            } else if (id == R.id.nav_settings_admin) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentoConfigAdmin())
                        .commit();
            } else if (id == R.id.nav_logout_admin) {
                Toast.makeText(MainActivityAdmin.this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();

                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("nombre_admin");
                editor.apply();

                Intent intent = new Intent(MainActivityAdmin.this, LoginAdminActivity.class);
                startActivity(intent);
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }
}
