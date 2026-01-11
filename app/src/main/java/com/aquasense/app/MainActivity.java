package com.aquasense.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("AquaSensePrefs", MODE_PRIVATE);
            boolean isSetupDone = prefs.contains("limit");

            if (isSetupDone) {
                startActivity(new Intent(this, HomeActivity.class));
            } else {
                startActivity(new Intent(this, SetupActivity.class));
            }
            finish();
        }, 2000);
    }
}
