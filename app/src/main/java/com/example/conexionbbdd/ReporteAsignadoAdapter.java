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

    private List<ReporteDTO> listaOriginal;   // Lista completa (sin filtrar)
    private List<ReporteDTO> listaReportes;   // Lista visible (filtrada)
    private Context context;
    private ReporteApi reporteApi;

    public ReporteAsignadoAdapter(List<ReporteDTO> listaReportes, Context context, ReporteApi reporteApi) {
        this.listaOriginal = new ArrayList<>();
        this.listaReportes = new ArrayList<>();
        if (listaReportes != null) {
            this.listaOriginal.addAll(listaReportes);
            this.listaReportes.addAll(listaReportes);
        }
        this.context = context;
        this.reporteApi = reporteApi;
    }

    public void filtrar(String texto) {
        String textoLower = texto.toLowerCase();
        List<ReporteDTO> filtrados = new ArrayList<>();
        for (ReporteDTO r : listaOriginal) {
            String asunto = r.getAsunto() != null ? r.getAsunto().toLowerCase() : "";
            String descripcion = r.getDescripcion() != null ? r.getDescripcion().toLowerCase() : "";

            if (asunto.contains(textoLower) || descripcion.contains(textoLower)) {
                filtrados.add(r);
            }
        }
        actualizarLista(filtrados);
    }

    public void setListaCompleta(List<ReporteDTO> nuevaLista) {
        this.listaOriginal.clear();
        this.listaReportes.clear();

        if (nuevaLista != null) {
            this.listaOriginal.addAll(nuevaLista);
            this.listaReportes.addAll(nuevaLista);
        }

        notifyDataSetChanged();
    }

    public void actualizarLista(List<ReporteDTO> listaFiltrada) {
        this.listaReportes.clear();
        if (listaFiltrada != null) {
            this.listaReportes.addAll(listaFiltrada);
        }
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

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReporteAsignadoActivity.class);
            intent.putExtra("id_reporte", reporte.getId());
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
