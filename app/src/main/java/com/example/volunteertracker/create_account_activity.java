package com.example.volunteertracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import com.example.volunteertracker.R;

import android.view.View;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

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

            SharedPreferences spCheck = getSharedPreferences("UserData", MODE_PRIVATE);
            String orgCheck = spCheck.getString("organization", "");

            if(role.equalsIgnoreCase("Admin")){
                // clear old selection so user must pick again
                SharedPreferences spClear = getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = spClear.edit();
                editor.remove("organization");
                editor.apply();

                choose_org_activity fragment = new choose_org_activity();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, fragment)
                        .addToBackStack(null)
                        .commit();

                return;
            }

            sp = getSharedPreferences("UserData",MODE_PRIVATE);
            spe = sp.edit();

            spe.putString("name",name);
            spe.putString("email",email);
            spe.putString("password",pass);
            spe.putString("role",role);

            String org = sp.getString("organization", "");
            if(role.equalsIgnoreCase("Admin") && !org.isEmpty()){
                spe.putString("organization", org);
            }

            spe.apply();

            Toast.makeText(this,"Account Created",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(create_account_activity.this, main_activity.class));
        });
    }

    // ADDED: called after organization is selected and saved
    public void saveOrganizationAndProceed(){
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String pass = et_pass.getText().toString();

        sp = getSharedPreferences("UserData",MODE_PRIVATE);
        spe = sp.edit();

        spe.putString("name",name);
        spe.putString("email",email);
        spe.putString("password",pass);
        spe.putString("role","Admin");

        String org = sp.getString("organization", "");
        spe.putString("organization", org);

        spe.apply();

        // ADDED: show confirmation and navigate immediately
        Toast.makeText(this,"Organization Saved",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(create_account_activity.this, main_activity.class));
    }
}

