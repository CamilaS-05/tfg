package com.example.conexionbbdd;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class NotificacionWorker extends Worker {

    private static final String CHANNEL_ID = "canal_notificaciones";

    public NotificacionWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        int idUsuario = getInputData().getInt("idUsuario", -1);
        if (idUsuario == -1) return Result.failure();

        // Crear canal para notificaciones (solo en Android 8+)
        crearCanalNotificacion();

        NotificacionApi api = RetrofitClient.getRetrofitInstance().create(NotificacionApi.class);

        try {
            Call<List<Notificacion>> call = api.obtenerNotificaciones(idUsuario, "incidencias");
            Response<List<Notificacion>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                List<Notificacion> notificaciones = response.body();

                for (Notificacion n : notificaciones) {
                    if (!n.isLeido()) {
                        mostrarNotificacion(n);
                    }
                }
            } else {
                return Result.retry();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }

        return Result.success();
    }

    private void mostrarNotificacion(Notificacion n) {
        Context context = getApplicationContext();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, DetalleReporteActivity.class);
        intent.putExtra("reporte_id", n.getIdReporte());
        intent.putExtra("notificacion_id", n.getId());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, n.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String titulo = "Nueva notificación"; // Título fijo
        String mensaje = n.getMensaje() != null ? n.getMensaje() : "";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificaciones)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (manager != null) {
            manager.notify(n.getId(), builder.build());
        }
    }
    private void crearCanalNotificacion() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    CHANNEL_ID,
                    "Notificaciones de la app",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canal);
        }
    }
}
