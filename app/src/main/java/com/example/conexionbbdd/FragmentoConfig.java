package com.example.conexionbbdd;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
                    // Mostrar diálogo para confirmar eliminación
                    new AlertDialog.Builder(getContext())
                            .setTitle("Confirmar eliminación")
                            .setMessage("¿Seguro que quieres eliminar tu cuenta? Esta acción no se puede deshacer.")
                            .setPositiveButton("Eliminar", (dialog, which) -> {
                                eliminarCuenta();
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
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

    private void eliminarCuenta() {
        SharedPreferences prefs = getActivity().getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        Long idUsuario = prefs.getLong("id_usuario", -1);

        if (idUsuario == -1) {
            Toast.makeText(getActivity(), "Error: usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

// Llamada Retrofit para eliminar usuario
        UsuarioApi api = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);
        Call<ResponseBody> call = api.eliminarUsuario(idUsuario, "incidencias");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Cuenta eliminada correctamente", Toast.LENGTH_SHORT).show();
                    // Redirigir a LoginActivity y cerrar la actual
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Error al eliminar la cuenta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
}
}


