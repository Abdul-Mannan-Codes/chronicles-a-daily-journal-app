package com.example.chronicles;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DashboardActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnNewJournal, btnViewJournals, btnFavorites, btnSettings;
    DatabaseHelper dbHelper;
    ImageButton btnSettingsIcon;
    SharedPreferences sharedPreferences;
    private String startDate = "", endDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        FooterHelper.setupFooter(this);

        ImageButton btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> showCreateChronicleDialog());

        tvWelcome = findViewById(R.id.tvWelcome);
        btnNewJournal = findViewById(R.id.btnNewJournal);
        btnViewJournals = findViewById(R.id.btnViewJournals);
        btnFavorites = findViewById(R.id.btnFavorites);
        btnSettings = findViewById(R.id.btnSettings);
        btnSettingsIcon = (ImageButton) findViewById(R.id.btnSettingsIcon);
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Check if the user is logged in
        int userId = sharedPreferences.getInt("user_id", -1); // -1 means user is not logged in
        String username = sharedPreferences.getString("username", "User");

        if (userId == -1 || username.equals("User")) {
            // If user is not logged in, redirect to login activity
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        } else {
            // If logged in, show welcome message
            TextView tvWelcome = findViewById(R.id.tvWelcome);
            tvWelcome.setText("Welcome, " + username + "!");
        }


        // Open Create Journal Page
        btnNewJournal.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, EditJournalActivity.class)));

        // Open Journal List Page
        btnViewJournals.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, JournalListActivity.class)));

        // Open Favorites Page
        btnFavorites.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, FavoritesActivity.class)));

        // Open Settings Page
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                Toast.makeText(getApplicationContext(),"Settings",Toast.LENGTH_SHORT).show();
           }
        });
    }
    private void showCreateChronicleDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_create_chronicle);

        EditText etChronicleName = dialog.findViewById(R.id.etChronicleName);
        Button btnSelectStartDate = dialog.findViewById(R.id.btnSelectStartDate);
        Button btnSelectEndDate = dialog.findViewById(R.id.btnSelectEndDate);
        Button btnSaveChronicle = dialog.findViewById(R.id.btnSaveChronicle);

        btnSelectStartDate.setOnClickListener(v -> selectDate(btnSelectStartDate, true));
        btnSelectEndDate.setOnClickListener(v -> selectDate(btnSelectEndDate, false));

        btnSaveChronicle.setOnClickListener(v -> {
            String name = etChronicleName.getText().toString().trim();
            if (name.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please enter all details!", Toast.LENGTH_SHORT).show();
            } else {
                saveChronicle(name, startDate, endDate);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void selectDate(Button button, boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                    button.setText(date);
                    if (isStart) {
                        startDate = date;
                    } else {
                        endDate = date;
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void saveChronicle(String name, String start, String end) {
        int userId = sharedPreferences.getInt("user_id", -1);
        if (userId == -1) {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO chronicles (user_id, name, start_date, end_date) VALUES (?, ?, ?, ?)",
                new Object[]{userId, name, start, end});
        db.close();

        Toast.makeText(this, "Chronicle '" + name + "' saved!", Toast.LENGTH_SHORT).show();
        loadChronicles();
    }
    private void loadChronicles() {
        LinearLayout chronicleContainer = findViewById(R.id.chronicleContainer);
        chronicleContainer.removeAllViews();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM chronicles", null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1); // Chronicle name
                String startDate = cursor.getString(2);
                String endDate = cursor.getString(3);

                Button btnChronicle = new Button(this);
                btnChronicle.setText(name + "\n(" + startDate + " - " + endDate + ")");
                btnChronicle.setAllCaps(false);
                btnChronicle.setBackgroundColor(Color.parseColor("#673AB7"));
                btnChronicle.setTextColor(Color.WHITE);
                btnChronicle.setPadding(20, 10, 20, 10);
                btnChronicle.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                btnChronicle.setOnClickListener(v -> {
                    Intent intent = new Intent(DashboardActivity.this, JournalListActivity.class);
                    intent.putExtra("chronicleName", name);
                    intent.putExtra("startDate", startDate);
                    intent.putExtra("endDate", endDate);
                    startActivity(intent);
                });

                chronicleContainer.addView(btnChronicle);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }
}
