package com.example.conexionbbdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentoReportesAdmin extends Fragment {

    private RecyclerView recyclerReportes;
    private ReporteAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinnerFiltro, spinnerOrdenFecha;
    private boolean ordenDescendente = true;

    private ReporteApi reporteApi; // Asumo que tienes tu servicio API configurado

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reporteApi = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);
        return inflater.inflate(R.layout.fragment_reportes_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerReportes = view.findViewById(R.id.recycler_reportes);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        spinnerFiltro = view.findViewById(R.id.spinner_filtro_estado);
        spinnerOrdenFecha = view.findViewById(R.id.spinner_orden_fecha);

        recyclerReportes.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar ApiService (retrofit)
        ReporteApi apiService = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);


        // Configurar spinner filtro estado
        ArrayAdapter<CharSequence> filtroAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.estado_array,  // por ejemplo: ["Todos", "Pendiente", "En progreso", "Completado"]
                android.R.layout.simple_spinner_item
        );
        filtroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(filtroAdapter);

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String estadoSeleccionado = (String) parent.getItemAtPosition(position);
                cargarReportesFiltrados(estadoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Configurar spinner orden fecha
        ArrayAdapter<CharSequence> ordenAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.orden_fecha_array,  // ["Más recientes", "Más antiguos"]
                android.R.layout.simple_spinner_item
        );
        ordenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdenFecha.setAdapter(ordenAdapter);

        spinnerOrdenFecha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ordenDescendente = (position == 0); // Más recientes = true
                if (adapter != null) {
                    adapter.ordenarPorFecha(ordenDescendente);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Configurar SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            String estadoSeleccionado = (String) spinnerFiltro.getSelectedItem();
            if (estadoSeleccionado == null) estadoSeleccionado = "Todos";
            cargarReportesFiltrados(estadoSeleccionado);
        });

        // Carga inicial con "Todos" y orden descendente
        spinnerFiltro.setSelection(0); // "Todos"
        spinnerOrdenFecha.setSelection(0); // "Más recientes"
    }

    private void cargarReportesFiltrados(String estado) {
        swipeRefreshLayout.setRefreshing(true);

        Call<List<ReporteDTO>> call;
        if (estado.equals("Todos")) {
            call = reporteApi.listarReportesDTO();
        } else {
            call = reporteApi.listarReportesDTOPorEstado(estado.toLowerCase());
        }

        call.enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(Call<List<ReporteDTO>> call, Response<List<ReporteDTO>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<ReporteDTO> reportes = response.body();

                    if (adapter == null) {
                        adapter = new ReporteAdapter(getContext(), reportes);
                        recyclerReportes.setAdapter(adapter);
                    } else {
                        adapter.updateData(reportes);
                    }

                    // Aplicar orden seleccionado
                    adapter.ordenarPorFecha(ordenDescendente);
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
}
