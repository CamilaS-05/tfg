package com.example.conexionbbdd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);  // Asegúrate de que este layout existe

        // Referencias
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view_admin);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Toggle para abrir/cerrar el drawer desde el toolbar
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Obtener el nombre del administrador de SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        String nombreAdmin = prefs.getString("nombre_admin", "Administrador");

        // Configurar el nombre en el encabezado del NavigationView
        View headerView = navigationView.getHeaderView(0); // Obtén la vista del header
        TextView nombreAdminTextView = headerView.findViewById(R.id.txtNombreAdmin); // Asegúrate que el ID coincide con el del XML

        // Establecer el nombre del administrador en el TextView
        nombreAdminTextView.setText("Bienvenido, " + nombreAdmin);

        // Fragment por defecto
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new PantallaPrincipalAdmin())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home_admin);
        }

        // Navegación de opciones del menú
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new PantallaPrincipalAdmin())
                            .commit();
                }else if (id == R.id.nav_profile_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new FragmentoPerfilAdmin())
                            .commit();
                } else if (id == R.id.nav_usuarios_admin) {
                    Toast.makeText(MainActivityAdmin.this, "Gestión de usuarios", Toast.LENGTH_SHORT).show();
                    // Aquí puedes lanzar otro fragment o activity si quieres
                } else if (id == R.id.nav_settings_admin) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new FragmentoConfigAdmin())
                            .commit();
                } else if (id == R.id.nav_logout_admin) {
                    Toast.makeText(MainActivityAdmin.this, "Cerrando sesión...", Toast.LENGTH_SHORT).show();

                    // Eliminar el nombre del administrador de SharedPreferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("nombre_admin");
                    editor.apply();

                    Intent intent = new Intent(MainActivityAdmin.this, InicioAdmin.class);
                    startActivity(intent);
                    finish();
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }
}