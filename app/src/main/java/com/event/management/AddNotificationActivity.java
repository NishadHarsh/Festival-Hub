package com.event.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddNotificationActivity extends AppCompatActivity {

    Spinner spinner;
    EditText message;
    Button submit;
    ArrayList<String> arrayEventId;
    ArrayList<String> arrayEventName;

    String sEventId;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);
        getSupportActionBar().setTitle("Send Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        spinner = findViewById(R.id.add_notification_spinner);
        message = findViewById(R.id.add_notification_message);
        submit = findViewById(R.id.add_notification_submit);

        arrayEventId = new ArrayList<>();
        arrayEventName = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            arrayEventId.add(String.valueOf(i));
            arrayEventName.add("ROBOTO" + " (College Name)");
        }
        ArrayAdapter collegeAdapter = new ArrayAdapter(AddNotificationActivity.this, android.R.layout.simple_list_item_checked, arrayEventName);
        spinner.setAdapter(collegeAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sEventId = arrayEventId.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().toString().equalsIgnoreCase("")) {
                    message.setError("Message Required");
                } else {
                    onBackPressed();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}