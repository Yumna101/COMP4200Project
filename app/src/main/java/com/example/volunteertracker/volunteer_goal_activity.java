package com.example.volunteertracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class volunteer_goal_activity extends AppCompatActivity {
    TextView tvGoalProgress, tvHoursLeft, tv_remaining;
    ProgressBar progressGoal;
    EditText et_goal;
    Button btnAddHours, btnSubtractHours, btn_save, btn_back;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_goal);

        // link views
        tv_remaining = findViewById(R.id.tv_remaining);
        et_goal = findViewById(R.id.et_goal);
        btn_save = findViewById(R.id.btn_save_goal);
        btn_back = findViewById(R.id.btn_back);
        tvGoalProgress = findViewById(R.id.tvGoalProgress);
        tvHoursLeft = findViewById(R.id.tvHoursLeft);
        progressGoal = findViewById(R.id.progressGoal);
        btnAddHours = findViewById(R.id.btn_add_hours);
        btnSubtractHours = findViewById(R.id.btn_subtract_hours);

        // setup SharedPreferences
        sp = getSharedPreferences("UserData", MODE_PRIVATE);
        spe = sp.edit();

        // show saved goal if exists
        int savedGoal = sp.getInt("goal", 0);
        et_goal.setText(savedGoal == 0 ? "" : String.valueOf(savedGoal));

        updateRemainingHours(savedGoal, sp.getInt("completedHours", 0));
        updateGoalProgress();

        // save goal button
        btn_save.setOnClickListener(v -> {
            String goalStr = et_goal.getText().toString().trim();
            if (goalStr.isEmpty()) {
                Toast.makeText(this, "Enter goal hours", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int goal = Integer.parseInt(goalStr);
                if (goal <= 0) {
                    Toast.makeText(this, "Goal must be greater than 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                spe.putInt("goal", goal);
                spe.apply();

                updateRemainingHours(goal, sp.getInt("completedHours", 0));
                updateGoalProgress();
                Toast.makeText(this, "Goal Saved", Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });

        // add hours button
        btnAddHours.setOnClickListener(v -> {
            int currentHours = sp.getInt("completedHours", 0);
            currentHours += 1; // simulate adding hours
            spe.putInt("completedHours", currentHours);
            spe.apply();
            Toast.makeText(this, "Hours Added!", Toast.LENGTH_SHORT).show();
            updateGoalProgress();
        });

        // subtract hours button
        btnSubtractHours.setOnClickListener(v -> {
            int currentHours = sp.getInt("completedHours", 0);
            if (currentHours > 0) {
                currentHours -= 1;
                spe.putInt("completedHours", currentHours);
                spe.apply();
                Toast.makeText(this, "Hours Subtracted!", Toast.LENGTH_SHORT).show();
                updateGoalProgress();
            } else {
                Toast.makeText(this, "No hours to subtract", Toast.LENGTH_SHORT).show();
            }
        });

        // back button
        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(volunteer_goal_activity.this, home_activity.class);
            startActivity(intent);
            finish();
        });

        // TextWatcher for live goal updates
        et_goal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no action
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String goalStr = s.toString().trim();
                if (goalStr.isEmpty()) {
                    updateGoalProgress(0, sp.getInt("completedHours", 0));
                    return;
                }
                try {
                    int goal = Integer.parseInt(goalStr);
                    if (goal > 0) {
                        spe.putInt("goal", goal);
                        spe.apply();
                        updateGoalProgress();
                    } else {
                        updateGoalProgress(0, sp.getInt("completedHours", 0));
                    }
                } catch (NumberFormatException e) {
                    updateGoalProgress(0, sp.getInt("completedHours", 0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no action
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGoalProgress();
    }

    private void updateGoalProgress() {
        int goal = sp.getInt("goal", 0);
        int completed = sp.getInt("completedHours", 0);
        updateGoalProgress(goal, completed);
    }

    private void updateGoalProgress(int goal, int completed) {
        if (goal > 0) {
            tvGoalProgress.setText(completed + " / " + goal + " hours completed");

            int remaining = goal - completed;
            if (remaining < 0) remaining = 0;

            tvHoursLeft.setText(remaining + " hours left");
            progressGoal.setProgress((int) (((float) completed / goal) * 100));
            tv_remaining.setText("Remaining Hours: " + remaining);
        } else {
            tvGoalProgress.setText("0 / 0 hours completed");
            tvHoursLeft.setText("0 hours left");
            progressGoal.setProgress(0);
            tv_remaining.setText("Remaining Hours: 0");
        }
    }

    private void updateRemainingHours(int goal, int completed) {
        int remaining = goal - completed;
        if (remaining < 0) remaining = 0;
        tv_remaining.setText("Remaining Hours: " + remaining);
    }
}