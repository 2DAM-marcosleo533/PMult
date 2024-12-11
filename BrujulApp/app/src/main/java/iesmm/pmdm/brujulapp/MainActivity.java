package iesmm.pmdm.brujulapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Creamos los sensores
    SensorManager sensorManager;
    Sensor sensor;
    private static float currentDegree = 0f;
    private ImageView compassImage;
    private TextView degreesText, cardinalDirectionText;
    // Medidor
    private Medidor medidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compassImage = findViewById(R.id.compass_image);
        degreesText = findViewById(R.id.degrees_text);
        cardinalDirectionText = findViewById(R.id.cardinal_direction);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        // Verificamos si el sensor está disponible
        if (sensor == null) {
            Toast.makeText(this, "Sensor de orientación no disponible en este dispositivo", Toast.LENGTH_LONG).show();
            return;
        }

        // Registramos el listener para el sensor
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

        // Inicializamos la tarea asíncrona que guarda los datos cada 10 segundos
        medidor = new Medidor();
        medidor.execute();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = Math.round(event.values[0]);

            // Actualizamos la brújula visual
            rotateCompass(degree);

            // Actualizamos los grados y la dirección cardinal en la UI
            degreesText.setText("Grados: " + degree + "°");
            cardinalDirectionText.setText("Dirección: " + getCardinalDirection(degree));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // Método para rotar la brújula
    private void rotateCompass(float degree) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                currentDegree, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(500);
        rotateAnimation.setFillAfter(true);
        compassImage.startAnimation(rotateAnimation);

        currentDegree = degree;
    }

    // Método para obtener la dirección cardinal basada en los grados
    private static String getCardinalDirection(float degree) {
        if (degree >= 337.5 || degree < 22.5) return "N";
        if (degree >= 22.5 && degree < 67.5) return "NE";
        if (degree >= 67.5 && degree < 112.5) return "E";
        if (degree >= 112.5 && degree < 157.5) return "SE";
        if (degree >= 157.5 && degree < 202.5) return "S";
        if (degree >= 202.5 && degree < 247.5) return "SO";
        if (degree >= 247.5 && degree < 292.5) return "O";
        if (degree >= 292.5 && degree < 337.5) return "NO";
        return "";
    }

    // Tarea asíncrona para guardar los datos en CSV
    private static class Medidor extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                while (true) {
                    Thread.sleep(10000); // Espera 10 segundos

                    // Obtener la hora actual
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);

                    // Obtener la dirección cardinal actual
                    String cardinalDirection = getCardinalDirection(currentDegree);

                    // Guardar los datos en un archivo CSV
                    saveDataToFile(cardinalDirection, hour, minute, second);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Método para guardar datos en un archivo CSV
        private void saveDataToFile(String cardinalDirection, int hour, int minute, int second) {
            File file = new File(Environment.getExternalStorageDirectory(), "sensor_data.csv");

            try (FileOutputStream fos = new FileOutputStream(file, true);
                 OutputStreamWriter writer = new OutputStreamWriter(fos)) {
                String data = cardinalDirection + "@" + String.format("%02d:%02d:%02d\n", hour, minute, second);
                writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}