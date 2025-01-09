package iesmm.pmdm.fotomatunapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class MainActivity extends AppCompatActivity {

    Button b = findViewById(R.id.button);
    int intentos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);




        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = ((EditText) findViewById(R.id.username)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();
                if (intentos>=3){
                    b.setEnabled(false);
                }

                if (getAccess(username, password)) {

                    Snackbar.make(view, "Ha accedido " + username + ":" + password, Snackbar.LENGTH_LONG).show();


                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);

                    // Crear un intent para la segunda actividad
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    intentos++;
                }
            }
        });


    }



    private boolean getAccess(String username, String password) {
        FileReader inputStream = this.openFileInput("users.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        Boolean booleano = false;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String linea;


            while ((linea = reader.readLine()) != null) {
                intentos++;
                String[] userDetails = linea.split(":");
                if (userDetails[0].equals(username) && userDetails[1].equals(password)) {
                    booleano = true;
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            booleano = false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return booleano;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}