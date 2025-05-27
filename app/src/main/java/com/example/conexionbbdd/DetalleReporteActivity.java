package com.example.conexionbbdd;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleReporteActivity extends AppCompatActivity {
    private TextView txtitulo, txtAsunto, txtDescripcion, txtFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reporte);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // Inicializar los TextViews que SÍ se usan
        txtitulo = findViewById(R.id.txtTitulo);
        txtAsunto = findViewById(R.id.txtTituloReporte);
        txtDescripcion = findViewById(R.id.txtDescripcionReporte);
        txtFecha = findViewById(R.id.txtFechaReporte);

        int idReporte = getIntent().getIntExtra("reporte_id", -1);

        if (idReporte > 0) {
            cargarDetalleReporte(idReporte);
        } else {
            Toast.makeText(this, "ID de reporte inválido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void cargarDetalleReporte(int idReporte) {
        ReporteApi api = RetrofitClient.getRetrofitInstance().create(ReporteApi.class);
        api.getReportePorId(idReporte).enqueue(new Callback<Reporte>() {
            @Override
            public void onResponse(Call<Reporte> call, Response<Reporte> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Reporte reporte = response.body();
                   txtitulo.setText("REPORTE DETALLADO");
                    txtAsunto.setText( reporte.getAsunto());
                    txtDescripcion.setText( reporte.getDescripcion());
                    txtFecha.setText(formatoFecha(reporte.getFecha_creacion()));

                } else {
                    Toast.makeText(DetalleReporteActivity.this, "Error al cargar el reporte", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Reporte> call, Throwable t) {
                Toast.makeText(DetalleReporteActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private String formatoFecha(String fechaISO) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = isoFormat.parse(fechaISO);
            SimpleDateFormat formatoLocal = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return formatoLocal.format(date);
        } catch (Exception e) {
            return fechaISO;
        }
    }
}
