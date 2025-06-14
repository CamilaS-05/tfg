package com.example.conexionbbdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
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
                new OpcionConfig("Dar de alta administrador", R.drawable.persona),
                new OpcionConfig("Apariencia", R.drawable.ic_apariencia),
                new OpcionConfig("Eliminar cuenta", R.drawable.ic_eliminar)
        );

        ConfigAdapter adapter = new ConfigAdapter(lista, position -> {
            switch (position) {
                case 0:
                    // Abrir pantalla de perfil
                    FragmentoPerfilConfig perfilFragment = new FragmentoPerfilConfig();
                    Bundle args = new Bundle();

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
                    // Abrir configuración de notificaciones (puedes implementar)
                    break;

                case 2:
                    // Dar de alta administrador (puedes implementar)
                    break;

                case 3:
                    // Ajustes de apariencia - mostrar selector tema
                    new AlertDialog.Builder(getContext())
                            .setTitle("Selecciona el tema")
                            .setItems(new CharSequence[]{"Claro", "Oscuro"}, (dialog, which) -> {
                                if (which == 0) {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                    guardarTema("claro");
                                } else {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                    guardarTema("oscuro");
                                }
                            })
                            .show();
                    break;

                case 4:
                    // Confirmar y eliminar cuenta (puedes implementar)
                    break;
            }
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    private void guardarTema(String tema) {
        SharedPreferences.Editor editor = requireActivity()
                .getSharedPreferences("MisPreferenciasAdmin", Context.MODE_PRIVATE)
                .edit();
        editor.putString("tema_app", tema);
        editor.apply();
    }
}
