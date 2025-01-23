package iesmm.pmdm.pmdm_t4_agenda;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contacts;
    private Context context;

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView contactName, contactPhone, contactEmail;
        public ImageView contactImage;

        public ContactViewHolder(View view) {
            super(view);
            contactName = view.findViewById(R.id.contactName);
            contactPhone = view.findViewById(R.id.contactPhone);
            contactEmail = view.findViewById(R.id.contactEmail);
            contactImage = view.findViewById(R.id.contactImage);

            view.setOnClickListener(v -> showContactDialog(contacts.get(getAdapterPosition())));
        }
    }

    public ContactAdapter(List<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.contactName.setText(contact.getName());
        holder.contactPhone.setText(contact.getPhone());
        holder.contactEmail.setText(contact.getEmail());


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private void showContactDialog(Contact contact) {
        String[] options = {"Llamar", "Enviar WhatsApp", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(contact.getName())
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            callContact(contact.getPhone());
                            break;
                        case 1:
                            sendWhatsApp(contact.getPhone());
                            break;
                        case 2:
                            break;
                    }
                });
        builder.show();
    }

    private void callContact(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    private void sendWhatsApp(String phone) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + phone));
        context.startActivity(intent);
    }
}

