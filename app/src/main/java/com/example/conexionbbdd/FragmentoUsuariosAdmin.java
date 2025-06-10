package com.example.conexionbbdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

public class FragmentoUsuariosAdmin extends Fragment {

    private RecyclerView recyclerViewUsuarios;
    private UsuarioAdapter usuarioAdapter;
    private List<Usuario> listaUsuarios;
    private ProgressBar progressBarLoading;

    public FragmentoUsuariosAdmin() {
        // Constructor público vacío requerido
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_usuarios_admin, container, false);

        // Inicializa el RecyclerView y el ProgressBar
        recyclerViewUsuarios = view.findViewById(R.id.recyclerViewUsuarios);
        progressBarLoading = view.findViewById(R.id.progressBarLoading);

        // Configura el LayoutManager para el RecyclerView
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializa la lista de usuarios vacía
        listaUsuarios = new ArrayList<>();
        // Inicializa el adaptador con la lista vacía
        usuarioAdapter = new UsuarioAdapter(getContext(), listaUsuarios);
        // Asigna el adaptador al RecyclerView
        recyclerViewUsuarios.setAdapter(usuarioAdapter);

        // Retorna la vista inflada
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Carga los usuarios cuando la vista ha sido creada
        cargarUsuariosIncidencias();
    }

    private void cargarUsuariosIncidencias() {
        // Muestra el ProgressBar para indicar que se están cargando los datos
        progressBarLoading.setVisibility(View.VISIBLE);
        recyclerViewUsuarios.setVisibility(View.GONE); // Oculta el RecyclerView mientras carga

        // Obtiene la instancia de la API de usuario
        UsuarioApi usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);
        Call<List<Usuario>> call = usuarioApi.obtenerUsuariosIncidencias();

        // Realiza la llamada asíncrona a la API
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                // Oculta el ProgressBar cuando la respuesta es recibida
                progressBarLoading.setVisibility(View.GONE);
                recyclerViewUsuarios.setVisibility(View.VISIBLE); // Muestra el RecyclerView

                if (response.isSuccessful() && response.body() != null) {
                    // Si la respuesta es exitosa y hay datos, actualiza la lista
                    listaUsuarios.clear();
                    listaUsuarios.addAll(response.body());
                    usuarioAdapter.updateData(listaUsuarios); // Notifica al adaptador del cambio de datos
                    if (listaUsuarios.isEmpty()) {
                        Toast.makeText(getContext(), "No se encontraron usuarios de incidencias.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si la respuesta no es exitosa, muestra un mensaje de error
                    Toast.makeText(getContext(), "Error al obtener usuarios: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                // Oculta el ProgressBar si falla la llamada
                progressBarLoading.setVisibility(View.GONE);
                recyclerViewUsuarios.setVisibility(View.VISIBLE); // Muestra el RecyclerView (vacío o con datos anteriores)

                // Muestra un mensaje de error en caso de fallo de red o similar
                Toast.makeText(getContext(), "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace(); // Imprime el stack trace para depuración
            }
        });
    }
}
