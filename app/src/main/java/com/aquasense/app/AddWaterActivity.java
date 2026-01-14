package com.aquasense.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddWaterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_water);

        EditText etWater = findViewById(R.id.etWater);
        Button btnSave = findViewById(R.id.btnSaveWater);

        SharedPreferences prefs = getSharedPreferences("AquaSensePrefs", MODE_PRIVATE);

        btnSave.setOnClickListener(v -> {
            int added = Integer.parseInt(etWater.getText().toString());
            int used = prefs.getInt("used", 0);
            // Save last day's usage for insights
            int yesterdayUsed = prefs.getInt("yesterdayUsed", 0);
            prefs.edit()
                    .putInt("yesterdayUsed", yesterdayUsed) // keep previous
                    .apply();


            prefs.edit().putInt("used", used + added).apply();
            finish();
        });
    }
}
