package iesmm.pmdm.pmdm_ej1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText numeroEditText;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numeroEditText = findViewById(R.id.numero);
        startButton = findViewById(R.id.button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el número de segundos desde el EditText
                String secondsString = numeroEditText.getText().toString();
                int seconds = Integer.parseInt(secondsString);

                // Crea un intent para pasar los datos a la siguiente actividad
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("seconds", seconds);
                startActivity(intent);
            }
        });
    }
}
