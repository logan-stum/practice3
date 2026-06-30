package com.example.practice3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.practice3.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages creation of the SQLite database and access to the "products" table.
 * On first creation the database is pre-populated with sample products so the
 * app has data to show immediately (no separate "add product" UI is required
 * by the assignment).
 */
public class DBHelper extends SQLiteOpenHelper {

    /** Name of the database file. */
    private static final String DATABASE_NAME = "products.db";
    /** Version number of the database schema. */
    private static final int DATABASE_VERSION = 3;

    /** Name of the table storing product data. */
    public static final String TABLE_PRODUCTS = "products";
    /** Column name for the product ID. */
    public static final String COL_ID = "id";
    /** Column name for the product name. */
    public static final String COL_NAME = "name";
    /** Column name for the product description. */
    public static final String COL_DESCRIPTION = "description";
    /** Column name for the product seller. */
    public static final String COL_SELLER = "seller";
    /** Column name for the product price. */
    public static final String COL_PRICE = "price";
    /** Column name for the product image resource name. */
    public static final String COL_IMAGE = "image";

    /**
     * Constructs a new DBHelper.
     * @param context The application context.
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_SELLER + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_IMAGE + " TEXT)";
        db.execSQL(createTable);
        seedData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    /**
     * Inserts a handful of sample rows the first time the DB is created.
     * @param db The SQLite database instance.
     */
    private void seedData(SQLiteDatabase db) {
        Object[][] sample = {
                {"Wireless Mouse", "Ergonomic 2.4GHz wireless mouse", "TechWorld", 19.99, "wireless_mouse"},
                {"Mechanical Keyboard", "RGB backlit mechanical keyboard", "TechWorld", 59.99, "mechanical_keyboard"},
                {"Running Shoes", "Lightweight breathable running shoes", "SportZone", 74.50, "running_shoes"},
                {"Yoga Mat", "Non-slip eco-friendly yoga mat", "SportZone", 24.00, "yoga_mat"},
                {"Coffee Maker", "12-cup programmable coffee maker", "HomeEssentials", 39.99, "coffee_maker"},
                {"Blender", "High-speed countertop blender", "HomeEssentials", 49.99, "blender"},
                {"Backpack", "Water-resistant 30L travel backpack", "TravelGear", 44.95, "backpack"},
                {"Desk Lamp", "LED desk lamp with adjustable brightness", "HomeEssentials", 17.25, "desk_lamp"}
        };

        for (Object[] row : sample) {
            ContentValues values = new ContentValues();
            values.put(COL_NAME, (String) row[0]);
            values.put(COL_DESCRIPTION, (String) row[1]);
            values.put(COL_SELLER, (String) row[2]);
            values.put(COL_PRICE, (Double) row[3]);
            values.put(COL_IMAGE, (String) row[4]);
            db.insert(TABLE_PRODUCTS, null, values);
        }
    }

    /** 
     * Returns every product currently stored in the database. 
     * @return A list of Product objects.
     */
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, COL_ID + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Product p = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_SELLER)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE))
                );
                products.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }
}
