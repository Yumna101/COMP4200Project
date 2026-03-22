package com.example.volunteertracker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class admin_dashboard_activity extends AppCompatActivity {
    TextView tvWelcome;

    Button btnProfile, btnLogs, btnFutureFeature;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        // link views
        tvWelcome = findViewById(R.id.tvWelcome);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogs = findViewById(R.id.btnLogs);
        btnFutureFeature = findViewById(R.id.btnFutureFeature);

        // load user data
        sp = getSharedPreferences("UserData", MODE_PRIVATE);
        String name = sp.getString("name", "");

        tvWelcome.setText("Welcome, " + name);

        // navigate to profile
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(admin_dashboard_activity.this, admin_profile_activity.class));
        });
    }
}