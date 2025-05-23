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

public class FragmentoReportesAdmin extends Fragment {

    private RecyclerView recyclerReportes;
    private Spinner spinnerFiltro;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ReporteAdapter adapter;

    public FragmentoReportesAdmin() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reportes_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerReportes = view.findViewById(R.id.recycler_reportes);
        spinnerFiltro = view.findViewById(R.id.spinner_filtro_estado);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        recyclerReportes.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.estados_filtro_array,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(spinnerAdapter);

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String estadoSeleccionado = (String) parent.getItemAtPosition(position);
                cargarReportesFiltrados(estadoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            String estadoSeleccionado = (String) spinnerFiltro.getSelectedItem();
            if (estadoSeleccionado == null) estadoSeleccionado = "Todos";
            cargarReportesFiltrados(estadoSeleccionado);
        });

        cargarReportesFiltrados("Todos");
    }

    private void cargarReportesFiltrados(String estado) {
        if (estado == null) estado = "Todos";

        ReporteApi api = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);
        Call<List<ReporteDTO>> call;

        if (estado.equals("Todos")) {
            call = api.listarReportesDTO();
        } else {
            call = api.listarReportesDTOPorEstado(estado.toLowerCase());
        }

        call.enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(Call<List<ReporteDTO>> call, Response<List<ReporteDTO>> response) {
                swipeRefreshLayout.setRefreshing(false); // ✅ Parar animación

                if (response.isSuccessful() && response.body() != null) {
                    if (adapter == null) {
                        adapter = new ReporteAdapter(getContext(), response.body());
                        recyclerReportes.setAdapter(adapter);

                        adapter.setOnUsuarioAsignadoListener(() -> {
                            String filtroActual = (String) spinnerFiltro.getSelectedItem();
                            if (filtroActual == null) filtroActual = "Todos";
                            cargarReportesFiltrados(filtroActual);
                        });

                    } else {
                        adapter.updateData(response.body());
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar reportes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReporteDTO>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false); // ✅ Parar animación
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
