package com.example.volunteertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

// Snackbar is used to show short messages at the bottom of the screen
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog; //import for alertDialog
import android.content.DialogInterface;    //import for alertDialog
import android.view.View;


public class admin_profile_activity extends AppCompatActivity {

    TextView tv_name, tv_org, tv_email, tv_role;
    EditText et_name, et_pass, et_confirm;
    Button btn_saveName, btn_savePass, btn_back;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_role = findViewById(R.id.tv_role);
        tv_org = findViewById(R.id.tv_org);
        et_name = findViewById(R.id.et_name);
        et_pass = findViewById(R.id.et_pass);
        et_confirm = findViewById(R.id.et_confirm);
        btn_saveName = findViewById(R.id.btn_saveName);
        btn_savePass = findViewById(R.id.btn_savePass);
        btn_back = findViewById(R.id.btn_back);

        sp = getSharedPreferences("UserData",MODE_PRIVATE);
        spe = sp.edit();

        String Displayname = sp.getString("name","");
        String email = sp.getString("email","");
        String role = sp.getString("role","");
        String organization = sp.getString("organization","");

        tv_name.setText("Name: "+Displayname);
        tv_email.setText("Email: "+email);
        tv_role.setText("Role: "+role);
        tv_org.setText("Organization: "+organization);

        btn_saveName.setOnClickListener(v -> {
            String name = et_name.getText().toString().trim();
            spe.putString("name",name);

            spe.apply();
            Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT).show();
        });

        btn_savePass.setOnClickListener(v -> {
            String pass = et_pass.getText().toString().trim();
            String confirm = et_confirm.getText().toString().trim();

            if(!pass.equals(confirm)){
                Snackbar.make(v, "Passwords do not match", Snackbar.LENGTH_LONG).show();
                return;
            }

            // Build an AlertDialog to confirm reset action
            AlertDialog.Builder builder = new AlertDialog.Builder(admin_profile_activity.this);

            builder.setTitle("Alert!");
            builder.setMessage("Do you want to change password?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                spe.putString("password", pass);
                spe.apply();

                Toast.makeText(admin_profile_activity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            builder.setCancelable(false);
            builder.show();
        });

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(admin_profile_activity.this, admin_dashboard_activity.class);
            startActivity(intent);
            finish();
        });
    }
}
