package com.example.conexionbbdd;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        holder.txtFecha.setText(formatoFecha(reporte.getFecha()));
        holder.txtEstado.setText(reporte.getEstado());

        // Añadimos listener para abrir detalle al clicar el item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleReporteActivity.class);
            intent.putExtra("reporte_id", reporte.getId());  // Pasamos el id del reporte
            context.startActivity(intent);
        });
    }

    private String formatoFecha(String fechaISO) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = isoFormat.parse(fechaISO);
            SimpleDateFormat formatoLocal = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return formatoLocal.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return fechaISO; // Si hay error, devolver la cadena original
        }
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
            txtEstado = itemView.findViewById(R.id.textViewEstado);
        }
    }
}
