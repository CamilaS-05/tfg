package com.example.conexionbbdd;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoVerReportes extends Fragment {

    private RecyclerView recyclerReportes;
    private ReporteSimpleAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText etBuscarTexto;

    private ReporteApi reporteApi;

    private List<ReporteDTO> listaCompletaReportes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reporteApi = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);
        return inflater.inflate(R.layout.fragmento_ver_reportes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerReportes = view.findViewById(R.id.recycler_reportes);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        etBuscarTexto = view.findViewById(R.id.et_buscar_texto);

        recyclerReportes.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(() -> cargarReportesSinAsignar());

        etBuscarTexto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltroTexto(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Carga inicial
        swipeRefreshLayout.setRefreshing(true);
        cargarReportesSinAsignar();
    }

    private void cargarReportesSinAsignar() {
        Call<List<ReporteDTO>> call = reporteApi.listarReportesDTO();
        // O usa el endpoint que filtre reportes sin usuario asignado (depende de tu API)

        call.enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(Call<List<ReporteDTO>> call, Response<List<ReporteDTO>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    // Filtrar solo los reportes con estado "Pendiente"
                    List<ReporteDTO> pendientes = new ArrayList<>();
                    for (ReporteDTO r : response.body()) {
                        if ("Pendiente".equalsIgnoreCase(r.getEstado())) {
                            pendientes.add(r);
                        }
                    }

                    listaCompletaReportes = pendientes;

                    if (adapter == null) {
                        adapter = new ReporteSimpleAdapter(getContext(), listaCompletaReportes);
                        recyclerReportes.setAdapter(adapter);
                    } else {
                        adapter.updateData(listaCompletaReportes);
                    }

                    aplicarFiltroTexto(etBuscarTexto.getText().toString());
                } else {
                    Toast.makeText(getContext(), "No se pudo cargar la lista", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReporteDTO>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Error al conectar: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void aplicarFiltroTexto(String texto) {
        if (adapter == null) return;

        if (texto.isEmpty()) {
            adapter.updateData(listaCompletaReportes);
        } else {
            List<ReporteDTO> filtrados = new ArrayList<>();
            String textoLower = texto.toLowerCase();

            for (ReporteDTO reporte : listaCompletaReportes) {
                if ((reporte.getAsunto() != null && reporte.getAsunto().toLowerCase().contains(textoLower)) ||
                        (reporte.getDescripcion() != null && reporte.getDescripcion().toLowerCase().contains(textoLower))) {
                    filtrados.add(reporte);
                }
            }
            adapter.updateData(filtrados);
        }
    }
}
