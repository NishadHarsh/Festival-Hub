package com.event.management;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddCollegeActivity extends AppCompatActivity {

    Button add;
    EditText college;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_college);
        getSupportActionBar().setTitle("Add College");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add = findViewById(R.id.add_college_add);
        college = findViewById(R.id.add_college_title);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (college.getText().toString().trim().equalsIgnoreCase("")) {
                    college.setError("College Name Required");
                } else {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}