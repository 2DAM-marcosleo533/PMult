package iesmm.pmdm.pmdm_t4_animaciones;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private ImageView pokemon;
private Animation animacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Comienzo de la animacion
        pokemon = this.findViewById(R.id.pokemon);
        animacion = AnimationUtils.loadAnimation(this, R.anim.jump_and_animation);
        pokemon.startAnimation(animacion);

        pokemon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        animacion = AnimationUtils.loadAnimation(this, R.anim.jump);
        pokemon.startAnimation(animacion);
    }
}
