package com.example.conexionbbdd;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.ViewHolder> {
    private List<Notificacion> lista;

    public NotificacionAdapter(List<Notificacion> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notificacion noti = lista.get(position);
        holder.mensaje.setText(noti.getMensaje());
        holder.fecha.setText(noti.getFecha());

        if (noti.getMensaje() != null && noti.getMensaje().contains("por ")) {
            String[] partes = noti.getMensaje().split("por ");
            if (partes.length > 1) {
                String[] nombreYresto = partes[1].split(":");
                String remitente = nombreYresto[0].trim();
                holder.remitente.setText("De: " + remitente);
                holder.remitente.setVisibility(View.VISIBLE);
            } else {
                holder.remitente.setVisibility(View.GONE);
            }
        } else {
            holder.remitente.setVisibility(View.GONE);
        }

        if (noti.isLeido()) {
            holder.itemView.setAlpha(0.5f);
        } else {
            holder.itemView.setAlpha(1.0f);
        }

        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();

            if (noti.getIdReporte() != null) {
                NotificacionApi api = RetrofitClient.getRetrofitInstance().create(NotificacionApi.class);

                // Primero obtenemos el detalle y marcamos como leída en backend (por si no está ya hecho)
                api.getReporteYMarcarLeido(noti.getId()).enqueue(new Callback<Reporte>() {
                    @Override
                    public void onResponse(Call<Reporte> call, Response<Reporte> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Reporte reporte = response.body();

                            // Marcar explícitamente la notificación como leída
                            api.marcarComoLeida(noti.getId()).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    // Ignorar respuesta
                                }
                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    // Ignorar fallo en marcar leída
                                }
                            });

                            // Abrir detalle del reporte
                            Intent intent = new Intent(context, DetalleReporteActivity.class);
                            intent.putExtra("reporte_id", reporte.getId());
                            context.startActivity(intent);

                            // Eliminar notificación de la lista y refrescar UI
                            int position = holder.getAdapterPosition();
                            lista.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, lista.size());

                        } else {
                            Toast.makeText(context, "No se pudo obtener el reporte", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Reporte> call, Throwable t) {
                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(context, "Reporte no disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mensaje, fecha, remitente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.txtMensaje);
            fecha = itemView.findViewById(R.id.txtFecha);
            remitente = itemView.findViewById(R.id.txtRemitente);
        }
    }
}
