package com.example.conexionbbdd;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class FragmentoReportesAdmin extends Fragment {

    private RecyclerView recyclerReportes;
    private ReporteAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner spinnerFiltro, spinnerOrdenFecha;
    private EditText etBuscarTexto;

    private boolean ordenDescendente = true;
    private ReporteApi reporteApi;

    private List<ReporteDTO> listaCompletaReportes = new ArrayList<>();
    private String filtroEstadoSeleccionado = "Todos";

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
        etBuscarTexto = view.findViewById(R.id.et_buscar_texto);

        recyclerReportes.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar spinner filtro estado
        ArrayAdapter<CharSequence> filtroAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.estado_array,
                android.R.layout.simple_spinner_item
        );
        filtroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(filtroAdapter);

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtroEstadoSeleccionado = (String) parent.getItemAtPosition(position);
                cargarReportesFiltrados(filtroEstadoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Configurar spinner orden fecha
        ArrayAdapter<CharSequence> ordenAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.orden_fecha_array,
                android.R.layout.simple_spinner_item
        );
        ordenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdenFecha.setAdapter(ordenAdapter);

        spinnerOrdenFecha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ordenDescendente = (position == 0);
                if (adapter != null) {
                    adapter.ordenarPorFecha(ordenDescendente);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Configurar SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> cargarReportesFiltrados(filtroEstadoSeleccionado));

        // Listener para filtro texto
        etBuscarTexto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltroTexto(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Carga inicial
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
                    listaCompletaReportes = response.body();

                    if (adapter == null) {
                        adapter = new ReporteAdapter(getContext(), listaCompletaReportes, true);
                        recyclerReportes.setAdapter(adapter);
                    } else {
                        adapter.updateData(listaCompletaReportes);
                    }

                    // Aplico orden
                    adapter.ordenarPorFecha(ordenDescendente);

                    // Aplico filtro texto (por si hay texto ya escrito)
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
            // Sin filtro texto, muestra todos los reportes cargados
            adapter.updateData(listaCompletaReportes);
        } else {
            List<ReporteDTO> filtrados = new ArrayList<>();
            String textoLower = texto.toLowerCase();

            for (ReporteDTO reporte : listaCompletaReportes) {
                // Filtra por título y descripción (modifica si quieres otros campos)
                if ((reporte.getAsunto() != null && reporte.getAsunto().toLowerCase().contains(textoLower)) ||
                        (reporte.getDescripcion() != null && reporte.getDescripcion().toLowerCase().contains(textoLower))) {
                    filtrados.add(reporte);
                }
            }
            adapter.updateData(filtrados);
        }

        adapter.ordenarPorFecha(ordenDescendente);
    }
}
