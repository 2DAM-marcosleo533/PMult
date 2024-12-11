package iesmm.pmdm.simonapp;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int greenOriginalColor, redOriginalColor, blueOriginalColor, yellowOriginalColor;
    private Button btnGreen, btnRed, btnBlue, btnYellow;
    private TextView scoreText, statusText;
    private ArrayList<Integer> sequence = new ArrayList<>();
    private ArrayList<Integer> userSequence = new ArrayList<>();
    private int score = 0;
    private Vibrator vibrator;
    private TextToSpeech textToSpeech;
    private boolean isUserTurn = false;
    private static final int countt = 10;
    private Handler handler = new Handler();
    private Runnable timeOutRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar botones
        btnGreen = findViewById(R.id.btnGreen);
        btnRed = findViewById(R.id.btnRed);
        btnBlue = findViewById(R.id.btnBlue);
        btnYellow = findViewById(R.id.btnYellow);

        // Inicializar colores
        greenOriginalColor = getResources().getColor(android.R.color.holo_green_light);
        redOriginalColor = getResources().getColor(android.R.color.holo_red_light);
        blueOriginalColor = getResources().getColor(android.R.color.holo_blue_light);
        yellowOriginalColor = getResources().getColor(android.R.color.holo_orange_light);

        // Inicializar textos y vibrador
        scoreText = findViewById(R.id.scoreText);
        statusText = findViewById(R.id.statusText);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.getDefault());
            }
        });

        // Iniciamos el juego
        startGame();

        // Eventos de click
        setButtonListeners();
    }

    private void startGame() {
        sequence.clear();
        userSequence.clear();
        score = 0;
        scoreText.setText("Puntuacion: " + score);
        addNewToSequence();
    }

    // Secuencia
    private void addNewToSequence() {
        Random random = new Random();
        sequence.add(random.nextInt(4)); // Añade un número aleatorio entre 0 y 3 (4 colores)
        statusText.setText("Secuencia: ");
        isUserTurn = false;

        // Mostramos la secuencia
        new ShowSequenceTask().execute(sequence.toArray(new Integer[0]));

        // Iniciamos el temporizador para que el usuario complete la secuencia
        startUserTurnTimer();
    }

    private void setButtonListeners() {
        btnGreen.setOnClickListener(v -> handleUserInput(0));
        btnRed.setOnClickListener(v -> handleUserInput(1));
        btnBlue.setOnClickListener(v -> handleUserInput(2));
        btnYellow.setOnClickListener(v -> handleUserInput(3));
    }

    private void handleUserInput(int color) {
        if (!isUserTurn) return;

        userSequence.add(color);
        vibrateAndSpeak(color);

        // Si la entrada es correcta o incorrecta, se sigue o se termina el juego
        if (userSequence.get(userSequence.size() - 1).equals(sequence.get(userSequence.size() - 1))) {
            if (userSequence.size() == sequence.size()) {
                score++;
                scoreText.setText("Puntuacion: " + score);
                userSequence.clear();
                addNewToSequence();  // Generamos una nueva secuencia
            }
        } else {
            statusText.setText("Has perdido! Puntuacion final: " + score);
            textToSpeech.speak("Game over", TextToSpeech.QUEUE_FLUSH, null, null);
            isUserTurn = false;

            // Ponemos retraso de 3 segundos antes de reiniciar el juego
            handler.postDelayed(this::startGame, 3000);  // 3000 ms = 3 segundos
        }
    }

    // Voz de los botones y vibración
    private void vibrateAndSpeak(int color) {
        if (vibrator != null) {
            vibrator.vibrate(100);
        }
        String colorName = "";
        switch (color) {
            case 0:
                colorName = "Green";
                break;
            case 1:
                colorName = "Red";
                break;
            case 2:
                colorName = "Blue";
                break;
            case 3:
                colorName = "Yellow";
                break;
        }
        textToSpeech.speak(colorName, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void highlightButton(int color) {
        final Button button;

        // Asignamos el botón basado en el color
        switch (color) {
            case 0:
                button = btnGreen;
                break;
            case 1:
                button = btnRed;
                break;
            case 2:
                button = btnBlue;
                break;
            case 3:
                button = btnYellow;
                break;
            default:
                return;
        }

        button.setBackgroundColor(Color.WHITE);

        // Ponemos el color original después de unos ms
        new Handler().postDelayed(() -> {
            switch (color) {
                case 0:
                    button.setBackgroundColor(greenOriginalColor);
                    break;
                case 1:
                    button.setBackgroundColor(redOriginalColor);
                    break;
                case 2:
                    button.setBackgroundColor(blueOriginalColor);
                    break;
                case 3:
                    button.setBackgroundColor(yellowOriginalColor);
                    break;
            }
        }, 500);
    }

    private void startUserTurnTimer() {
        userSequence.clear();
        handler.removeCallbacks(timeOutRunnable);  //

        // Configuramos el tiempo para que el usuario complete la secuencia
        timeOutRunnable = new Runnable() {
            @Override
            public void run() {
                startGame();  // Reiniciamos el juego si el tiempo se termina
            }
        };

        // 10 segundos para completar la secuencia
        handler.postDelayed(timeOutRunnable, countt * 1000);
    }

    private class ShowSequenceTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... colors) {
            try {
                for (int color : colors) {
                    runOnUiThread(() -> highlightButton(color));
                    Thread.sleep(1000);  // Esperamos 1 segundo entre cada color
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            statusText.setText("¡Tu turno!");
            isUserTurn = true;  // Activamos el turno del usuario
        }
    }

    // Finalizamos la actividad
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
