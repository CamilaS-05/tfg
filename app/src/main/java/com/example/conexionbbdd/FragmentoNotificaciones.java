package com.example.conexionbbdd;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoNotificaciones extends Fragment {

    private RecyclerView recyclerView;
    private NotificacionAdapter adapter;
    private List<Notificacion> listaNotificaciones = new ArrayList<>();
    private NotificacionApi notificacionApi;  // Interfaz Retrofit

    public FragmentoNotificaciones() {
        // Constructor vacío obligatorio
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones, container, false);

        recyclerView = view.findViewById(R.id.recyclerNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicializa el adaptador con lista vacía
        adapter = new NotificacionAdapter(listaNotificaciones);
        recyclerView.setAdapter(adapter);

        // Inicializa Retrofit y la API
        notificacionApi = RetrofitClient.getRetrofitInstance().create(NotificacionApi.class);

        // Obtiene id de usuario de SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        int idUsuario = (int) prefs.getLong("id_usuario", -1);

        if (idUsuario != -1) {
            cargarNotificaciones(idUsuario);
        } else {
            Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void cargarNotificaciones(int idUsuario) {
        String tipoDestino = "incidencias";

        Call<List<Notificacion>> call = notificacionApi.obtenerNotificaciones(idUsuario, tipoDestino);
        call.enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaNotificaciones.clear();
                    listaNotificaciones.addAll(response.body());
                    adapter.notifyDataSetChanged(); // Actualiza el RecyclerView
                } else {
                    Toast.makeText(getContext(), "No hay notificaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar notificaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
