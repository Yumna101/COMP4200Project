package com.example.volunteertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class main_activity extends AppCompatActivity {

    EditText et_name, et_password;
    Button btn_login;

    Switch aSwitch;

    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // linking java variables to xml views
        et_name = findViewById(R.id.editText_name);
        et_password = findViewById(R.id.editTextTextPassword);
        btn_login = findViewById(R.id.button);
        Button btn_createAccount = findViewById(R.id.button_createAccount);
        aSwitch = findViewById(R.id.switch1);

        // loading previously saved data
        loadData();

        // login button click listener
        btn_login.setOnClickListener(v -> {

            String name = et_name.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            // basic validation
            if(name.isEmpty() || password.isEmpty()){
                Toast.makeText(main_activity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // access shared preferences
            SharedPreferences userSP = getSharedPreferences("UserData", MODE_PRIVATE);
            String savedEmail = userSP.getString("email", "");
            String savedname = userSP.getString("name", "");
            String savedPass = userSP.getString("password", "");

            // check if account exists
            if(savedEmail.isEmpty() || savedPass.isEmpty()){
                Toast.makeText(main_activity.this, "No account found. Please create an account.", Toast.LENGTH_SHORT).show();
                return;
            }

            // validate credentials
            boolean isUserMatch = name.equals(savedEmail) || name.equals(savedname);
            if(!isUserMatch || !password.equals(savedPass)){
                Toast.makeText(main_activity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            sp = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            spe = sp.edit();

            // save credentials if remember switch is on
            if(aSwitch.isChecked()){
                spe.putString("key_name", name);
                spe.putString("key_pass", password);
                spe.putBoolean("key_switch", true);
            } else {
                spe.clear();
            }

            spe.apply();

            // role-based navigation using saved role
            String role = userSP.getString("role", "").trim();

            if(role.isEmpty()){
                Toast.makeText(main_activity.this, "Please create an account first", Toast.LENGTH_SHORT).show();
            }
            else if(role.equalsIgnoreCase("Admin")){
                Toast.makeText(main_activity.this, "Opening Admin Dashboard", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(main_activity.this, admin_dashboard_activity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(main_activity.this, "Opening Volunteer Dashboard", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(main_activity.this, home_activity.class));
                finish();
            }
        });

        // create account button click listener
        btn_createAccount.setOnClickListener(v -> {
            startActivity(new Intent(main_activity.this, create_account_activity.class));
        });

        // listening for changes in the remember switch
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            // accessing shared preferences
            sp = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            spe = sp.edit();

            if (isChecked) {

                // saving name when switch is turned on
                spe.putString("key_name", et_name.getText().toString());

                // saving password when switch is turned on
                spe.putString("key_pass", et_password.getText().toString());

                // saving switch state
                spe.putBoolean("key_switch", true);

            } else {

                // clearing all saved data when switch is turned off
                spe.clear();
            }

            // committing the changes immediately
            spe.commit();
        });
        // modified code ends here
    }

    void loadData() {

        // accessing shared preferences
        sp = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        // restoring name if it exists
        et_name.setText(sp.getString("key_name", null));

        // restoring password if it exists
        et_password.setText(sp.getString("key_pass", null));

        // restoring switch state
        aSwitch.setChecked(sp.getBoolean("key_switch", false));
    }
}