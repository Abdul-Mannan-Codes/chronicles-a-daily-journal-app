package com.example.chronicles;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {
    ListView listViewFavorites;
    DatabaseHelper dbHelper;
    ArrayList<String> favoriteList;
    ArrayAdapter<String> adapter;
    int userId;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);
        listViewFavorites = findViewById(R.id.listViewFavorites);
        dbHelper = new DatabaseHelper(this);
        favoriteList = new ArrayList<>();

        loadFavorites();
    }

    private void loadFavorites() {
        favoriteList = dbHelper.getFavoriteChronicles(userId);
        if (favoriteList.isEmpty()) {
            Toast.makeText(this, "No favorite chronicles found!", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoriteList);
            listViewFavorites.setAdapter(adapter);
        }
    }
}
