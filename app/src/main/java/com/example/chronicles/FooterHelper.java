package com.example.chronicles;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class FooterHelper {
static ImageButton button;
    public static void setupFooter(final Activity activity) {
        setupButtonClickListener(activity, R.id.btnCalendar, CalendarActivity.class);
        setupButtonClickListener(activity, R.id.btnDashboard, DashboardActivity.class);
        setupButtonClickListener(activity, R.id.btnCreate, EditJournalActivity.class);
        setupButtonClickListener(activity, R.id.btnSettingsIcon, SettingsActivity.class);
    }

    private static void setupButtonClickListener(final Activity activity, int buttonId, final Class<?> targetActivity) {
        View buttonView = activity.findViewById(buttonId);
        if (buttonView instanceof ImageButton) {
            button = (ImageButton) buttonView;
            // Set onClickListener
        } else {
            Log.e("FooterHelper", "Button is not of type ImageButton.");
        }

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Prevent navigation to the current activity
                    if (!activity.getClass().equals(targetActivity)) {
                        Intent intent = new Intent(activity, targetActivity);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }
}
