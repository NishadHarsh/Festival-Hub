package com.event.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddEventCollegeActivity extends AppCompatActivity {
 
    EditText name,price,maxAllowed, eventDate, eventTime,description;
    Button signup;
    Spinner collegeName;
    String sCollegeName, sCollegeNameId;

    ArrayList<String> collegeNameArray;
    ArrayList<String> collegeNameIdArray;

    SharedPreferences sp;

    Calendar calendar;
    int iHour,iMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_college);
        getSupportActionBar().setTitle("Add Event Of College");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
        name = findViewById(R.id.add_event_college_name);
        price = findViewById(R.id.add_event_college_price);
        maxAllowed = findViewById(R.id.add_event_college_max_allowed);
        eventDate = findViewById(R.id.add_event_college_date);
        eventTime = findViewById(R.id.add_event_college_time);
        description = findViewById(R.id.add_event_college_message);

        signup = findViewById(R.id.add_event_college_button);
        collegeName = findViewById(R.id.add_event_college_college);

        collegeNameArray = new ArrayList<>();
        collegeNameIdArray = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            collegeNameIdArray.add(String.valueOf(i));
            collegeNameArray.add("College Name");
        }
        ArrayAdapter collegeAdapter = new ArrayAdapter(AddEventCollegeActivity.this, android.R.layout.simple_list_item_checked, collegeNameArray);
        collegeName.setAdapter(collegeAdapter);

        collegeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sCollegeName = collegeNameArray.get(i);
                sCollegeNameId = collegeNameIdArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Event Name Required");
                }
                else if (price.getText().toString().trim().equalsIgnoreCase("")) {
                    price.setError("Price Required");
                } else if (maxAllowed.getText().toString().trim().equalsIgnoreCase("")) {
                    maxAllowed.setError("Max. No. Of Team Member Required");
                } else if (eventDate.getText().toString().trim().equalsIgnoreCase("")) {
                    eventDate.setError("Event Date Required Required");
                } else if (eventTime.getText().toString().trim().equalsIgnoreCase("")) {
                    eventTime.setError("Event Time Required");
                }
                else if (description.getText().toString().trim().equalsIgnoreCase("")) {
                    description.setError("Rules/Description Required");
                }
                else {
                    onBackPressed();
                }
            }
        });

        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener fromDateClick = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR,i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                eventDate.setText(format.format(calendar.getTime()));

            }
        };

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddEventCollegeActivity.this,fromDateClick,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TimePickerDialog.OnTimeSetListener timeClick = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                iHour = i;
                iMinute = i1;

                String AM_PM = "";
                if(iHour>=12){
                    AM_PM = "PM";
                    if(iHour>=13 && iHour<24){
                        iHour -=12;
                    }
                    else{
                        iHour=12;
                    }
                }
                else if(iHour==0){
                    AM_PM = "AM";
                    iHour=12;
                }
                else {
                    AM_PM = "AM";
                }

                String sMin;
                if(iMinute<10){
                    sMin = "0"+iMinute;
                }
                else{
                    sMin= String.valueOf(iMinute);
                }

                eventTime.setText(iHour+ " : "+ sMin +" "+AM_PM);

            }
        };

        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(AddEventCollegeActivity.this,timeClick,iHour,iMinute,false).show();
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