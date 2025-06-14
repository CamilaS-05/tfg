package com.example.conexionbbdd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentoConfig extends Fragment {

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

        lista = Arrays.asList(
                new OpcionConfig("Perfil", R.drawable.persona),
                new OpcionConfig("Notificaciones", R.drawable.ic_notificaciones),
                new OpcionConfig("Apariencia", R.drawable.ic_apariencia),
                new OpcionConfig("Eliminar cuenta", R.drawable.ic_eliminar)
        );

        adapter = new ConfigAdapter(lista, position -> {
            switch (position) {
                case 0:
                    // Abrir pantalla de edición de perfil
                    Intent intentPerfil = new Intent(getContext(), EditarPerfilActivity.class);
                    startActivity(intentPerfil);
                    break;

                case 1:
                    // Aquí podrías abrir configuración de notificaciones
                    Toast.makeText(getContext(), "Funcionalidad no implementada aún", Toast.LENGTH_SHORT).show();
                    break;

                case 2:
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

                case 3:
                    new AlertDialog.Builder(getContext())
                            .setTitle("Confirmar eliminación")
                            .setMessage("¿Seguro que quieres eliminar tu cuenta? Esta acción no se puede deshacer.")
                            .setPositiveButton("Eliminar", (dialog, which) -> eliminarCuenta())
                            .setNegativeButton("Cancelar", null)
                            .show();
                    break;
            }
        });

        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Refrescar datos (aquí es estático, solo actualizamos la vista)
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    private void guardarTema(String tema) {
        SharedPreferences.Editor editor = requireActivity()
                .getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
                .edit();
        editor.putString("tema_app", tema);
        editor.apply();
    }

    private void eliminarCuenta() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        long idUsuario = prefs.getLong("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(getActivity(), "Error: usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        UsuarioApi api = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);
        Call<ResponseBody> call = api.eliminarUsuario(idUsuario, "incidencias");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
