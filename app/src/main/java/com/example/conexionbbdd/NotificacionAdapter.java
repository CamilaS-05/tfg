package com.example.conexionbbdd;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Notificacion noti = lista.get(position);
        holder.mensaje.setText(noti.getMensaje());
        holder.fecha.setText(noti.getFecha());

        // Intentamos extraer el remitente desde el mensaje
        if (noti.getMensaje().contains("por ")) {
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

        // Al hacer clic, mostrar detalle y opción para marcar como leída
        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Notificación")
                    .setMessage(noti.getMensaje())
                    .setPositiveButton("Marcar como leída", (dialog, which) -> {
                        NotificacionApi api = RetrofitClient.getRetrofitInstance().create(NotificacionApi.class);
                        api.marcarComoLeida(noti.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(v.getContext(), "Marcada como leída", Toast.LENGTH_SHORT).show();
                                    noti.setLeido(true);
                                    notifyItemChanged(position);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(v.getContext(), "Error al marcar", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Cerrar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mensaje, fecha, remitente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mensaje = itemView.findViewById(R.id.txtMensaje);
            fecha = itemView.findViewById(R.id.txtFecha);
            remitente = itemView.findViewById(R.id.txtRemitente);
        }
    }
}
