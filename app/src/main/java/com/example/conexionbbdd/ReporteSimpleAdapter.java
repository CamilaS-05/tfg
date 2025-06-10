package com.example.conexionbbdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReporteSimpleAdapter extends RecyclerView.Adapter<ReporteSimpleAdapter.ReporteViewHolder> {

    private List<ReporteDTO> lista;
    private Context context;

    public ReporteSimpleAdapter(Context context, List<ReporteDTO> lista) {
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
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte_ver, parent, false);
        return new ReporteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ReporteViewHolder holder, int position) {
        ReporteDTO reporte = lista.get(position);

        holder.textViewAsunto.setText(reporte.getAsunto());
        holder.textViewDescripcion.setText(reporte.getDescripcion());
        holder.textViewEstado.setText("Estado: " + reporte.getEstado());
        holder.textViewFecha.setText("Fecha: " + formatoFecha(reporte.getFecha()));
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
            // Si no puede parsear, retorna la fecha original
            return fechaISO;
        }
    }

    static class ReporteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAsunto, textViewDescripcion, textViewEstado, textViewFecha;

        public ReporteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAsunto = itemView.findViewById(R.id.textViewAsunto);
            textViewDescripcion = itemView.findViewById(R.id.textViewDescripcion);
            textViewEstado = itemView.findViewById(R.id.textViewEstado);
            textViewFecha = itemView.findViewById(R.id.textViewFecha);
        }
    }
}
