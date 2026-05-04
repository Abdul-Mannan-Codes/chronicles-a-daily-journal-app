package com.example.chronicles;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditJournalActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    TextView tvJournalDate;
    EditText etJournalEntry;
    Button btnSaveJournal;
    DatabaseHelper dbHelper;
    String selectedDate;
    int userId;
    CheckBox cbFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_journal);
        FooterHelper.setupFooter(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        tvJournalDate = findViewById(R.id.tvJournalDate);
        etJournalEntry = findViewById(R.id.etJournalEntry);
        btnSaveJournal = findViewById(R.id.btnSaveJournal);
        cbFav = (CheckBox) findViewById(R.id.cbFav);
        dbHelper = new DatabaseHelper(this);

        // Get selectedDate from Intent
        selectedDate = getIntent().getStringExtra("selectedDate");

        if (selectedDate == null) {
            // If no date is passed, use today's date
            selectedDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        }
        userId = sharedPreferences.getInt("user_id",-1);
        tvJournalDate.setText("Date: " + selectedDate);

        // Load existing journal entry if available
        loadJournalEntry(selectedDate, userId);

        // Save or update journal entry
        btnSaveJournal.setOnClickListener(v -> saveJournalEntry());
        cbFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                if (isChecked) {
                    // ✅ Mark journal as favorite
                    ContentValues values = new ContentValues();
                    values.put("is_favorite", 1);
                    db.update("journal", values, "date=? AND user_id=?", new String[]{selectedDate, String.valueOf(userId)});

                    // ✅ Insert into favorites table
                    Cursor cursor = db.rawQuery("SELECT id FROM journal WHERE date=? AND user_id=?", new String[]{selectedDate, String.valueOf(userId)});
                    if (cursor.moveToFirst()) {
                        int journalId = cursor.getInt(0);
                        ContentValues favValues = new ContentValues();
                        favValues.put("user_id", userId);
                        favValues.put("journal_id", journalId);
                        db.insert("favorites", null, favValues);
                    }
                    cursor.close();
                    Toast.makeText(EditJournalActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    // ❌ Remove journal from favorites
                    ContentValues values = new ContentValues();
                    values.put("is_favorite", 0);
                    db.update("journal", values, "date=? AND user_id=?", new String[]{selectedDate, String.valueOf(userId)});

                    // ❌ Delete from favorites table
                    db.delete("favorites", "journal_id IN (SELECT id FROM journal WHERE date=? AND user_id=?)", new String[]{selectedDate, String.valueOf(userId)});
                    Toast.makeText(EditJournalActivity.this, "Removed from Favorites", Toast.LENGTH_SHORT).show();
                }

                db.close();
            }
        });

    }

    private void loadJournalEntry(String date, int user_id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT entry, is_favorite FROM journal WHERE date=? AND user_id=?",
                new String[]{date, String.valueOf(user_id)}
        );

        if (cursor.moveToFirst()) {
            etJournalEntry.setText(cursor.getString(0));  // Set the existing journal entry
            int isFavorite = cursor.getInt(1);            // Get the is_favorite value
            cbFav.setChecked(isFavorite == 1);            // Check or uncheck checkbox
        }

        cursor.close();
        db.close();
    }


    private void saveJournalEntry() {
        String entryText = etJournalEntry.getText().toString().trim();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set the date and entry values
        values.put("date", selectedDate);
        values.put("entry", entryText);
        values.put("user_id", userId);
        // Set the title to be the same as the date if no title is provided (or by default)
        String title = selectedDate;  // Setting title as date
        values.put("title", title);

        // Check if entry already exists for the selected date
        Cursor cursor = db.rawQuery("SELECT * FROM journal WHERE date=? AND user_id=?", new String[]{selectedDate, String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            // If journal exists, update it
            db.update("journal", values, "date=? AND user_id=?", new String[]{selectedDate, String.valueOf(userId)});
            Toast.makeText(this, "Journal Updated", Toast.LENGTH_SHORT).show();
        } else {
            // If journal doesn't exist, insert a new entry
            db.insert("journal", null, values);
            Toast.makeText(this, "Journal Saved", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
    }

}
