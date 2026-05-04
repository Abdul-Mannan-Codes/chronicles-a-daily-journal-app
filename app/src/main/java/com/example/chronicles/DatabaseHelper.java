package com.example.chronicles;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ChroniclesDB";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user_info (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, profile_image_path TEXT)");

        db.execSQL("CREATE TABLE journal (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, date TEXT, entry TEXT, title TEXT DEFAULT 'TITLE', is_favorite INTEGER, FOREIGN KEY (user_id) REFERENCES user_info(id))");

        db.execSQL("CREATE TABLE favorites (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, journal_id INTEGER, FOREIGN KEY (user_id) REFERENCES user_info(id), FOREIGN KEY (journal_id) REFERENCES journal(id))");

        db.execSQL("CREATE TABLE chronicles (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, name TEXT, start_date TEXT, end_date TEXT, is_favorite INTEGER DEFAULT 0, FOREIGN KEY (user_id) REFERENCES user_info(id))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user_info");
        db.execSQL("DROP TABLE IF EXISTS journal");
        db.execSQL("DROP TABLE IF EXISTS favorites");
        db.execSQL("DROP TABLE IF EXISTS chronicles");
        onCreate(db);
    }

    // Get Favorite Chronicles
    public ArrayList<String> getFavoriteChronicles(int userId) {
        ArrayList<String> favorites = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT entry FROM journal WHERE is_favorite = 1 AND user_id="+userId, null);

        if (cursor.moveToFirst()) {
            do {
                favorites.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return favorites;
    }
    public void addChronicle(int userId, String name, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO chronicles (user_id, name, start_date, end_date) VALUES (?, ?, ?, ?)",
                new Object[]{userId, name, startDate, endDate});
        db.close();
    }

}
