package com.example.chronicles;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvSignup;
    DatabaseHelper dbHelper;  // SQLite Helper Class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        dbHelper = new DatabaseHelper(this);  // Initialize Database
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if(sharedPreferences.getInt("user_id", -1)!=-1){
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        }
        // Login Button Click
        btnLogin.setOnClickListener(v -> loginUser());

        // Signup Text Click
        tvSignup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_info WHERE username=? AND password=?",
                new String[]{username, password});

        if (cursor.getCount() > 0) {
            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("user_id", id); // Example user ID, you can retrieve this dynamically
                editor.putString("username", username);
                editor.apply();
                cursor.close();
            }
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }
}
