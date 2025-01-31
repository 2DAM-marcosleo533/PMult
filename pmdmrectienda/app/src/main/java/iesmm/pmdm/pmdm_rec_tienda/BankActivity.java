package iesmm.pmdm.pmdm_rec_tienda;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BankActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> productList = new ArrayList<>();
    private List<String> productUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_products);

        listView = findViewById(R.id.listView);
        String username = getIntent().getStringExtra("username");

        productosUsuarios(username);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> showProductDialog(position));
    }

    private void productosUsuarios(String username) {
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = openFileInput("bankproducts.csv");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 4 && parts[0].equals(username)) {
                    productList.add(formatoIBAN(parts[2]));
                    productUrls.add(parts[3].replace("\"", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String formatoIBAN(String iban) {
        return iban.substring(0, 2) + " " + iban.substring(2, 4) + " " + iban.substring(4, 8) + " " +
                iban.substring(8, 12) + " " + iban.substring(12, 14) + " " + iban.substring(14);
    }

    private void showProductDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalles del Producto");
        builder.setMessage("¿Acceder a la URL del producto?");
        builder.setPositiveButton("Abrir", (dialog, which) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(productUrls.get(position)));
            startActivity(browserIntent);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}