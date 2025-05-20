package com.example.conexionbbdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoNotificacionesAdmin extends Fragment {
    private RecyclerView recyclerView;
    private NotificacionAdapter adapter;
    private List<Notificacion> listaNotificaciones = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones_admin, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.recycler_notificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificacionAdapter(listaNotificaciones);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::cargarNotificaciones);

        cargarNotificaciones();
        return view;
    }

    private void cargarNotificaciones() {
        swipeRefreshLayout.setRefreshing(true); // Muestra el indicador de carga

        NotificacionApi api = RetrofitClient.getRetrofitInstance().create(NotificacionApi.class);
        Call<List<Notificacion>> call = api.obtenerNotificacionesAdmin("incidencias");

        call.enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Notificacion> notificaciones = response.body();
                    Toast.makeText(getContext(), "Recibidas: " + notificaciones.size(), Toast.LENGTH_SHORT).show(); // 👈

                    listaNotificaciones.clear();
                    listaNotificaciones.addAll(notificaciones);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Respuesta vacía o incorrecta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
