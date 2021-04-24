package com.example.blackburninventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import Model.Items;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name
    public static final String DATABASE_NAME = "inventory.db";

    // Table for user names and passwords
    public static final String TABLE_USERS = "users";
    // Columns for table
    public static final String COL_ID_USERS = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Table for items and quantity
    public static final String TABLE_INVENTORY = "inventory";
    // Columns for table
    public static final String COL_ID_INVENTORY = "id";
    public static final String COL_USERNAME_INV = "username";
    public static final String COL_ITEM = "item";
    public static final String COL_QUANTITY = "quantity";

    // Create users table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE "
            + TABLE_USERS + " (" + COL_ID_USERS + " INTEGER PRIMARY KEY, "
            + COL_USERNAME + " TEXT unique,"
            + COL_PASSWORD + " TEXT" + ") ";

    //Create inventory table
    private static final String CREATE_TABLE_INVENTORY = "CREATE TABLE "
            + TABLE_INVENTORY + " (" + COL_ID_INVENTORY + " INTEGER PRIMARY KEY, "
            + COL_USERNAME_INV + " TEXT,"
            + COL_ITEM + " TEXT,"
            + COL_QUANTITY + " TEXT" + ") ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_INVENTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVENTORY);
        onCreate(db);
    }

    public boolean createUsers(String user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, user);
        contentValues.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, contentValues);

        return result != -1;
    }

    // Check if user exists in database
    public boolean checkUser(String user, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "Select * from " + TABLE_USERS + " where " + COL_USERNAME + " = " + "'" + user + '\'' + " and " + COL_PASSWORD + " = " + "'" + pass + '\'';
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    // Insert a new item into the database
    public boolean createItem(String username, String item, String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME_INV, username);
        contentValues.put(COL_ITEM, item);
        contentValues.put(COL_QUANTITY, quantity);

        long result = db.insert(TABLE_INVENTORY, null, contentValues);

        return result != -1;
    }

    // update an item in the database
    public boolean updateItem(String item, String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_INVENTORY + " SET " + COL_QUANTITY + " = " + "'" + quantity + '\'' + " WHERE " + COL_ITEM + " = " + "'" + item + '\'');
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    // Delete and item in the database
    public boolean deleteItem(String item) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_INVENTORY, COL_ITEM + " = " + '"' + item + '"', null) > 0;
    }

    // Loads unique user data from data base into array
    public ArrayList<Items> getAllData() {
        String user = MainActivity.returnUserName();
        ArrayList<Items> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        // SQLite query to return unique data for each user
        Cursor cursor = db.rawQuery("SELECT * FROM inventory WHERE username =" + "'" + user + "'", null);

        while (cursor.moveToNext()) {

            String item = cursor.getString(2);
            String quantity = cursor.getString(3);
            Items items = new Items(item, quantity);

            arrayList.add(items);
        }
        cursor.close();

        return arrayList;
    }
}
