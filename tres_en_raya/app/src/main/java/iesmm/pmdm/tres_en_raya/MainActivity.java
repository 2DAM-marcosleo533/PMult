package iesmm.pmdm.tres_en_raya;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private JuegoTresEnRaya miJuego;
    private Button[] mBotonesTablero;
    private TextView mInfoTexto, mPlayerScore, mComputerScore, mTieScore;
    private Button resetButton;
    private int jugadorScore = 0, maquinaScore = 0, empates = 0;
    private boolean gameOver = false;
    private MediaPlayer backgroundMusic;
    private MediaPlayer effectSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBotonesTablero = new Button[JuegoTresEnRaya.DIM_TABLERO];
        for (int i = 0; i < mBotonesTablero.length; i++) {
            String buttonID = "button_" + (i + 1);
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            mBotonesTablero[i] = findViewById(resID);
            mBotonesTablero[i].setOnClickListener(this::onClick);
        }

        //  los TextViews
        mInfoTexto = findViewById(R.id.information);
        mPlayerScore = findViewById(R.id.player_score);
        mComputerScore = findViewById(R.id.computer_score);
        mTieScore = findViewById(R.id.tie_score);

        //  botón de reinicio
        resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(view -> reiniciarJuego());

        // Inicializar el juego
        miJuego = new JuegoTresEnRaya();
        comenzarJuego();

        // Iniciar la música de fondo
        iniciarMusicaFondo();
    }

    private void comenzarJuego() {
        miJuego.limpiarTablero();

        for (Button button : mBotonesTablero) {
            button.setText("");
            button.setEnabled(true);
        }
        gameOver = false;
        mInfoTexto.setText(R.string.turno_jugador);
    }

    private void reiniciarJuego() {
        comenzarJuego();
        mInfoTexto.setText(R.string.turno_jugador);
    }

    // manejar clics en los botones del tablero
    public void onClick(View view) {
        if (gameOver) return;

        Button boton = (Button) view;
        int casilla = Integer.parseInt(boton.getContentDescription().toString()) - 1;

        if (miJuego.moverFicha(JuegoTresEnRaya.JUGADOR, casilla)) {
            boton.setText(String.valueOf(JuegoTresEnRaya.JUGADOR));
            boton.setEnabled(false);

            //  efecto de sonido al colocar X
            reproducirEfectoSonido();


            if (comprobarEstadoJuego()) return;


            if (!gameOver) {
                int casillaMaquina = miJuego.getMovimientoMaquina();
                if (casillaMaquina != -1) {
                    mBotonesTablero[casillaMaquina].setText(String.valueOf(JuegoTresEnRaya.MAQUINA));
                    mBotonesTablero[casillaMaquina].setEnabled(false);
                    comprobarEstadoJuego();
                }
              }
        }
    }

    private boolean comprobarEstadoJuego() {

        int estadoJuego = miJuego.comprobarGanador();

        if (estadoJuego == 1) {
            mInfoTexto.setText(R.string.result_human_wins);
            jugadorScore++;
            mPlayerScore.setText(String.valueOf(jugadorScore));
            gameOver = true;
        } else if (estadoJuego == 2) {
            mInfoTexto.setText(R.string.result_computer_wins);
            maquinaScore++;
            mComputerScore.setText(String.valueOf(maquinaScore));
            gameOver = true;
        } else if (estadoJuego == -1) {
            mInfoTexto.setText(R.string.result_tie);
            empates++;
            mTieScore.setText(String.valueOf(empates));
            gameOver = true;
        }
        return gameOver;
    }

    // reproducir la música de fondo
    private void iniciarMusicaFondo() {
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music);
        backgroundMusic.setLooping(true); // Para que la música se repita
        backgroundMusic.start();
    }

    //reproducir el efecto de sonido
    private void reproducirEfectoSonido() {
        if (effectSound != null) {
            effectSound.release();
        }
        effectSound = MediaPlayer.create(this, R.raw.effect);
        effectSound.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundMusic != null) {
            backgroundMusic.release();
        }
        if (effectSound != null) {
            effectSound.release();
        }
    }
}
