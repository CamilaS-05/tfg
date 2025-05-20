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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoReportesAdmin extends Fragment {

    private RecyclerView recyclerReportes;
    private Spinner spinnerFiltro;

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

        recyclerReportes.setLayoutManager(new LinearLayoutManager(getContext()));

        // Adapter del spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.estados_filtro_array, // Este array debes crearlo en strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiltro.setAdapter(adapter);

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String estadoSeleccionado = (String) parent.getItemAtPosition(position);
                cargarReportesFiltrados(estadoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        cargarReportesFiltrados("Todos");
    }

    private void cargarReportesFiltrados(String estado) {
        ReporteApi api = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);

        Call<List<Reporte>> call;
        if (estado.equals("Todos")) {
            call = api.obtenerReportes();
        } else {
            call = api.obtenerReportesPorEstado(estado.toLowerCase()); // asegúrate que tu endpoint lo soporte
        }

        call.enqueue(new Callback<List<Reporte>>() {
            @Override
            public void onResponse(Call<List<Reporte>> call, Response<List<Reporte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerReportes.setAdapter(new ReporteAdapter(getContext(), response.body()));
                } else {
                    Toast.makeText(getContext(), "Error al cargar reportes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reporte>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
