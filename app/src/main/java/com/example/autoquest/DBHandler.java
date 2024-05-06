package com.example.autoquest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Columns of table
    private static final String DATABASE_NAME = "cars_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "cars_table";
    private static final String ID_COLUMN = "id";
    private static final String BRAND_COLUMN = "brand";
    private static final String MODEL_COLUMN = "model";
    private static final String YEAR_COLUMN = "year";
    private static final String POWER_COLUMN = "power";
    private static final String PRICE_COLUMN = "price";

    // Constructor
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public int getCarCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM cars", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // Methods for creating and updating database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating table
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BRAND_COLUMN + " TEXT,"
                + MODEL_COLUMN + " TEXT,"
                + YEAR_COLUMN + " INTEGER,"
                + POWER_COLUMN + " INTEGER,"
                + PRICE_COLUMN + " INTEGER)";
        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database updating
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add new car record into database
    public void addNewCar(String brand, String model, int year, int power, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(BRAND_COLUMN, brand);
        values.put(MODEL_COLUMN, model);
        values.put(YEAR_COLUMN, year);
        values.put(POWER_COLUMN, power);
        values.put(PRICE_COLUMN, price);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public void deleteCar(String brand) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "brand=?", new String[]{brand});
        db.close();
    }

    public ArrayList<Car> readCars() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<Car> arrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                arrayList.add(new Car(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getInt(5)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return arrayList;
    }

}

