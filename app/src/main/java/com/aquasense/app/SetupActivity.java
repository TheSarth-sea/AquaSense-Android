package com.aquasense.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        EditText etName = findViewById(R.id.etName);
        EditText etLimit = findViewById(R.id.etLimit);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String limit = etLimit.getText().toString();

            SharedPreferences prefs = getSharedPreferences("AquaSensePrefs", MODE_PRIVATE);
            prefs.edit()
                    .putString("name", name)
                    .putInt("limit", Integer.parseInt(limit))
                    .apply();

            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}
