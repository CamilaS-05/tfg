package com.example.conexionbbdd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class FragmentoMisIncidencias extends Fragment {
    private RecyclerView recyclerView;
    private ReporteApi reporteApi;
    private ReporteAsignadoAdapter adapter;
    private List<ReporteDTO> listaOriginal;
    ImageButton btnVolver;
    private androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_incidencias, container, false);

        recyclerView = view.findViewById(R.id.recyclerMisReportes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnVolver = view.findViewById(R.id.btn_volver);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        reporteApi = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);

        cargarReportesAsignados();

        // Configura el listener para refrescar
        swipeRefreshLayout.setOnRefreshListener(() -> {
            cargarReportesAsignados();
        });
        btnVolver.setOnClickListener(v -> {
            // Acción para volver al fragmento anterior o pantalla principal
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new PantallaPrincipal())
                        .commit();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarReportesAsignados();
    }

    private void cargarReportesAsignados() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("MisPreferencias", getContext().MODE_PRIVATE);
        long idUsuarioLong = prefs.getLong("id_usuario", -1);

        if (idUsuarioLong == -1) {
            Toast.makeText(getContext(), "Error: usuario no identificado", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false); // Parar el indicador si estaba activo
            return;
        }

        int idUsuarioActual = (int) idUsuarioLong;

        reporteApi.getReportesAsignados(idUsuarioActual).enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(Call<List<ReporteDTO>> call, Response<List<ReporteDTO>> response) {
                swipeRefreshLayout.setRefreshing(false); // Parar animación refresco
                if (response.isSuccessful() && response.body() != null) {
                    listaOriginal = response.body();
                    adapter = new ReporteAsignadoAdapter(listaOriginal, getContext(), reporteApi);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No se encontraron reportes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReporteDTO>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false); // Parar animación refresco
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Filtra localmente la lista de reportes por asunto, nombreAsignado y fecha.
     * Puede llamarse desde la actividad o fragmento padre que tenga el input de búsqueda.
     */
    public void filtrarReportes(String asunto, String nombreAsignado, String fecha) {
        if (adapter == null || listaOriginal == null) {
            return; // No hay datos para filtrar todavía
        }

        List<ReporteDTO> listaFiltrada = new ArrayList<>();

        for (ReporteDTO r : listaOriginal) {
            boolean cumpleAsunto = asunto == null || asunto.isEmpty() ||
                    r.getAsunto().toLowerCase().contains(asunto.toLowerCase());

            boolean cumpleNombre = nombreAsignado == null || nombreAsignado.isEmpty() ||
                    r.getNombreAsignado().toLowerCase().contains(nombreAsignado.toLowerCase());

            boolean cumpleFecha = fecha == null || fecha.isEmpty() ||
                    r.getFecha().toLowerCase().contains(fecha.toLowerCase());

            if (cumpleAsunto && cumpleNombre && cumpleFecha) {
                listaFiltrada.add(r);
            }
        }

        adapter.actualizarLista(listaFiltrada);
    }
}
