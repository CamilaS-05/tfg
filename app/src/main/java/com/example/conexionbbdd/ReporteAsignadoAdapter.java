package com.example.conexionbbdd;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReporteAsignadoAdapter extends RecyclerView.Adapter<ReporteAsignadoAdapter.ViewHolder> {

    private List<ReporteDTO> listaOriginal;    // Lista completa sin filtrar
    private List<ReporteDTO> listaReportes;    // Lista filtrada que mostramos
    private Context context;
    private ReporteApi reporteApi;

    // Constructor con lista original y contexto
    public ReporteAsignadoAdapter(List<ReporteDTO> listaReportes, Context context, ReporteApi reporteApi) {
        this.listaOriginal = new ArrayList<>();
        if (listaReportes != null) {
            this.listaOriginal.addAll(listaReportes);
        }
        this.listaReportes = listaReportes != null ? listaReportes : new ArrayList<>();
        this.context = context;
        this.reporteApi = reporteApi;
    }

    // Actualiza la lista completa (sin filtrar)
    public void setListaOriginal(List<ReporteDTO> listaReportes) {
        if (listaReportes == null) {
            this.listaOriginal = new ArrayList<>();
        } else {
            this.listaOriginal = new ArrayList<>(listaReportes);
        }
        this.listaReportes = new ArrayList<>(this.listaOriginal);
        notifyDataSetChanged();
    }

    // Actualiza sólo la lista filtrada que se muestra
    public void actualizarLista(List<ReporteDTO> listaFiltrada) {
        if (listaFiltrada == null) {
            this.listaReportes = new ArrayList<>();
        } else {
            this.listaReportes = listaFiltrada;
        }
        notifyDataSetChanged();
    }

    // Método para filtrar localmente por asunto, estado, fecha, etc.
    public void filtrar(String asunto, String estado, String fecha) {
        List<ReporteDTO> filtrados = new ArrayList<>();

        for (ReporteDTO r : listaOriginal) {
            boolean cumpleAsunto = asunto == null || asunto.isEmpty() || r.getAsunto().toLowerCase().contains(asunto.toLowerCase());
            boolean cumpleEstado = estado == null || estado.isEmpty() || r.getEstado().toLowerCase().contains(estado.toLowerCase());
            boolean cumpleFecha = fecha == null || fecha.isEmpty() || r.getFecha().toLowerCase().contains(fecha.toLowerCase());

            if (cumpleAsunto && cumpleEstado && cumpleFecha) {
                filtrados.add(r);
            }
        }

        actualizarLista(filtrados);
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

        // Listener para abrir detalle de reporte
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReporteAsignadoActivity.class);
            intent.putExtra("id_reporte", reporte.getId()); // Enviamos ID del reporte
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
