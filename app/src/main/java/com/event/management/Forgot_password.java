package com.event.management;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class Forgot_password extends AppCompatActivity {
    ImageButton back;

    EditText e1, e2, e3, email;
    Button b1, next;
    LinearLayout firstLayout, secondLayout;
    SharedPreferences sp;
    String email_pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        email = findViewById(R.id.forgot_cp_current);
        firstLayout = findViewById(R.id.forgot_password_first);
        secondLayout = findViewById(R.id.forgot_password_second);
        next = findViewById(R.id.forgot_cp_button_next);

        e1 = findViewById(R.id.forgot_cp_current_email);
        e2 = findViewById(R.id.forgot_cp_newpass);
        e3 = findViewById(R.id.forgot_cp_reeneterpass);
        b1 = findViewById(R.id.forgot_cp_button);

        back = findViewById(R.id.forgot_cp_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equalsIgnoreCase("")) {
                    email.setError("Please enter email-id");
                } else if (!email.getText().toString().matches(email_pattern)) {
                    email.setError("Valid Email Id Required");
                    //Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else {
                    firstLayout.setVisibility(View.GONE);
                    secondLayout.setVisibility(View.VISIBLE);
                    e1.setText(email.getText().toString());
                }

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e1.getText().toString().equalsIgnoreCase("")) {
                    e1.setError("Please enter email-id");
                } else if (!e1.getText().toString().matches(email_pattern)) {
                    e1.setError("Valid Email Id Required");
                    //Toast.makeText(SignupActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (e2.getText().toString().trim().equalsIgnoreCase("")) {
                    e2.setError("New Password Required");
                } else if (e3.getText().toString().trim().equalsIgnoreCase("")) {
                    e3.setError("Confirm Password Required");
                } else if (!e2.getText().toString().matches(e3.getText().toString())) {
                    e3.setError("Password Not Match");
                } else {
                    Toast.makeText(Forgot_password.this, "Password Change Successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });

    }
}
