package iesmm.pmdm.t4_animaciones_02;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Animation.AnimationListener {
    private Animation animacion;
    private AnimationDrawable sprites;
    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vinculación de escuchadores a componentes
        imagen = (ImageView) findViewById(R.id.imageView);
        imagen.setOnClickListener(this);

        Button boton = (Button) findViewById(R.id.boton);
        boton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageView) {
            loadAnimation(view, R.anim.rotacion);
        } else if (view.getId() == R.id.boton) {
            if (sprites != null && sprites.isRunning())
                sprites.stop();
            else {
                // 1. Defino y vinculo al componente el drawable de tipo animation_list
                imagen.setBackgroundResource(R.drawable.sprites);

                // 2. Obtengo la referencia del conjunto de animation_list
                sprites = (AnimationDrawable) imagen.getBackground();

                // 3. Comienzo de la animación
                sprites.start();
            }

        }
    }

    /* Dado un componente y un recurso de animación, genera y comienza una animación */
    private void loadAnimation(View view, int res) {
        // 1. Carga de una nueva animación
        animacion = AnimationUtils.loadAnimation(this, res);
        // 2. Vinculación del escuchador de los estados de la animación
        animacion.setAnimationListener(this);
        // 3. Comienzo de la animación
        view.startAnimation(animacion);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Toast.makeText(this, "Empieza la animación", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Toast.makeText(this, "Finalizó la animación", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}