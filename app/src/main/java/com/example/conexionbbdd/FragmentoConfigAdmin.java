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

public class FragmentoConfigAdmin extends Fragment {

    public FragmentoConfigAdmin() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_config_admin, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_config_admin);
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
                    // Abrir pantalla de perfil
                    FragmentoPerfilConfig perfilFragment = new FragmentoPerfilConfig();

                    Bundle args = new Bundle();

                    // Obtener el nombre del admin logueado desde el Intent que abrió esta Activity
                    String nombreAdmin = getActivity().getIntent().getStringExtra("usuario_logueado");
                    args.putString("nombre_Admin", nombreAdmin);

                    perfilFragment.setArguments(args);

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, perfilFragment)
                            .addToBackStack(null)
                            .commit();
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
