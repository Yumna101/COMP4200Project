package com.example.volunteertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

public class admin_profile_activity extends AppCompatActivity {

    TextView tv_name, tv_changeName, tv_email, tv_role, tv_pass;
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

        tv_name.setText("Name: "+Displayname);
        tv_email.setText("Email: "+email);
        tv_role.setText("Role: "+role);

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
                Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                return;
            }

            spe.putString("password",pass);
            spe.apply();

            Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT).show();
        });

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(admin_profile_activity.this, admin_dashboard_activity.class);
            startActivity(intent);
            finish();
        });
    }
}
