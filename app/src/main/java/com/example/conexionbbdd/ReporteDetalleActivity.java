package com.example.conexionbbdd;



import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.conexionbbdd.ReporteApi;
import com.example.conexionbbdd.Reporte;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReporteDetalleActivity extends AppCompatActivity {

    private TextView txtAsunto, txtDescripcion, txtEstado, txtFecha;
    private Spinner spinnerEstado;
    private Button btnGuardar;

    private int idReporte;
    private String estadoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_detalle);

        // Vincular vistas
        txtAsunto = findViewById(R.id.txtAsunto);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtEstado = findViewById(R.id.txtEstadoActual);
        txtFecha = findViewById(R.id.txtFecha);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        btnGuardar = findViewById(R.id.btnCambiarEstado);

        // Obtener datos del intent
        Reporte reporte = (Reporte) getIntent().getSerializableExtra("reporte");
        if (reporte != null) {
            idReporte = reporte.getId();
            txtAsunto.setText(reporte.getAsunto());
            txtDescripcion.setText(reporte.getDescripcion());
            txtEstado.setText(reporte.getEstado());
            txtFecha.setText(formatoFecha(reporte.getFechaCreacion()));

        }



        // Spinner de estados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.estados_reporte, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        spinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estadoSeleccionado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                estadoSeleccionado = null;
            }
        });

        // Botón para cambiar estado
        btnGuardar.setOnClickListener(v -> {
            if (estadoSeleccionado == null || estadoSeleccionado.equals(reporte.getEstado())) {
                Toast.makeText(this, "Selecciona un estado diferente", Toast.LENGTH_SHORT).show();
                return;
            }

            cambiarEstadoReporte(idReporte, estadoSeleccionado);
        });
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
    private void cambiarEstadoReporte(int idReporte, String nuevoEstado) {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ReporteApi api = retrofit.create(ReporteApi.class);

        Call<String> call = api.cambiarEstado(idReporte, nuevoEstado);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReporteDetalleActivity.this, "Estado actualizado", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad
                } else {
                    Toast.makeText(ReporteDetalleActivity.this, "Error al cambiar estado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ReporteDetalleActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
