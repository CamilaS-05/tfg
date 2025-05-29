package com.example.conexionbbdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Arrays;
import java.util.List;

public class FragmentoConfig extends Fragment {
    ImageButton btnVolver;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ConfigAdapter adapter;
    List<OpcionConfig> lista;

    public FragmentoConfig() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_config, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        recyclerView = view.findViewById(R.id.recyclerMisReportes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnVolver = view.findViewById(R.id.btn_volver);

        lista = Arrays.asList(
                new OpcionConfig("Perfil", R.drawable.persona),
                new OpcionConfig("Notificaciones", R.drawable.ic_notificaciones),
                new OpcionConfig("Apariencia", R.drawable.ic_apariencia),
                new OpcionConfig("Eliminar cuenta", R.drawable.ic_eliminar)
        );

        adapter = new ConfigAdapter(lista, position -> {
            switch (position) {
                case 0:
                    // abrir pantalla de perfil
                    break;
                case 1:
                    // abrir configuración de notificaciones
                    break;
                case 2:
                    // abrir ajustes de apariencia
                    break;
                case 3:
                    // confirmar y eliminar cuenta
                    break;
            }
        });

        recyclerView.setAdapter(adapter);

        btnVolver.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new PantallaPrincipal())
                        .commit();
            }
        });

        // Configurar SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Aquí iría la lógica para refrescar datos,
            // ahora simplemente refrescamos la lista estática

            // Si la lista viniera de un servidor, aquí harías la llamada para actualizar

            // Simulamos refresco de datos llamando notifyDataSetChanged
            adapter.notifyDataSetChanged();

            // Detenemos el indicador de refresco
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }
}
