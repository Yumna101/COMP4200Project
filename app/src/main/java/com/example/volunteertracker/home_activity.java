package com.example.volunteertracker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;


public class home_activity extends AppCompatActivity {
    TextView tvWelcome, tvGoalProgress, tvHoursLeft;
    ProgressBar progressGoal;
    Button btnGoal, btnProfile, btnLogs, btnFutureFeature, btnVolunteer;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // link views
        tvWelcome = findViewById(R.id.tvWelcome);
        btnGoal = findViewById(R.id.btnGoal);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogs = findViewById(R.id.btnLogs);
        btnFutureFeature = findViewById(R.id.btnFutureFeature);
        btnVolunteer = findViewById(R.id.btnVolunteer);
        tvGoalProgress = findViewById(R.id.tvGoalProgress);
        tvHoursLeft = findViewById(R.id.tvHoursLeft);
        progressGoal = findViewById(R.id.progressGoal);

        sp = getSharedPreferences("UserData", MODE_PRIVATE);
        String name = sp.getString("name", "");
        tvWelcome.setText("Welcome, " + name);

        btnProfile.setOnClickListener(v -> startActivity(new Intent(home_activity.this, profile_activity.class)));
        btnGoal.setOnClickListener(v -> startActivity(new Intent(home_activity.this, volunteer_goal_activity.class)));
    }

    // update goal progress on resume
    @Override
    protected void onResume() {
        super.onResume();
        updateGoalProgress();
    }

    private void updateGoalProgress() {
        int goal = sp.getInt("goal", 0);
        int completed = sp.getInt("completedHours", 0);

        if(goal > 0){
            tvGoalProgress.setText(completed + " / " + goal + " hours completed");

            int remaining = goal - completed;
            if(remaining < 0) remaining = 0;

            tvHoursLeft.setText(remaining + " hours left");

            int progressPercent = (int)(((float) completed / goal) * 100);
            progressGoal.setProgress(progressPercent);
        }

        // navigate to profile
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(home_activity.this, profile_activity.class));
        });

        // navigate to goal screen
        btnGoal.setOnClickListener(v -> {
            startActivity(new Intent(home_activity.this, volunteer_goal_activity.class));
        });
    }
}