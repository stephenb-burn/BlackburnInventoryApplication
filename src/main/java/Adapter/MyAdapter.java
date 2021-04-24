package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.blackburninventoryapp.DatabaseHelper;
import com.example.blackburninventoryapp.Notification;
import com.example.blackburninventoryapp.R;

import java.util.ArrayList;

import Model.Items;

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<Items> arrayList;
    DatabaseHelper db;
    boolean onOrOff;
    private TextView item;
    private NotificationManagerCompat notificationManager;


    public MyAdapter(Context context, ArrayList<Items> arrayList, boolean onOrOff) {
        this.context = context;
        this.arrayList = arrayList;
        this.onOrOff = onOrOff;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.mycustomlistview, null);

        final EditText t2_quantity = convertView.findViewById(R.id.quantity_txt);
        final TextView t1_item = convertView.findViewById(R.id.item_txt);
        Button buttonSave = convertView.findViewById(R.id.buttonSave);
        Button buttonX = convertView.findViewById(R.id.buttonX);
        notificationManager = NotificationManagerCompat.from(context);

        db = new DatabaseHelper(context);

        Items items = arrayList.get(position);

        t1_item.setText(items.getItems());
        t2_quantity.setText(items.getQuantity());

        // Save button listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean update = false;
                boolean checkIfEmpty = t2_quantity.getText().toString().trim().isEmpty();
                boolean checkIfZero = false;

                // Check if quantity field is empty
                if (!checkIfEmpty) {
                    update = db.updateItem(t1_item.getText().toString(), t2_quantity.getText().toString());
                    item = t1_item;
                    // Check if quantity is zero
                    if (Integer.parseInt(t2_quantity.getText().toString().trim()) == 0) {
                        checkIfZero = true;
                    }
                }
                // if update is successful display message and update list
                if (update) {
                    // Activate SMS notification if SMS switch is on and quantity is 0
                    if (checkIfZero && onOrOff) {
                        sendOnChannel1(view);
                    }
                    Toast.makeText(context.getApplicationContext(), "Quantity of " + t1_item.getText() + " updated", Toast.LENGTH_SHORT).show();
                    arrayList = db.getAllData();
                    new MyAdapter(context, arrayList, onOrOff);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context.getApplicationContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener for delete button
        buttonX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean delete = db.deleteItem(t1_item.getText().toString());
                if (delete) {
                    Toast.makeText(context.getApplicationContext(), "Item " + t1_item.getText() + " has been deleted", Toast.LENGTH_SHORT).show();
                    arrayList.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context.getApplicationContext(), "Could not delete " + t1_item.getText() + " has been deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    // Display SMS message
    public void sendOnChannel1(View v) {
        android.app.Notification notification = new NotificationCompat.Builder(context, Notification.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Item has reached zero")
                .setContentText("Please order more: " + item.getText().toString().trim())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }
}
