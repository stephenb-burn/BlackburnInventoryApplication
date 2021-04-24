package com.example.blackburninventoryapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    DatabaseHelper db;
    private Button buttonLogin, buttonCreateAccount;
    @SuppressLint("StaticFieldLeak")
    private static EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        setupUIViews();

        // Login button listener
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    //Check database
                    boolean check = db.checkUser(username.getText().toString().trim(), password.getText().toString().trim());
                    // Start next intent if credentials are in database
                    if (check) {
                        Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect Username Or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Create Account button listener
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    // Save data to database
                    boolean insert = db.createUsers(username.getText().toString().trim(), password.getText().toString().trim());
                    // Start next intent if credentials are successfully added to database
                    if (insert) {
                        Toast.makeText(getApplicationContext(), "Successfully Created Account", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Username Already Exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setupUIViews() {
        username = findViewById(R.id.editTextTextUserName);
        password = findViewById(R.id.editTextTextPassword);
        buttonLogin = findViewById(R.id.loginButton);
        buttonCreateAccount = findViewById(R.id.createAccountButton);
    }

    // Validation method to check if fields are empty
    private Boolean validate() {
        boolean result = false;

        String name = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (name.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }

    // Method to return username of current user
    public static String returnUserName() {
        return username.getText().toString().trim();
    }
}