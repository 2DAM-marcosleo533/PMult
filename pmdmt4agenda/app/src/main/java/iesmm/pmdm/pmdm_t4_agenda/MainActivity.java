package iesmm.pmdm.pmdm_t4_agenda;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Contact> contactList = readCSVFile(this);
        ContactAdapter contactAdapter = new ContactAdapter(contactList, this);
        recyclerView.setAdapter(contactAdapter);
    }

    private void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        builder.show();
    }


    public List<Contact> readCSVFile(Context context) {
        List<Contact> contactList = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir(), "contactos.csv");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 3) {
                        Contact contact = new Contact(parts[0], parts[1], parts[2]);
                        contactList.add(contact);
                    }
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showDialog(context, "Error", "No se pudo leer el archivo CSV.");
        }
        return contactList;
    }

}
