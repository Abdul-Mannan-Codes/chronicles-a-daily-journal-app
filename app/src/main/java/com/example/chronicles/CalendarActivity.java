package com.example.chronicles;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    Button btnOpenJournal;
    long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        FooterHelper.setupFooter(this);
        calendarView = findViewById(R.id.calendarView);
        btnOpenJournal = findViewById(R.id.btnOpenJournal);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            selectedDateMillis = calendar.getTimeInMillis();
            btnOpenJournal.setVisibility(View.VISIBLE);
        });

        btnOpenJournal.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarActivity.this, EditJournalActivity.class);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(selectedDateMillis));
            intent.putExtra("selectedDate", formattedDate);
            startActivity(intent);
        });

        setupFooterNavigation();
    }

    private void setupFooterNavigation() {
        findViewById(R.id.btnDashboard).setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
        });
        findViewById(R.id.btnSettingsIcon).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
        findViewById(R.id.btnCreate).setOnClickListener(v -> {
            startActivity(new Intent(this, DialogCreateChronicleActivity.class));
        });
    }

}
