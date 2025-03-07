package iesmm.pmdm.integracionfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
            loadDelanteros();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadDelanteros() {
        DelanteroDAO.getAllDelanteros().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = 0;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    count++;
                }
                Log.d(TAG, "Total Delanteros: " + count);
            } else {
                Log.w(TAG, "Error", task.getException());
            }
        });
    }


    private void addDelantero(Delantero delantero) {
        DelanteroDAO.addDelantero(delantero).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Delantero añadido ID: " + task.getResult().getId());
            } else {
                Log.w(TAG, "Error", task.getException());
            }
        });
    }

    private void updateDelantero(String id, Delantero delantero) {
        DelanteroDAO.updateDelantero(id, delantero).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Delantero ediado ID: " + id);
            } else {
                Log.w(TAG, "Error", task.getException());
            }
        });
    }

    private void deleteDelantero(String id) {
        DelanteroDAO.deleteDelantero(id).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Delantero eliminado ID: " + id);
            } else {
                Log.w(TAG, "Error", task.getException());
            }
        });
    }
}