package iesmm.pmdm.integracionfirebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DelanteroDAO {
    private static FirebaseFirestore db;

    public DelanteroDAO() {
        db = FirebaseFirestore.getInstance();
    }

    public static Task<QuerySnapshot> getAllDelanteros() {
        return db.collection("delanteros").get();
    }
    public static Task<DocumentReference> addDelantero(Delantero delantero) {
        return db.collection("delanteros").add(delantero);
    }

    public static Task<Void> updateDelantero(String id, Delantero delantero) {
        return db.collection("delanteros").document(id).set(delantero);
    }

    public static Task<Void> deleteDelantero(String id) {
        return db.collection("delanteros").document(id).delete();
    }

}