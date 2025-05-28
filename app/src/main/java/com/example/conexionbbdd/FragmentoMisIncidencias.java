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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoMisIncidencias extends Fragment {

    private RecyclerView recyclerView;
    private ReporteApi reporteApi;

    public FragmentoMisIncidencias() {
        // Constructor vacío obligatorio
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_incidencias, container, false);

        recyclerView = view.findViewById(R.id.recyclerMisReportes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        reporteApi = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);

        cargarReportesAsignados();

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
            return;
        }

        int idUsuarioActual = (int) idUsuarioLong;

        reporteApi.getReportesAsignados(idUsuarioActual).enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(Call<List<ReporteDTO>> call, Response<List<ReporteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReporteDTO> lista = response.body();
                    recyclerView.setAdapter(new ReporteAsignadoAdapter(lista, getContext(), reporteApi));
                } else {
                    Toast.makeText(getContext(), "No se encontraron reportes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReporteDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
