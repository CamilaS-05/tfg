package com.example.conexionbbdd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoMisIncidencias extends Fragment {
    private RecyclerView recyclerView;
    private ReporteApi reporteApi;
    private ReporteAsignadoAdapter adapter;
    private List<ReporteDTO> listaOriginal;
    private SearchView searchView;

    private androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_incidencias, container, false);

        recyclerView = view.findViewById(R.id.recyclerMisReportes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        searchView = view.findViewById(R.id.searchView);

        reporteApi = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);

        cargarReportesAsignados();

        swipeRefreshLayout.setOnRefreshListener(this::cargarReportesAsignados);

        // Mostrar siempre el campo de búsqueda desplegado
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarReportes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarReportes(newText);
                return true;
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
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        int idUsuarioActual = (int) idUsuarioLong;

        reporteApi.getReportesAsignados(idUsuarioActual).enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(Call<List<ReporteDTO>> call, Response<List<ReporteDTO>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    listaOriginal = response.body();

                    // Ojo: aquí deberías pasar al adapter las fechas ya formateadas si quieres mostrar formateado,
                    // o modificar el adaptador para que llame a formatoFecha al asignar la fecha a la vista.
                    adapter = new ReporteAsignadoAdapter(listaOriginal, getContext(), reporteApi);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No se encontraron reportes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReporteDTO>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Filtra localmente la lista de reportes por asunto, estado, fecha (formateada) y nombreAsignado.
     */
    public void filtrarReportes(String textoBusqueda) {
        if (adapter == null || listaOriginal == null) {
            return; // No hay datos para filtrar todavía
        }

        String textoLower = textoBusqueda.toLowerCase();
        List<ReporteDTO> listaFiltrada = new ArrayList<>();

        for (ReporteDTO r : listaOriginal) {
            boolean cumpleEstado = r.getEstado() != null && r.getEstado().toLowerCase().contains(textoLower);

            // Aquí usamos formatoFecha para comparar con la fecha formateada
            String fechaFormateada = r.getFecha() != null ? formatoFecha(r.getFecha()) : "";
            boolean cumpleFecha = fechaFormateada.toLowerCase().contains(textoLower);

            boolean cumpleAsunto = r.getAsunto() != null && r.getAsunto().toLowerCase().contains(textoLower);
            boolean cumpleNombreAsignado = r.getNombreAsignado() != null && r.getNombreAsignado().toLowerCase().contains(textoLower);

            if (cumpleEstado || cumpleFecha || cumpleAsunto || cumpleNombreAsignado) {
                listaFiltrada.add(r);
            }
        }

        adapter.actualizarLista(listaFiltrada);
    }

    /**
     * Formatea la fecha ISO 8601 a formato "dd/MM/yyyy HH:mm".
     */
    private String formatoFecha(String fechaISO) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = isoFormat.parse(fechaISO);
            SimpleDateFormat formatoLocal = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return formatoLocal.format(date);
        } catch (Exception e) {
            return fechaISO; // En caso de error, devuelve la fecha original
        }
    }
}
