package com.example.conexionbbdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaPrincipal extends Fragment {

    private EditText etBuscar;
    private ImageView iconoNotificaciones;
    private TextView txtSaludo;
    private RecyclerView recyclerReportes;
    private ReporteAsignadoAdapter adapter;
    private List<ReporteDTO> listaReportes = new ArrayList<>();

    private LinearLayout navIncidencias, navPerfil, navSoporte;

    public PantallaPrincipal() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pantalla_principal, container, false);

        etBuscar = view.findViewById(R.id.etBuscar);
        iconoNotificaciones = view.findViewById(R.id.iconoNotificaciones);
        txtSaludo = view.findViewById(R.id.txtSaludo);
        recyclerReportes = view.findViewById(R.id.recyclerReportes);

        navIncidencias = view.findViewById(R.id.navIncidencias);
        navPerfil = view.findViewById(R.id.navPerfil);
        navSoporte = view.findViewById(R.id.navSoporte);

        // Mostrar saludo
        SharedPreferences prefs = requireActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String nombreUsuario = prefs.getString("nombre_usuario", "Usuario");
        long idUsuario = prefs.getLong("id_usuario", -1);
        txtSaludo.setText("Área de trabajo de " + nombreUsuario);

        // Configurar RecyclerView
        adapter = new ReporteAsignadoAdapter(listaReportes, getContext(), RetrofitClient.getRetrofitInstance().create(ReporteApi.class));
        recyclerReportes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReportes.setAdapter(adapter);

        // Buscar reportes desde API
        obtenerReportesAsignados(idUsuario);

        // Buscar texto en vivo
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence query, int start, int before, int count) {
                adapter.filtrar(query.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Notificaciones
        iconoNotificaciones.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoNotificaciones())
                    .addToBackStack(null)
                    .commit();
        });

        // Navegación inferior
        navIncidencias.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoMisIncidencias())
                    .addToBackStack(null)
                    .commit();
        });

        navPerfil.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoPerfil())
                    .addToBackStack(null)
                    .commit();
        });

        navSoporte.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FragmentoConfig())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void obtenerReportesAsignados(long idUsuario) {
        ReporteApi apiService = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);
        Call<List<ReporteDTO>> call = apiService.getReportesAsignados(idUsuario);
        call.enqueue(new Callback<List<ReporteDTO>>() {
            @Override
            public void onResponse(Call<List<ReporteDTO>> call, Response<List<ReporteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReporteDTO> filtrados = new ArrayList<>();

                    for (ReporteDTO reporte : response.body()) {
                        String estado = reporte.getEstado();

                        // Depuración: mostrar el estado recibido
                        Log.d("ESTADO_REPORTE", "Estado: [" + estado + "]");

                        if (estado != null) {
                            String estadoNormalizado = estado.trim().toLowerCase();
                            if (estadoNormalizado.equals("pendiente") || estadoNormalizado.equals("en proceso")) {
                                filtrados.add(reporte);
                            }
                        }
                    }

                    // Depuración: cuántos reportes cumplen el filtro
                    Log.d("API_DEBUG", "Total reportes filtrados: " + filtrados.size());

                    listaReportes.clear();
                    listaReportes.addAll(filtrados);  // filtrados ya solo tiene pendientes y en proceso
                    adapter.setListaCompleta(listaReportes);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("API_DEBUG", "Respuesta vacía o no exitosa");
                }
            }

            @Override
            public void onFailure(Call<List<ReporteDTO>> call, Throwable t) {
                Log.e("API_DEBUG", "Error al obtener reportes asignados", t);
            }
        });
    }

}
