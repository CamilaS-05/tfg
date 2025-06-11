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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaPrincipalAdmin extends Fragment {

    private ImageView iconoNotificaciones;
    private TextView txtSaludoAdmin, txtPanelSubtitle;
    private LinearLayout navUsersAdmin, navPerfilAdmin, navSettingsAdmin;
    private MaterialCardView btnGestionUsuarios, btnVerReportes;
    private RecyclerView recyclerUltimosReportes;
    private UltimosReportesAdapter reporteAdapter;
    private List<ReporteDTO> listaReportes = new ArrayList<>();
    private ReporteApi reporteApi;


    public PantallaPrincipalAdmin() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pantalla_principal_admin, container, false);
        // Inicializar RecyclerView
        recyclerUltimosReportes = view.findViewById(R.id.recyclerUltimosReportes);
        recyclerUltimosReportes.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        reporteAdapter = new UltimosReportesAdapter(listaReportes, requireContext());// null si no hay listener
        recyclerUltimosReportes.setAdapter(reporteAdapter);

// Inicializar Retrofit
        reporteApi = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);

// Cargar últimos reportes
        cargarUltimosReportes();


        // Inicializar vistas
        iconoNotificaciones = view.findViewById(R.id.iconoNotificacionesAdmin);
        txtSaludoAdmin = view.findViewById(R.id.txtSaludoAdmin);
        txtPanelSubtitle = view.findViewById(R.id.txtPanelSubtitle);

        btnGestionUsuarios = view.findViewById(R.id.btnGestionUsuarios);
        btnVerReportes = view.findViewById(R.id.btnVerReportes);

        navUsersAdmin = view.findViewById(R.id.navUsersAdmin);
        navPerfilAdmin = view.findViewById(R.id.navPerfilAdmin);
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

        btnVerReportes.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoVerReportes()) // Fragmento para reportes sin asignar
                    .addToBackStack(null)
                    .commit();
        });

        // Este botón de la barra inferior sigue llevando a FragmentoUsuariosAdmin para ver usuarios
        navUsersAdmin.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoUsuariosAdmin())
                    .addToBackStack(null)
                    .commit();
        });

        // Este botón de la barra inferior también apunta a FragmentoReportesAdmin
        navPerfilAdmin.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoPerfilAdmin())
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
    private void cargarUltimosReportes() {
        Call<List<ReporteDTO>> call = reporteApi.getUltimosReportes();
        call.enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(Call<List<ReporteDTO>> call, Response<List<ReporteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaReportes.clear();
                    listaReportes.addAll(response.body());
                    reporteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ReporteDTO>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error al cargar reportes", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
