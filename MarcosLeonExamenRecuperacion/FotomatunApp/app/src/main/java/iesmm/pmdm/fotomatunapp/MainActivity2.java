package iesmm.pmdm.fotomatunapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity{
    private Vibrator vibrator;
    private ImageView imageView;
    private MediaPlayer backgroundMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        int cont =0;
        imageView = this.findViewById(R.id.image);

        //Recuperar datos del bundle
        Bundle parametros = this.getIntent().getExtras();

        iniciarMusicaFondo();

        while (true){
            try {
                wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            vibrator.vibrate(100);
            for (int i = 0; i<=10; i++) {
                cont++;
                setImage(imageView, "admin" + cont);
            }
        }
    }


    //Modifica al contenido de un imageView a partir del nombre de un drawable asset existente
    void setImage(ImageView imageView, String imageNameRes){
        int resId = this.getResources().getIdentifier(imageNameRes,"drawable",this.getPackageName());
        if (resId != 0) {
            imageView.setImageResource(resId);
        }

    }

    //Aleatorio entre min y max
    public int aleatorio(int min,int max){
        return min + new Random().nextInt(max - min + 1);
    }

    private void iniciarMusicaFondo() {
        backgroundMusic = MediaPlayer.create(this, R.raw.music);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (backgroundMusic != null) {
            backgroundMusic = MediaPlayer.create(this, R.raw.finish);
            backgroundMusic.setLooping(true);
            backgroundMusic.start();
        }

        setImage(imageView, "finish" );
    }
}
