package com.example.blackburninventoryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.regex.Pattern;

import Adapter.MyAdapter;
import Model.Items;

public class MainActivity2 extends AppCompatActivity {

    DatabaseHelper db;
    boolean notificationsOn;
    ArrayList<Items> arrayList;
    MyAdapter myAdapter;
    private NotificationManagerCompat notificationManager;
    private ListView listView;
    private EditText editItem, setQuantity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button buttonAdd = findViewById(R.id.buttonAdd);
        final String userId = MainActivity.returnUserName();
        editItem = findViewById(R.id.editTextTextItem);
        setQuantity = findViewById(R.id.editTextTextQuantity);
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        Switch notifications = findViewById(R.id.smsSwitch);
        notificationManager = NotificationManagerCompat.from(this);
        db = new DatabaseHelper(this);

        loadDataInListView();

        // Checks if SMS notifications button is on or off
        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notificationsOn = b;
                loadDataInListView();
            }
        });

        // Add button listener
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    // Save data to database
                    boolean insert = db.createItem(userId, editItem.getText().toString().trim(), setQuantity.getText().toString().trim());
                    if (insert) {
                        Toast.makeText(getApplicationContext(), "Successfully Added Item", Toast.LENGTH_SHORT).show();

                        if ((Integer.parseInt(setQuantity.getText().toString()) == 0 && notificationsOn)) {
                            sendOnChannel1(view);
                        }

                        editItem.getText().clear();
                        setQuantity.getText().clear();
                        loadDataInListView();

                    } else {
                        Toast.makeText(getApplicationContext(), "Item Already Exists", Toast.LENGTH_SHORT).show();
                        editItem.getText().clear();
                        setQuantity.getText().clear();
                    }
                }
            }
        });
    }

    // Display SMS message
    public void sendOnChannel1(View v) {
        android.app.Notification notification = new NotificationCompat.Builder(this, Notification.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Item has reached zero")
                .setContentText("Please order more: " + editItem.getText().toString().trim())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    public void loadDataInListView() {
        arrayList = db.getAllData();
        myAdapter = new MyAdapter(this, arrayList, notificationsOn);
        listView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    // Validation method to check if fields are empty
    private Boolean validate() {
        boolean result = false;

        String item = editItem.getText().toString().trim();
        String quantity = setQuantity.getText().toString().trim();
        boolean isNumber = Pattern.matches("[0-9]+", quantity);

        if (item.isEmpty() && quantity.isEmpty()) {
            Toast.makeText(this, "Both Fields Are Empty", Toast.LENGTH_SHORT).show();
        } else if (item.isEmpty()) {
            Toast.makeText(this, "Please Add An Item", Toast.LENGTH_SHORT).show();

        } else if (!isNumber) {
            Toast.makeText(this, "Enter A Number", Toast.LENGTH_SHORT).show();

        } else {
            result = true;
        }
        return result;
    }
}
