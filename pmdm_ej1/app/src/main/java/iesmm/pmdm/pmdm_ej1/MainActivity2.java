package iesmm.pmdm.pmdm_ej1;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private TextView cronometerTextView;
    private ImageView bombImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        cronometerTextView = findViewById(R.id.cronometer);
        bombImageView = findViewById(R.id.imageView);

        // Obtén el tiempo desde el intent
        int seconds = getIntent().getIntExtra("seconds", 0);

        // Configura el cronómetro con el tiempo obtenido
        new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Actualiza el cronómetro cada segundo
                int secondsRemaining = (int) millisUntilFinished / 1000;
                cronometerTextView.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                // Cuando el cronómetro termine, puedes hacer que la bomba explote o algo similar
                cronometerTextView.setText("BOOM!");
                bombImageView.setImageResource(R.drawable.explotion); // Suponiendo que tienes una imagen de explosión
            }
        }.start();
    }
}
