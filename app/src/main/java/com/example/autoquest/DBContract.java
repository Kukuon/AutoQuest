package com.example.autoquest;

import android.provider.BaseColumns;

public class DBContract {
    private DBContract() {
    }
    public static class Car implements BaseColumns {
        public static final String TABLE_NAME = "car";
        public static final String COLUMN_BRAND = "brand";
        public static final String COLUMN_MODEL = "model";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_POWER = "power";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BRAND + " TEXT, " +
                COLUMN_MODEL + " TEXT, " +
                COLUMN_YEAR + " TEXT, " +
                COLUMN_PRICE + " INTEGER, " +
                COLUMN_POWER + " TEXT" + ")";

    }
}
