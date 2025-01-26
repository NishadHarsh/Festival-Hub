package com.event.management;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Change_password extends AppCompatActivity {
    ImageButton back;

    EditText e1, e2, e3;
    Button b1;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        e1 = findViewById(R.id.cp_current);

        e2 = findViewById(R.id.cp_newpass);
        e3 = findViewById(R.id.cp_reeneterpass);
        b1 = findViewById(R.id.cp_button);

        back = findViewById(R.id.cp_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e1.getText().toString().trim().equalsIgnoreCase("")) {
                    e1.setError("Old Password Required");
                } else if (e2.getText().toString().trim().equalsIgnoreCase("")) {
                    e2.setError("New Password Required");
                } else if (e3.getText().toString().trim().equalsIgnoreCase("")) {
                    e3.setError("Confirm Password Required");
                } else if (!e2.getText().toString().matches(e3.getText().toString())) {
                    e3.setError("Password Not Match");
                } else {

                }
            }
        });
        e1.setText(sp.getString(ConstantSp.PASSWORD, ""));

    }
}
