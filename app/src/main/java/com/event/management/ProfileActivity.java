package com.event.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    EditText name, email, contact;
    RadioGroup gender;
    RadioButton male, female, transgender;
    Button signup;
    Spinner collegeName, stream, semester;
    String sGender, sCollegeName, sCollegeNameId, sStream, sSemester;

    String[] streamArray = {"Diploma", "Degree", "BCA"};
    String[] semesterArray = {"1", "2", "3", "4", "5", "6", "7", "8"};

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ArrayList<String> collegeNameArray;
    ArrayList<String> collegeNameIdArray;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        contact = findViewById(R.id.profile_cno);

        gender = findViewById(R.id.profile_gender);
        male = findViewById(R.id.profile_male);
        female = findViewById(R.id.profile_female);
        transgender = findViewById(R.id.profile_transgender);

        signup = findViewById(R.id.profile_button);

        collegeName = findViewById(R.id.profile_college);

        stream = findViewById(R.id.profile_stream);
        ArrayAdapter streamAdapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_list_item_checked, streamArray);
        stream.setAdapter(streamAdapter);

        for (int i = 0; i < streamArray.length; i++) {
            if (streamArray[i].equalsIgnoreCase(sp.getString(ConstantSp.STREAM, ""))) {
                stream.setSelection(i);
            }
        }
        sStream = sp.getString(ConstantSp.STREAM, "");

        stream.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sStream = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        semester = findViewById(R.id.profile_semester);
        ArrayAdapter semesterAdapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_list_item_checked, semesterArray);
        semester.setAdapter(semesterAdapter);
        semester.setSelection(Integer.parseInt(sp.getString(ConstantSp.SEMESTER, "")) - 1);
        sSemester = sp.getString(ConstantSp.SEMESTER, "");

        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sSemester = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        collegeNameArray = new ArrayList<>();
        collegeNameIdArray = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            collegeNameIdArray.add(String.valueOf(i));
            collegeNameArray.add("College Name");
        }
        ArrayAdapter collegeAdapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_list_item_checked, collegeNameArray);
        collegeName.setAdapter(collegeAdapter);
        sCollegeName = sp.getString(ConstantSp.COLLEGENAME, "");
        sCollegeNameId = sp.getString(ConstantSp.COLLEGEID, "");


        if (sp.getString(ConstantSp.GENDER, "").equalsIgnoreCase("Male")) {
            male.setChecked(true);
        } else if (sp.getString(ConstantSp.GENDER, "").equalsIgnoreCase("Female")) {
            female.setChecked(true);
        } else if (sp.getString(ConstantSp.GENDER, "").equalsIgnoreCase("Transgender")) {
            transgender.setChecked(true);
        } else {

        }
        sGender = sp.getString(ConstantSp.GENDER, "");
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = gender.getCheckedRadioButtonId();
                RadioButton rb = findViewById(id);
                sGender = rb.getText().toString();
            }
        });

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
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().trim().equalsIgnoreCase("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().length() < 10 || contact.getText().toString().length() > 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new ToastIntentClass(ProfileActivity.this, "Please Select Gender");
                } else {
                    //onBackPressed();
                }
            }
        });

        name.setText(sp.getString(ConstantSp.NAME, ""));
        email.setText(sp.getString(ConstantSp.EMAIL, ""));
        contact.setText(sp.getString(ConstantSp.CONTACT, ""));

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