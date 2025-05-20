package com.example.conexionbbdd;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    private List<Reporte> lista;
    private Context context;

    public ReporteAdapter(Context context, List<Reporte> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte, parent, false);
        return new ReporteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        Reporte reporte = lista.get(position);
        holder.txtAsunto.setText("Asunto: " + reporte.getAsunto());
        holder.txtEstado.setText("Estado: " + reporte.getEstado());
        holder.txtFecha.setText("Fecha: " + reporte.getFecha_creacion());

        holder.btnAsignar.setOnClickListener(v -> {
            mostrarDialogoAsignarUsuario(reporte, position);
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    private void mostrarDialogoAsignarUsuario(Reporte reporte, int position) {
        ReporteApi api = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);
        api.obtenerUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Usuario> usuarios = response.body();
                    String[] nombres = new String[usuarios.size()];
                    int[] ids = new int[usuarios.size()];
                    for (int i = 0; i < usuarios.size(); i++) {
                        nombres[i] = usuarios.get(i).getNombre();
                        ids[i] = usuarios.get(i).getId();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Selecciona un usuario para asignar");
                    builder.setItems(nombres, (dialog, which) -> {
                        asignarUsuarioAReporte(reporte, ids[which], position);
                    });
                    builder.show();

                } else {
                    Toast.makeText(context, "No se pudo cargar usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asignarUsuarioAReporte(Reporte reporte, int idUsuario, int position) {
        ReporteApi api = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);

        Call<String> call = api.asignarUsuario(reporte.getId(), idUsuario);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Usuario asignado correctamente", Toast.LENGTH_SHORT).show();
                    reporte.setIdUsuarioAsignado(idUsuario);
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(context, "Error al asignar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView txtAsunto, txtEstado, txtFecha;
        Button btnAsignar;

        ReporteViewHolder(View itemView) {
            super(itemView);
            txtAsunto = itemView.findViewById(R.id.txtAsunto);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            btnAsignar = itemView.findViewById(R.id.btnAsignar);
        }
    }
}
