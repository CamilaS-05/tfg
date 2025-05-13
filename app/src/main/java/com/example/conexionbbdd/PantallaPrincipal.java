package com.example.conexionbbdd;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PantallaPrincipal extends Fragment {

    public PantallaPrincipal() {
        // Constructor vacío requerido por el sistema.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout del fragmento de inicio.
        return inflater.inflate(R.layout.pantalla_principal, container, false);
    }
}