package com.example.conexionbbdd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConfigAdapter extends RecyclerView.Adapter<ConfigAdapter.ViewHolder> {

    private final List<OpcionConfig> opciones;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ConfigAdapter(List<OpcionConfig> opciones, OnItemClickListener listener) {
        this.opciones = opciones;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icono;
        TextView titulo;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            icono = itemView.findViewById(R.id.icono);
            titulo = itemView.findViewById(R.id.titulo);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu_config, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OpcionConfig opcion = opciones.get(position);
        holder.titulo.setText(opcion.titulo);
        holder.icono.setImageResource(opcion.icono);
    }

    @Override
    public int getItemCount() {
        return opciones.size();
    }
}
