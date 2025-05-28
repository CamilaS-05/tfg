package com.example.conexionbbdd;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReporteAsignadoAdapter extends RecyclerView.Adapter<ReporteAsignadoAdapter.ViewHolder> {

    private List<ReporteDTO> listaReportes;
    private Context context;
    private ReporteApi reporteApi;

    // Constructor con los 3 parámetros
    public ReporteAsignadoAdapter(List<ReporteDTO> listaReportes, Context context, ReporteApi reporteApi) {
        this.listaReportes = listaReportes;
        this.context = context;
        this.reporteApi = reporteApi;
    }



    public void setListaReportes(List<ReporteDTO> listaReportes) {
        this.listaReportes = listaReportes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReporteAsignadoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reporte_asignado, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteAsignadoAdapter.ViewHolder holder, int position) {
        ReporteDTO reporte = listaReportes.get(position);
        holder.textViewAsunto.setText(reporte.getAsunto());
        holder.textViewDescripcion.setText(reporte.getDescripcion());
        holder.textViewEstado.setText("Estado: " + reporte.getEstado());
        holder.textViewFecha.setText("Fecha: " + reporte.getFecha());

        // Aquí agregas el listener para abrir la actividad
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReporteAsignadoActivity.class);
            intent.putExtra("id_reporte", reporte.getId()); // Envías el ID del reporte
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaReportes != null ? listaReportes.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAsunto, textViewDescripcion, textViewEstado, textViewFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAsunto = itemView.findViewById(R.id.textViewAsunto);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
        }
    }
}
