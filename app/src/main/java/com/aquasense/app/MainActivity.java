package com.aquasense.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import android.graphics.Color;


public class MainActivity extends AppCompatActivity {

    int dailyLimit, usedToday;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvLimit = findViewById(R.id.tvLimit);
        TextView tvUsed = findViewById(R.id.tvUsed);
        ProgressBar progressBar = findViewById(R.id.progressWater);
        Button btnAddWater = findViewById(R.id.btnAddWater);
        Button btnSettings = findViewById(R.id.btnSettings);
        BarChart barChart = findViewById(R.id.barChart);

        prefs = getSharedPreferences("waterPrefs", MODE_PRIVATE);
        dailyLimit = prefs.getInt("dailyLimit", 10);
        usedToday = prefs.getInt("usedToday", 0);

        updateUI(tvLimit, tvUsed, progressBar);
        setupChart(barChart);

        btnAddWater.setOnClickListener(v -> {
            usedToday++;
            prefs.edit().putInt("usedToday", usedToday).apply();
            updateUI(tvLimit, tvUsed, progressBar);
            setupChart(barChart);
            uploadToFirebase();
        });

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        dailyLimit = prefs.getInt("dailyLimit", 10);
        usedToday = prefs.getInt("usedToday", 0);

        TextView tvLimit = findViewById(R.id.tvLimit);
        TextView tvUsed = findViewById(R.id.tvUsed);
        ProgressBar progressBar = findViewById(R.id.progressWater);
        BarChart barChart = findViewById(R.id.barChart);

        updateUI(tvLimit, tvUsed, progressBar);
        setupChart(barChart);
    }

    private void updateUI(TextView tvLimit, TextView tvUsed, ProgressBar progressBar) {
        tvLimit.setText("Daily Limit: " + dailyLimit + " L");
        tvUsed.setText("Used Today: " + usedToday + " L");
        progressBar.setProgress((usedToday * 100) / dailyLimit);
    }

    private void setupChart(BarChart barChart) {

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, usedToday));
        entries.add(new BarEntry(1f, dailyLimit));

        BarDataSet dataSet = new BarDataSet(entries, "Water (Liters)");
        dataSet.setColors(
                Color.parseColor("#42A5F5"),
                Color.parseColor("#66BB6A")
        );
        dataSet.setValueTextSize(14f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.4f);
        barChart.setData(barData);

        // X-Axis labels
        final String[] labels = {"Used", "Limit"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // Y-Axis FIX (IMPORTANT)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(dailyLimit);
        leftAxis.setGranularity(1f);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
    }


    private void uploadToFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("dailyLimit", dailyLimit);
        data.put("usedToday", usedToday);

        db.collection("waterData").document("user1").set(data);
    }
}
