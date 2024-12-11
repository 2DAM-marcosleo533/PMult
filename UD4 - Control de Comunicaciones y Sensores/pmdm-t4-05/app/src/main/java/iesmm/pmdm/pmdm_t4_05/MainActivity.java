package iesmm.pmdm.pmdm_t4_05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Instanciación de componentes visuales para su control
        Button start = (Button) this.findViewById(R.id.button);

        // 2. Vinculamos el control del escuchador de eventos con el componente
        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                showProgress();

                break;
        }
    }

    /* Muestra un cuadro de diálogo con barra de progreso */
    private void showProgress() {
        // Configurar ProgressDialog
        progress = new ProgressDialog(this);
        progress.setTitle("Descarga simulada"); // Titulo
        progress.setMessage("Cargando..."); // Contenido
        progress.setMax(100); // Valor máximo de la barra de progreso
        progress.setCancelable(true); // Permitir que se cancele por el usuario
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // Tipo de barra
        progress.show(); // Mostrar el cuadro de diálogo

        // Ejecutar AsyncTask
        new SimulateDownloadTask().execute();
    }

    // Clase interna que simula la descarga usando AsyncTask
    private class SimulateDownloadTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Se ejecuta antes de iniciar la tarea en segundo plano
            Toast.makeText(MainActivity.this, "Iniciando descarga...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Simular el progreso en segundo plano
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(50); // Simular tiempo de descarga
                    publishProgress(i); // Actualizar progreso
                } catch (InterruptedException e) {
                    Log.e("AsyncTask", "Error en la simulación de descarga", e);
                }
            }
            return null; // No se necesita devolver un resultado
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // Actualizar la barra de progreso
            if (progress != null) {
                progress.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Finalizar el cuadro de diálogo cuando termine la tarea
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
            // Mostrar mensaje de finalización
            Snackbar.make(findViewById(R.id.button), "Descarga completada", Snackbar.LENGTH_SHORT).show();
        }
    }

}