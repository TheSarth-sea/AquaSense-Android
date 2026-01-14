package com.aquasense.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.components.YAxis;





public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvLimit = findViewById(R.id.tvLimit);
        TextView tvUsed = findViewById(R.id.tvUsed);
        TextView tvRemaining = findViewById(R.id.tvRemaining);

        ProgressBar progressWater = findViewById(R.id.progressWater);


        Button btnAddWater = findViewById(R.id.btnAddWater);

        btnAddWater.setOnClickListener(v ->
                startActivity(new Intent(this, AddWaterActivity.class))
        );


        SharedPreferences prefs = getSharedPreferences("AquaSensePrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "User");
        int limit = prefs.getInt("limit", 0);

    }
    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("AquaSensePrefs", MODE_PRIVATE);
        String today = new java.text.SimpleDateFormat(
                "yyyyMMdd",
                java.util.Locale.getDefault()
        ).format(new java.util.Date());

        String savedDate = prefs.getString("date", "");

        if (!today.equals(savedDate)) {
            int lastUsed = prefs.getInt("used", 0);

            prefs.edit()
                    .putInt("yesterdayUsed", lastUsed)
                    .putInt("used", 0)
                    .putString("date", today)
                    .apply();
        }

        TextView tvLimit = findViewById(R.id.tvLimit);
        TextView tvUsed = findViewById(R.id.tvUsed);
        TextView tvRemaining = findViewById(R.id.tvRemaining);
        ProgressBar progressWater = findViewById(R.id.progressWater);

        int limit = prefs.getInt("limit", 0);
        int used = prefs.getInt("used", 0);

        int remaining = limit - used;
        int progress = 0;

        if (limit > 0) {
            progress = (used * 100) / limit;
        }

        tvLimit.setText("Daily Limit: " + limit + " L");
        tvUsed.setText("Used Today: " + used + " L");
        tvRemaining.setText("Remaining: " + remaining + " L");
        progressWater.setProgress(progress);

        if (used > limit) {
            Toast.makeText(this, "Daily water limit exceeded!", Toast.LENGTH_SHORT).show();
        }
        TextView tvInsight = findViewById(R.id.tvInsight);
        int yesterday = prefs.getInt("yesterdayUsed", 0);

        if (yesterday > 0) {
            if (used < yesterday) {
                tvInsight.setText("Great job! You used less water than yesterday 👏");
            } else if (used > yesterday) {
                tvInsight.setText("You used more water than yesterday. Try to reduce 💧");
            } else {
                tvInsight.setText("Your water usage is same as yesterday.");
            }
        } else {
            tvInsight.setText("Start tracking daily to see insights.");
        }
        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, used));   // Used
        entries.add(new BarEntry(1f, limit));  // Limit

        BarDataSet dataSet = new BarDataSet(entries, "Water Usage");
        dataSet.setColors(
                android.graphics.Color.parseColor("#2196F3"), // Used
                android.graphics.Color.parseColor("#4CAF50")  // Limit
        );
        dataSet.setValueTextSize(12f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.5f);

        barChart.setData(barData);
        YAxis leftAxis = barChart.getAxisLeft();

// Set Y-axis minimum and maximum
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(limit);   // VERY IMPORTANT

        leftAxis.setGranularity(1f);
        leftAxis.setDrawGridLines(true);

// Disable right Y-axis
        barChart.getAxisRight().setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setGranularity(1f);
        ArrayList<String> labels = new ArrayList<>();
        labels.add("Used");
        labels.add("Limit");

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        barChart.getAxisRight().setEnabled(false);
        barChart.animateY(900);
        barChart.invalidate();



    }

}
