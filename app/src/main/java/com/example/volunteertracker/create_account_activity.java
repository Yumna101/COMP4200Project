package com.example.volunteertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

public class create_account_activity extends AppCompatActivity {

    EditText et_name, et_email, et_pass, et_confirm;
    RadioGroup roleGroup;
    Button btn_create;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        et_confirm = findViewById(R.id.et_confirm);
        roleGroup = findViewById(R.id.role_group);
        btn_create = findViewById(R.id.btn_create);
        btn_create.setOnClickListener(v -> {

            String name = et_name.getText().toString();
            String email = et_email.getText().toString();
            String pass = et_pass.getText().toString();
            String confirm = et_confirm.getText().toString();

            if(name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()){
                Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show();
                return;
            }

            if(!pass.equals(confirm)){
                Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                return;
            }

            int selected = roleGroup.getCheckedRadioButtonId();

            // check if no role selected
            if(selected == -1){
                Toast.makeText(this,"Select a role",Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton roleBtn = findViewById(selected);
            String role = roleBtn.getText().toString().trim();

            // Normalize role value
            if(role.equalsIgnoreCase("admin")){
                role = "Admin";
            } else if(role.equalsIgnoreCase("volunteer")){
                role = "Volunteer";
            }

            // Debug toast to verify saved role
            Toast.makeText(this, "Saved role: '" + role + "'", Toast.LENGTH_SHORT).show();

            sp = getSharedPreferences("UserData",MODE_PRIVATE);
            spe = sp.edit();

            spe.putString("name",name);
            spe.putString("email",email);
            spe.putString("password",pass);
            spe.putString("role",role);

            spe.apply();

            Toast.makeText(this,"Account Created",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(create_account_activity.this, main_activity.class));
        });
    }
}