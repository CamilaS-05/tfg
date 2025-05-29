package com.example.conexionbbdd;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReporteAdapter extends RecyclerView.Adapter<ReporteAdapter.ReporteViewHolder> {

    private List<ReporteDTO> lista;
    private Context context;

    public interface OnUsuarioAsignadoListener {
        void onUsuarioAsignado();
    }

    private OnUsuarioAsignadoListener listener;

    public void setOnUsuarioAsignadoListener(OnUsuarioAsignadoListener listener) {
        this.listener = listener;
    }

    public ReporteAdapter(Context context, List<ReporteDTO> lista) {
        this.context = context;
        this.lista = lista;
    }

    public void updateData(List<ReporteDTO> nuevosReportes) {
        this.lista = nuevosReportes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReporteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte, parent, false);
        return new ReporteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        ReporteDTO reporte = lista.get(position);
        String nombre = reporte.getNombreAsignado();
        holder.txtAsunto.setText("Asunto: " + reporte.getAsunto());
        holder.txtEstado.setText("Estado: " + reporte.getEstado());
        holder.txtFecha.setText("Fecha: " + formatoFecha(reporte.getFecha()));
        holder.txtAsignado.setText("Asignado a: " + (nombre != null && !nombre.isEmpty() ? nombre : "Asignando..."));
        holder.btnAsignar.setOnClickListener(v -> mostrarDialogoAsignarUsuario(reporte, position));
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    private String formatoFecha(String fechaISO) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = isoFormat.parse(fechaISO);
            SimpleDateFormat formatoLocal = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return formatoLocal.format(date);
        } catch (Exception e) {
            return fechaISO;
        }
    }

    private void mostrarDialogoAsignarUsuario(ReporteDTO reporte, int position) {
        UsuarioApi usuarioApi = RetrofitClient.getRetrofitInstance().create(UsuarioApi.class);
        usuarioApi.obtenerUsuariosIncidencias().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Usuario> usuarios = response.body();

                    if (usuarios.isEmpty()) {
                        Toast.makeText(context, "No hay usuarios disponibles", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_asignar_usuario, null);
                    Spinner spinnerUsuarios = dialogView.findViewById(R.id.spinnerUsuarios);

                    ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(
                            context,
                            android.R.layout.simple_spinner_item,
                            usuarios
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerUsuarios.setAdapter(adapter);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Asignar usuario");
                    builder.setView(dialogView);
                    builder.setPositiveButton("Asignar", (dialog, which) -> {
                        Usuario usuarioSeleccionado = (Usuario) spinnerUsuarios.getSelectedItem();
                        if (usuarioSeleccionado != null) {
                            asignarUsuarioAReporte(reporte, usuarioSeleccionado.getId(), position, usuarioSeleccionado.getNombreCompleto());
                        } else {
                            Toast.makeText(context, "No se seleccionó ningún usuario", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancelar", null);
                    builder.show();
                } else {
                    Toast.makeText(context, "No se pudieron obtener los usuarios", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void asignarUsuarioAReporte(ReporteDTO reporte, int idUsuario, int position, String nombreCompleto) {
        ReporteApi api = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);

        // Guardamos nombre anterior en caso de error
        String nombreAnterior = reporte.getNombreAsignado();

        // Actualizamos la UI inmediatamente
        reporte.setNombreAsignado(nombreCompleto);
        reporte.setEstado("en progreso");
        notifyItemChanged(position);

        Call<String> call = api.asignarUsuario(reporte.getId(), idUsuario);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Usuario asignado correctamente", Toast.LENGTH_SHORT).show();

                    if (listener != null) {
                        listener.onUsuarioAsignado();
                    }
                } else {
                    // Revertimos los cambios
                    reporte.setNombreAsignado(nombreAnterior);
                    notifyItemChanged(position);
                    Toast.makeText(context, "Error al asignar usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                reporte.setNombreAsignado(nombreAnterior);
                notifyItemChanged(position);
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView txtAsunto, txtEstado, txtFecha, txtAsignado;
        Button btnAsignar;

        ReporteViewHolder(View itemView) {
            super(itemView);
            txtAsunto = itemView.findViewById(R.id.txtAsunto);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtAsignado = itemView.findViewById(R.id.txtAsignado);
            btnAsignar = itemView.findViewById(R.id.btnAsignar);
        }
    }
}
