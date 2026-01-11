package com.aquasense.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvLimit = findViewById(R.id.tvLimit);
        TextView tvUsed = findViewById(R.id.tvUsed);
        TextView tvRemaining = findViewById(R.id.tvRemaining);

        Button btnAddWater = findViewById(R.id.btnAddWater);

        btnAddWater.setOnClickListener(v ->
                startActivity(new Intent(this, AddWaterActivity.class))
        );


        SharedPreferences prefs = getSharedPreferences("AquaSensePrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "User");
        int limit = prefs.getInt("limit", 0);

        int used = prefs.getInt("used", 0);
        int remaining = limit - used;

        tvWelcome.setText("Welcome, " + name);
        tvLimit.setText("Daily Limit: " + limit + " L");
        tvUsed.setText("Used Today: " + used + " L");
        tvRemaining.setText("Remaining: " + remaining + " L");
    }
}
