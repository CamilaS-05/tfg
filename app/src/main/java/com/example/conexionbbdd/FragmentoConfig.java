package com.example.conexionbbdd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class FragmentoConfig extends Fragment {

    public FragmentoConfig() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_config, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_config);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<OpcionConfig> lista = Arrays.asList(
                new OpcionConfig("Perfil", R.drawable.persona),
                new OpcionConfig("Notificaciones", R.drawable.ic_notificaciones),
                new OpcionConfig("Apariencia", R.drawable.ic_apariencia),
                new OpcionConfig("Eliminar cuenta", R.drawable.ic_eliminar)
        );

        ConfigAdapter adapter = new ConfigAdapter(lista, position -> {
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

        return view;
    }
}
