package com.example.conexionbbdd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UltimosReportesAdapter extends RecyclerView.Adapter<UltimosReportesAdapter.ViewHolder> {
    private List<ReporteDTO> listaReportes;
    private Context context;

    public UltimosReportesAdapter(List<ReporteDTO> listaReportes, Context context) {
        this.listaReportes = listaReportes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reporte_resumen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReporteDTO reporte = listaReportes.get(position);
        holder.txtTitulo.setText(reporte.getAsunto());
        holder.txtFecha.setText(reporte.getFecha());
        holder.txtEstado.setText(reporte.getEstado());
    }

    @Override
    public int getItemCount() {
        return listaReportes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtFecha, txtEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTituloReporte);
            txtFecha = itemView.findViewById(R.id.txtFechaReporte);
            txtEstado = itemView.findViewById(R.id.txtEstadoReporte);
        }
    }
}
