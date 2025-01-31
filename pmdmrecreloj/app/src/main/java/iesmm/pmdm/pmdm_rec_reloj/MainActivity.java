package iesmm.pmdm.pmdm_rec_reloj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView relojTextView;
    private String ultimoMinuto = "";
    private String ultimaHora = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relojTextView = findViewById(R.id.relojTextView);

        // Iniciamos la tarea de actualizar el reloj
        new RelojTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class RelojTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            while (!isCancelled()) {
                String horaActual = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                publishProgress(horaActual);

                String minutoActual = horaActual.substring(3, 5);
                String horaSolo = horaActual.substring(0, 2);


                if (ultimoMinuto.isEmpty() && ultimaHora.isEmpty()) {
                    ultimoMinuto = minutoActual;
                    ultimaHora = horaSolo;
                }

                // Vemos si ha cambiado el minuto
                if (!minutoActual.equals(ultimoMinuto)) {
                    publishProgress("Se ha cambiado de minuto");
                    ultimoMinuto = minutoActual;
                }

                // Vemos si ha cambiado la hora
                if (!horaSolo.equals(ultimaHora)) {
                    publishProgress("Se ha cambiado de hora");
                    ultimaHora = horaSolo;
                }

                try {
                    // Esperamos 1 segundo antes de actualizar
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values.length > 0) {
                String mensaje = values[0];
                if (mensaje.equals("Se ha cambiado de minuto") || mensaje.equals("Se ha cambiado de hora")) {
                    // Mostramos si hay cambio de minuto u hora
                    Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                } else {
                    // Actualizamos el reloj
                    relojTextView.setText(mensaje);
                }
            }
        }
    }
    }

