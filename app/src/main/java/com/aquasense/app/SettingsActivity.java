package com.aquasense.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText etLimit = findViewById(R.id.etLimit);
        Button btnSave = findViewById(R.id.btnSave);

        SharedPreferences prefs = getSharedPreferences("waterPrefs", MODE_PRIVATE);

        btnSave.setOnClickListener(v -> {
            int limit = Integer.parseInt(etLimit.getText().toString());
            prefs.edit().putInt("dailyLimit", limit).apply();
            Toast.makeText(this, "Limit Updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
