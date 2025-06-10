package com.example.conexionbbdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

public class PantallaPrincipalAdmin extends Fragment {

    private ImageView iconoNotificaciones;
    private TextView txtSaludoAdmin, txtTotalUsuarios, txtReportesPendientes, txtNotificaciones, txtPanelSubtitle;
    private LinearLayout navHomeAdmin, navUsersAdmin, navReportsAdmin, navSettingsAdmin;
    private MaterialCardView btnGestionUsuarios, btnVerReportes;

    public PantallaPrincipalAdmin() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pantalla_principal_admin, container, false);

        // Inicializar vistas
        iconoNotificaciones = view.findViewById(R.id.iconoNotificacionesAdmin);
        txtSaludoAdmin = view.findViewById(R.id.txtSaludoAdmin);
        txtPanelSubtitle = view.findViewById(R.id.txtPanelSubtitle);

        btnGestionUsuarios = view.findViewById(R.id.btnGestionUsuarios);
        btnVerReportes = view.findViewById(R.id.btnVerReportes);

        navHomeAdmin = view.findViewById(R.id.navHomeAdmin);
        navUsersAdmin = view.findViewById(R.id.navUsersAdmin);
        navReportsAdmin = view.findViewById(R.id.navReportsAdmin);
        navSettingsAdmin = view.findViewById(R.id.navSettingsAdmin);

        // Configurar saludo personalizado
        SharedPreferences prefs = requireActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String nombreAdmin = prefs.getString("nombre_admin", "Administrador");
        if (nombreAdmin != null && !nombreAdmin.isEmpty() && !nombreAdmin.equals("Administrador")) {
            txtSaludoAdmin.setText("¡Hola, " + nombreAdmin + "!");
        } else {
            txtSaludoAdmin.setText("¡Hola, Administrador!");
        }
        txtPanelSubtitle.setText("Panel de Administración");



        // Listeners
        iconoNotificaciones.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoNotificacionesAdmin())
                    .addToBackStack(null)
                    .commit();
        });

        // ¡ATENCIÓN! Ahora "Gestionar Usuarios" va a FragmentoReportesAdmin (modificado para asignación)
        btnGestionUsuarios.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoReportesAdmin()) // Apunta a FragmentoReportesAdmin
                    .addToBackStack(null)
                    .commit();
        });

        // "Ver Reportes" también va a FragmentoReportesAdmin, lo que significa
        // que ambos botones ahora mostrarán la lista de reportes para asignación.
        btnVerReportes.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoReportesAdmin()) // Apunta a FragmentoReportesAdmin
                    .addToBackStack(null)
                    .commit();
        });

        // Navegación inferior
        navHomeAdmin.setOnClickListener(v -> {
            // Ya estamos en home
        });

        // Este botón de la barra inferior sigue llevando a FragmentoUsuariosAdmin para ver usuarios
        navUsersAdmin.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoUsuariosAdmin())
                    .addToBackStack(null)
                    .commit();
        });

        // Este botón de la barra inferior también apunta a FragmentoReportesAdmin
        navReportsAdmin.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoReportesAdmin())
                    .addToBackStack(null)
                    .commit();
        });

        navSettingsAdmin.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoConfigAdmin())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

}
