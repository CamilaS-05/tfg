package com.example.conexionbbdd;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    public PantallaPrincipal() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pantalla_principal, container, false);

        etBuscar = view.findViewById(R.id.etBuscar);
        iconoNotificaciones = view.findViewById(R.id.iconoNotificaciones);
        txtSaludo = view.findViewById(R.id.txtSaludo);

        navIncidencias = view.findViewById(R.id.navIncidencias);
        navPerfil = view.findViewById(R.id.navPerfil);
        navSoporte = view.findViewById(R.id.navSoporte);

        // Cambiar texto saludo dinámicamente (ejemplo)
        String nombreUsuario = "Carlos"; // Cambia por el nombre real
        txtSaludo.setText("¡Hola, " + nombreUsuario + "!");

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                Toast.makeText(getContext(), "Buscar: " + query, Toast.LENGTH_SHORT).show();
                // Aquí podrías filtrar tu lista o abrir un fragmento de búsqueda
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        iconoNotificaciones.setOnClickListener(v ->
                Toast.makeText(getContext(), "Notificaciones (por implementar)", Toast.LENGTH_SHORT).show()
        );

        navIncidencias.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentoMisIncidencias())
                        .commit()
        );

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
