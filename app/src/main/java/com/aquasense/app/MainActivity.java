package com.aquasense.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int dailyLimit = 10;
    private int usedToday = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // UI references
        TextView tvLimit = findViewById(R.id.tvLimit);
        TextView tvUsed = findViewById(R.id.tvUsed);
        ProgressBar progressBar = findViewById(R.id.progressWater);
        Button btnAddWater = findViewById(R.id.btnAddWater);

        // Update UI initially
        updateUI(tvLimit, tvUsed, progressBar);

        // Button click
        btnAddWater.setOnClickListener(v -> {
            usedToday += 1; // add 1L per click

            if (usedToday > dailyLimit) {
                Toast.makeText(this, "Daily limit exceeded!", Toast.LENGTH_SHORT).show();
            }

            updateUI(tvLimit, tvUsed, progressBar);
            uploadToFirebase();
        });
    }

    private void updateUI(TextView tvLimit, TextView tvUsed, ProgressBar progressBar) {
        tvLimit.setText("Daily Limit: " + dailyLimit + " L");
        tvUsed.setText("Used Today: " + usedToday + " L");

        int progress = (usedToday * 100) / dailyLimit;
        progressBar.setProgress(progress);
    }

    private void uploadToFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> data = new HashMap<>();
        data.put("dailyLimit", dailyLimit);
        data.put("usedToday", usedToday);

        db.collection("waterData")
                .document("user1")
                .set(data);
    }
}
