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

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> listaUsuarios;
    private Context context;

    public UsuarioAdapter(Context context, List<Usuario> listaUsuarios) {
        this.context = context;
        this.listaUsuarios = listaUsuarios;
    }

    // Método para actualizar la lista de usuarios
    public void updateData(List<Usuario> nuevosUsuarios) {
        this.listaUsuarios = nuevosUsuarios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout del item de usuario
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        // Obtiene el usuario actual y establece los datos en los TextViews
        Usuario usuario = listaUsuarios.get(position);
        holder.textViewUserName.setText(usuario.getNombreCompleto());
        holder.textViewUserEmail.setText(usuario.getCorreo());
        holder.textViewUserPhone.setText("Teléfono: " + usuario.getTelefono());

        // Configura el OnClickListener para cada ítem de usuario
        holder.itemView.setOnClickListener(v -> {
            // Crea un Intent para iniciar ReportesAsignadosActivity
            Intent intent = new Intent(context, ReporteAsignadoActivity.class);
            // Pasa el ID del usuario a la nueva actividad
            intent.putExtra("id_usuario", usuario.getId());
            intent.putExtra("nombre_usuario", usuario.getNombreCompleto()); // Opcional: pasar el nombre para el título
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Retorna la cantidad de usuarios en la lista
        return listaUsuarios != null ? listaUsuarios.size() : 0;
    }

    // Clase interna ViewHolder para mantener las referencias a los views de cada ítem
    static class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;
        TextView textViewUserEmail;
        TextView textViewUserPhone;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializa los TextViews
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserEmail = itemView.findViewById(R.id.textViewUserEmail);
            textViewUserPhone = itemView.findViewById(R.id.textViewUserPhone);
        }
    }
}
