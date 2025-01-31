package iesmm.pmdm.pmdm_rec_tienda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button b = findViewById(R.id.boton_iniciar_sesion);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = ((EditText) findViewById(R.id.input_usuario)).getText().toString();
                String password = ((EditText) findViewById(R.id.input_contrasena)).getText().toString();

                if (getAccess(username, password)) {
                    Snackbar.make(view, "Ha accedido " + username + ":" + password, Snackbar.LENGTH_LONG).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);

                    // Crear un intent para la segunda actividad
                    Intent intent = new Intent(getApplicationContext(), BankActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "Credenciales incorrectas", Snackbar.LENGTH_LONG).show();
                }
            }

            private boolean getAccess(String username, String password) {
                FileInputStream inputStream = null;
                BufferedReader reader = null;
                boolean accesoConcedido = false;

                try {
                    // Abrir el archivo users.csv del almacenamiento interno
                    inputStream = openFileInput("bankproducts.csv");
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String linea;

                    // Leer cada línea del archivo y comprobar las credenciales
                    while ((linea = reader.readLine()) != null) {
                        // Dividir la línea por ":"
                        String[] userDetails = linea.split(":");
                        if (userDetails.length >= 2 && userDetails[0].equals(username) && userDetails[1].equals(password)) {
                            accesoConcedido = true;
                            break;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return accesoConcedido;
            }
        });
    }
}
