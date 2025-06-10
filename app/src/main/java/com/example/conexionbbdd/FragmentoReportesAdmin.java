package com.example.conexionbbdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Este fragmento ahora servirá para mostrar los reportes que necesitan asignación
// y permitir su gestión (incluida la asignación).
public class FragmentoReportesAdmin extends Fragment {

    private RecyclerView recyclerViewReportes;
    private ReporteAdapter reporteAdapter; // Usamos el ReporteAdapter que ya tenías
    private List<ReporteDTO> listaReportes;
    private ProgressBar progressBarLoading;

    public FragmentoReportesAdmin() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout para este fragmento. Si ya tienes un layout para FragmentoReportesAdmin,
        // asegúrate de que contenga un RecyclerView con el ID 'recyclerViewReportes' y un ProgressBar con 'progressBarLoading'.
        // Si no tienes, usa un layout similar a 'fragmento_asignar_reportes.xml' que te proporcioné.
        View view = inflater.inflate(R.layout.fragment_reportes_asignados, container, false); // Reutilizamos este layout

        recyclerViewReportes = view.findViewById(R.id.recyclerViewAssignedReports); // ID del RecyclerView en el layout reutilizado
        progressBarLoading = view.findViewById(R.id.progressBarAssignedReportsLoading); // ID del ProgressBar en el layout reutilizado

        recyclerViewReportes.setLayoutManager(new LinearLayoutManager(getContext()));

        listaReportes = new ArrayList<>();
        reporteAdapter = new ReporteAdapter(getContext(), listaReportes);

        // ¡Importante! Configurar el listener para que se recarguen los reportes
        // después de que se asigne un usuario a uno.
        reporteAdapter.setOnUsuarioAsignadoListener(() -> {
            cargarReportesParaAsignar(); // Recarga la lista de reportes después de una asignación
        });

        recyclerViewReportes.setAdapter(reporteAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Carga los reportes cuando la vista ha sido creada
        cargarReportesParaAsignar();
    }

    // Este método cargará los reportes que están pendientes de asignación
    private void cargarReportesParaAsignar() {
        progressBarLoading.setVisibility(View.VISIBLE);
        recyclerViewReportes.setVisibility(View.GONE);

        ReporteApi reporteApi = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);
        // Llamamos a la API para obtener reportes en estado "Pendiente"
        // Adapta este estado ("Pendiente") a los estados reales en tu backend
        // que representen reportes que necesitan ser asignados.
        Call<List<ReporteDTO>> call = reporteApi.listarReportesDTOPorEstado("Pendiente");

        call.enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReporteDTO>> call, @NonNull Response<List<ReporteDTO>> response) {
                progressBarLoading.setVisibility(View.GONE);
                recyclerViewReportes.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    List<ReporteDTO> reportesObtenidos = response.body();
                    // Opcional: Podrías filtrar aquí si necesitas más condiciones,
                    // pero si la API ya filtra por "Pendiente" y estos son los que se asignan,
                    // entonces la lista ya está lista.
                    if (reportesObtenidos.isEmpty()) {
                        Toast.makeText(getContext(), "No hay reportes pendientes de asignación.", Toast.LENGTH_LONG).show();
                    }
                    reporteAdapter.updateData(reportesObtenidos); // Actualiza la lista en el adaptador
                } else {
                    Toast.makeText(getContext(), "Error al cargar reportes para asignación: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReporteDTO>> call, @NonNull Throwable t) {
                progressBarLoading.setVisibility(View.GONE);
                recyclerViewReportes.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Error de conexión al cargar reportes: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
