package com.example.conexionbbdd;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class PantallaPrincipal extends Fragment {

    private EditText etBuscar;
    private ImageView iconoNotificaciones;
    private TextView txtSaludo;

    private LinearLayout navIncidencias, navPerfil, navSoporte;

    private ImageButton btnMenu;  // botón para abrir drawer

    // Referencia al fragmento para llamar a filtrar
    private FragmentoMisIncidencias fragmentoMisIncidencias;

    public PantallaPrincipal() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pantalla_principal, container, false);

      //  btnMenu = view.findViewById(R.id.btnMenu);
        etBuscar = view.findViewById(R.id.etBuscar);
        iconoNotificaciones = view.findViewById(R.id.iconoNotificaciones);
        txtSaludo = view.findViewById(R.id.txtSaludo);

        navIncidencias = view.findViewById(R.id.navIncidencias);
        navPerfil = view.findViewById(R.id.navPerfil);
        navSoporte = view.findViewById(R.id.navSoporte);

        // Leer nombre de usuario desde SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        String nombreUsuario = prefs.getString("nombre_usuario", "Usuario");

        txtSaludo.setText("Área de trabajo de " + nombreUsuario );

        // Click para abrir el drawer desde el activity
//        btnMenu.setOnClickListener(v -> {
//            if (getActivity() instanceof MainActivity) {
//                ((MainActivity) getActivity()).abrirDrawer();
//            }
//        });

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if (fragmentoMisIncidencias != null) {
                    fragmentoMisIncidencias.filtrarReportes(query.toString(), "", "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        iconoNotificaciones.setOnClickListener(v ->
                Toast.makeText(getContext(), "Notificaciones (por implementar)", Toast.LENGTH_SHORT).show()
        );

        navIncidencias.setOnClickListener(v -> {
            fragmentoMisIncidencias = new FragmentoMisIncidencias();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragmentoMisIncidencias)
                    .commit();
        });

        navPerfil.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentoPerfil())
                        .commit()
        );

        navSoporte.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentoConfig())
                        .commit()
        );

        return view;
    }
}
