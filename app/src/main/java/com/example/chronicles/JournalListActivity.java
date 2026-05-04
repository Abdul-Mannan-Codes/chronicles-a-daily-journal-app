package com.example.chronicles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class JournalListActivity extends AppCompatActivity {
    ListView listViewJournals;
    DatabaseHelper dbHelper;
    ArrayList<String> journalList;
    ArrayList<String> journalDates;  // To store dates of journals
    ArrayAdapter<String> adapter;
    int userId;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        sharedPreferences = getSharedPreferences("UserPrefs",MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);

        listViewJournals = findViewById(R.id.listViewJournals);
        dbHelper = new DatabaseHelper(this);
        journalList = new ArrayList<>();
        journalDates = new ArrayList<>(); // Initialize dates list

        loadJournals();

        listViewJournals.setOnItemClickListener((parent, view, position, id) -> {
            String selectedJournal = journalList.get(position);
            String selectedDate = journalDates.get(position);  // Get the date from journalDates

            // Pass both title and date to the EditJournalActivity
            Intent intent = new Intent(JournalListActivity.this, EditJournalActivity.class);
            intent.putExtra("journalTitle", selectedJournal);
            intent.putExtra("selectedDate", selectedDate);  // Pass selectedDate here
            startActivity(intent);
        });
    }

    private void loadJournals() {
        journalList.clear();
        journalDates.clear();

        String startDate = getIntent().getStringExtra("startDate");
        String endDate = getIntent().getStringExtra("endDate");

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        if (startDate != null && endDate != null) {
            // Filter journals within the date range
            cursor = db.rawQuery(
                    "SELECT title, date FROM journal WHERE user_id = ? AND date BETWEEN ? AND ?",
                    new String[]{String.valueOf(userId), startDate, endDate}
            );
        } else {
            // Load all journals
            cursor = db.rawQuery(
                    "SELECT title, date FROM journal WHERE user_id = ?",
                    new String[]{String.valueOf(userId)}
            );
        }

        if (cursor.moveToFirst()) {
            do {
                journalList.add(cursor.getString(0));  // title
                journalDates.add(cursor.getString(1)); // date
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No journals found!", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, journalList);
        listViewJournals.setAdapter(adapter);
    }

}
