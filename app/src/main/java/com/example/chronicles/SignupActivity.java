package com.example.chronicles;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;

public class SignupActivity extends AppCompatActivity {

    EditText etNewUsername, etNewPassword;
    Button btnSignup, btnSelectImage;
    ImageView imgProfile;
    TextView tvGoToLogin;
    DatabaseHelper dbHelper;
    String imagePath = "";  // Stores image file path

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSignup = findViewById(R.id.btnSignup);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgProfile = findViewById(R.id.imgProfile);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
        dbHelper = new DatabaseHelper(this);

        btnSignup.setOnClickListener(v -> registerUser());
        btnSelectImage.setOnClickListener(v -> openImageChooser());
        tvGoToLogin.setOnClickListener(v -> startActivity(new Intent(SignupActivity.this, LoginActivity.class)));
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = (Uri)data.getData();
            imgProfile.setImageURI(imageUri);
            imagePath = getRealPathFromURI(imageUri);
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        android.database.Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();  // fallback
    }

    private void registerUser() {
        String username = etNewUsername.getText().toString().trim();
        String password = etNewPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("profile_image_path", imagePath);  // Save image path, not BLOB

        long result = db.insert("user_info", null, values);
        if (result != -1) {
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
