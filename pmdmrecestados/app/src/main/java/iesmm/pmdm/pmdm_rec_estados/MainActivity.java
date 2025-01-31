package iesmm.pmdm.pmdm_rec_estados;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TableLayout tablaLayout;
    private SharedPreferences preferenciasCompartidas;
    private static final String NOMBRE_PREFERENCIAS = "AppStatesPrefs";
    private static final String CLAVE_ESTADOS = "States";
    private final SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tablaLayout = findViewById(R.id.tableLayout);
        preferenciasCompartidas = getSharedPreferences(NOMBRE_PREFERENCIAS, MODE_PRIVATE);

        // Cargamos los estados guardados cuando la actividad se inicia
        cargarEstados();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registrarEstado(getString(R.string.iniciar));
    }

    @Override
    protected void onPause() {
        super.onPause();
        registrarEstado(getString(R.string.pausar));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registrarEstado(getString(R.string.reiniciar));
    }

    @Override
    protected void onStop() {
        super.onStop();
        registrarEstado(getString(R.string.finalizar));
    }


    private void registrarEstado(String estado) {
        try {
            // Recuperamos los estados guardados en SharedPreferences
            JSONArray arrayEstados = new JSONArray(preferenciasCompartidas.getString(CLAVE_ESTADOS, "[]"));

            // Obtenemos la hora actual
            String horaActual = formatoHora.format(new Date());

            boolean estadoExistente = false;

            // Verificamos si el estado ya existe en la lista
            for (int i = 0; i < arrayEstados.length(); i++) {
                JSONObject estadoObj = arrayEstados.getJSONObject(i);
                if (estadoObj.getString("estado").equals(estado)) {
                    // Si el estado existe, incrementamos el contador de veces
                    estadoObj.put("veces", estadoObj.getInt("veces") + 1);
                    estadoObj.put("hora", horaActual);
                    estadoExistente = true;
                    break;
                }
            }

            // Si el estado no existe, lo agregamos a la lista
            if (!estadoExistente) {
                JSONObject nuevoEstado = new JSONObject();
                nuevoEstado.put("estado", estado);
                nuevoEstado.put("veces", 1);
                nuevoEstado.put("hora", horaActual);
                arrayEstados.put(nuevoEstado);
            }

            // Guardamos los estados actualizados en SharedPreferences
            preferenciasCompartidas.edit().putString(CLAVE_ESTADOS, arrayEstados.toString()).apply();


            actualizarTabla(arrayEstados);
            Toast.makeText(this, String.format(getString(R.string.estado_registrado), estado), Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Log.e("AppStates", "Error al registrar estado", e);
            Toast.makeText(this, getString(R.string.error_agregar), Toast.LENGTH_SHORT).show();
        }
    }


    private void cargarEstados() {
        try {
            JSONArray arrayEstados = new JSONArray(preferenciasCompartidas.getString(CLAVE_ESTADOS, "[]"));
            actualizarTabla(arrayEstados);
        } catch (JSONException e) {
            Log.e("AppStates", "Error al cargar estados", e);
            Toast.makeText(this, getString(R.string.error_cargar), Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarTabla(JSONArray arrayEstados) {
        tablaLayout.removeAllViews();

        // Agregamos una fila de encabezado
        TableRow filaEncabezado = new TableRow(this);
        agregarCeldaAFila(filaEncabezado, getString(R.string.estado));
        agregarCeldaAFila(filaEncabezado, getString(R.string.num_veces));
        agregarCeldaAFila(filaEncabezado, getString(R.string.hora));
        tablaLayout.addView(filaEncabezado);

        // Agregamos una fila por cada estado guardado
        for (int i = 0; i < arrayEstados.length(); i++) {
            try {
                JSONObject estadoObj = arrayEstados.getJSONObject(i);
                TableRow fila = new TableRow(this);
                agregarCeldaAFila(fila, estadoObj.getString("estado"));
                agregarCeldaAFila(fila, String.valueOf(estadoObj.getInt("veces")));
                agregarCeldaAFila(fila, estadoObj.getString("hora"));
                tablaLayout.addView(fila);
            } catch (JSONException e) {
                Log.e("AppStates", "Error al actualizar la tabla", e);
            }
        }
    }

    //Agregamos celda por fila
    private void agregarCeldaAFila(TableRow fila, String texto) {
        TextView textView = new TextView(this);
        textView.setText(texto);
        textView.setPadding(16, 16, 16, 16);

        fila.addView(textView);
    }
}
