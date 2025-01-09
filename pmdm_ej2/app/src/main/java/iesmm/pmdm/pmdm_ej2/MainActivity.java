package iesmm.pmdm.pmdm_ej2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView enfadado;
    private TextView relajado;
    private TextView frustrado;
    private TextView nervioso;
    private TextView tolerante;
    private Button boton;
    private TextView estado;
    private ImageView imagen;
    private TextView porcentaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        enfadado = findViewById(R.id.enfadado2);
        relajado = findViewById(R.id.relajado2);
        frustrado = findViewById(R.id.frustrado2);
        nervioso = findViewById(R.id.nervioso2);
        tolerante = findViewById(R.id.tolerante2);
        boton = findViewById(R.id.btn);
        estado = findViewById(R.id.estado);
        imagen = findViewById(R.id.imageView);
        porcentaje = findViewById(R.id.porcentaje);
    }

    private void changeColor(){
        generarNumero();
        if (aleatorio() <= 30) estado.setText("Triste");
        estado.setTextColor(0);
        if (aleatorio() > 30 && aleatorio() <= 60) estado.setText("Neutro");
        estado.setTextColor(1);
        if (aleatorio() > 60 && aleatorio() <= 100) estado.setText("Alterado");
        estado.setTextColor(2);
    }

    private void generarNumero(){

            boton.setOnClickListener(v ->  porcentaje.setText(aleatorio() + "%"));

        }


    public double aleatorio(){
        return new Random().nextDouble();
    }

    public double formatearNumero(double num){
        return Math.floor(num*100)/100;
    }
}