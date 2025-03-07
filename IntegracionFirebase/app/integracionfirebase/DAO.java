package iesmm.pmdm.integracionfirebase;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DAO {

    private FirebaseFirestore db;

    public DAO() {
        db = FirebaseFirestore.getInstance();
    }

    // Consultar todos los libros
    public void getAllDelanteros() {
        db.collection("delanteros")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Delantero> delanteros = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Delantero delantero = document.toObject(Delantero.class);
                            Delanteros.add(Delantero);
                        }
                        // Manejar la lista de libros
                        Log.d("Firestore", "Todos los libros: " +  delanteros);
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }

    // Consultar libros por criterio (ej. por género)
    public void getLibrosPorGenero(String genero) {
        db.collection("libros")
                .whereEqualTo("género", genero)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Libro> libros = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Libro libro = document.toObject(Libro.class);
                            libros.add(libro);
                        }
                        // Manejar la lista de libros
                        Log.d("Firestore", "Libros por género: " + libros);
                    } else {
                        Log.w("Firestore", "Error getting documents.", task.getException());
                    }
                });
    }
}
