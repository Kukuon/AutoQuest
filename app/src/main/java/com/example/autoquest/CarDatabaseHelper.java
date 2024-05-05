package com.example.autoquest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CarDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CarDatabase";
    private static final int DATABASE_VERSION = 1;

    public CarDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы Cars
        db.execSQL(DBContract.Car.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Обновление базы данных при изменении структуры
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Car.TABLE_NAME);
        onCreate(db);
    }
}

