package iesmm.pmdm.pmdm_t4_users;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.maps_activity);


        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            username = parametros.getString("username");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        cargarUsuariosDesdeCSV();
    }

    private void cargarUsuariosDesdeCSV() {
        try {
            InputStream inputStream = openFileInput("users.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String linea;

            while ((linea = reader.readLine()) != null) {
                Log.d("CSV", "Línea leída: " + linea);
                String[] datos = linea.split(";");
                if (datos.length >= 5) {
                    String nombreUsuario = datos[0];
                    String emailUsuario = datos[2];
                    String telefonoUsuario = datos[3];
                    String direccionUsuario = datos[4];

                    LatLng coordenadas = obtenerCoordenadasDesdeDireccion(direccionUsuario);
                    if (coordenadas != null) {
                        if (emailUsuario.equals(username)) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(coordenadas)
                                    .title(nombreUsuario)
                                    .snippet("Email: " + emailUsuario + "\nTel: " + telefonoUsuario)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 12));
                        } else {
                            mMap.addMarker(new MarkerOptions()
                                    .position(coordenadas)
                                    .title(nombreUsuario)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            mostrarError("Error al leer el archivo CSV: " + e.getMessage());
            Log.e("CSV", "Error al leer CSV", e);
        }
    }

    private LatLng obtenerCoordenadasDesdeDireccion(String direccion) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocationName(direccion, 1);
            if (!direcciones.isEmpty()) {
                Address ubicacion = direcciones.get(0);
                return new LatLng(ubicacion.getLatitude(), ubicacion.getLongitude());
            }
        } catch (IOException e) {
            mostrarError("Error al obtener coordenadas para: " + direccion);
        }
        return null;
    }

    private void mostrarError(String mensaje) {
        runOnUiThread(() -> Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show());
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Toast.makeText(this, marker.getSnippet(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Toast.makeText(this, latLng.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
