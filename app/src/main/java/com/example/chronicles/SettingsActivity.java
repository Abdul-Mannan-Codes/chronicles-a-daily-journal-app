package com.example.chronicles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private EditText etUsername;
    private Button btnSaveSettings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        etUsername = findViewById(R.id.etUsername);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        FooterHelper.setupFooter(this);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedName = prefs.getString("username", "");
        etUsername.setText(savedName);

        btnSaveSettings.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username", username);
            editor.apply();

            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
        });
        btnLogout.setOnClickListener(view->{
            sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
