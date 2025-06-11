package com.example.conexionbbdd;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReporteAsignadoActivity extends AppCompatActivity {

    private TextView txtAsunto, txtDescripcion, txtEstadoActual, txtFecha;
    private Spinner spinnerNuevoEstado;
    private Button btnCambiarEstado;
    private ReporteApi reporteApi;
    private int idReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_asignado);

        txtAsunto = findViewById(R.id.txtAsunto);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtEstadoActual = findViewById(R.id.txtEstadoActual);
        txtFecha = findViewById(R.id.txtFecha);
        spinnerNuevoEstado = findViewById(R.id.spinnerNuevoEstado);
        btnCambiarEstado = findViewById(R.id.btnCambiarEstado);

        reporteApi = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);
        idReporte = getIntent().getIntExtra("id_reporte", -1);

        if (idReporte == -1) {
            Toast.makeText(this, "Error al recibir el ID del reporte", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDetalleReporte(idReporte);

        // Lista de estados, la misma que en tu string-array o la que quieras:
        List<String> estadosList = Arrays.asList("Pendiente", "En Proceso", "Resuelto", "Cerrado");

        // Adapter personalizado con layouts para spinner bonito
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.item_spinner_selected,  // Layout para el item seleccionado
                estadosList);

        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown); // Layout para dropdown
        spinnerNuevoEstado.setAdapter(adapter);

        btnCambiarEstado.setOnClickListener(view -> {
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_bounce));
            cambiarEstado();
        });
    }

    private void cargarDetalleReporte(int idReporte) {
        reporteApi.getReportePorId(idReporte).enqueue(new Callback<Reporte>() {
            @Override
            public void onResponse(Call<Reporte> call, Response<Reporte> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Reporte reporte = response.body();
                    txtAsunto.setText(reporte.getAsunto());
                    txtDescripcion.setText(reporte.getDescripcion());
                    txtEstadoActual.setText("Estado actual: " + reporte.getEstado());
                    txtFecha.setText(formatoFecha(reporte.getFechaCreacion()));
                } else {
                    Toast.makeText(ReporteAsignadoActivity.this, "No se pudo cargar el reporte", Toast.LENGTH_SHORT).show();
                }
            }
            private String formatoFecha(String fechaISO) {
                if (fechaISO == null || fechaISO.isEmpty()) return "Fecha no disponible";
                try {
                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    Date date = isoFormat.parse(fechaISO);
                    SimpleDateFormat formatoLocal = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    return formatoLocal.format(date);
                } catch (Exception e) {
                    return fechaISO;
                }
            }


            @Override
            public void onFailure(Call<Reporte> call, Throwable t) {
                Toast.makeText(ReporteAsignadoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cambiarEstado() {
        String nuevoEstado = spinnerNuevoEstado.getSelectedItem().toString();
        reporteApi.cambiarEstado(idReporte, nuevoEstado).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReporteAsignadoActivity.this, "Estado actualizado", Toast.LENGTH_SHORT).show();

                    // ✅ Esto cerrará la actividad y volverá al Fragmento
                    setResult(Activity.RESULT_OK); // opcional
                    finish();
                } else {
                    Toast.makeText(ReporteAsignadoActivity.this, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ReporteAsignadoActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
