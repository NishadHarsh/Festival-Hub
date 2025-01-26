package com.event.management;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SignupActivity extends AppCompatActivity {

    EditText name, email, contact, password, confirmPassword;
    RadioGroup gender;
    Button signup;
    Spinner collegeName, stream, semester;
    String sGender, sCollegeName, sCollegeNameId, sStream, sSemester;

    String[] streamArray = {"Diploma", "Degree", "BCA"};
    String[] semesterArray = {"1", "2", "3", "4", "5", "6", "7", "8"};

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String namePattern = "[a-zA-Z ]+";

    ArrayList<String> collegeNameArray;
    ArrayList<String> collegeNameIdArray;

    ImageView passwordVisible, passwordInvisible, confirmPasswordVisible, confirmPasswordInvisible;

    final int min = 1111;
    final int max = 9999;
    final Random random = new Random();

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Signup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        contact = findViewById(R.id.signup_cno);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm_password);

        gender = findViewById(R.id.signup_gender);
        signup = findViewById(R.id.signup_button);

        collegeName = findViewById(R.id.signup_college);

        stream = findViewById(R.id.signup_stream);
        ArrayAdapter streamAdapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_checked, streamArray);
        stream.setAdapter(streamAdapter);

        stream.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sStream = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        semester = findViewById(R.id.signup_semester);
        ArrayAdapter semesterAdapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_checked, semesterArray);
        semester.setAdapter(semesterAdapter);

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
        ArrayAdapter collegeAdapter = new ArrayAdapter(SignupActivity.this, android.R.layout.simple_list_item_checked, collegeNameArray);
        collegeName.setAdapter(collegeAdapter);

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

        passwordVisible = findViewById(R.id.signup_visible_password);
        passwordInvisible = findViewById(R.id.signup_invisible_password);
        confirmPasswordVisible = findViewById(R.id.signup_confirm_visible_password);
        confirmPasswordInvisible = findViewById(R.id.signup_confirm_invisible_password);

        passwordInvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordInvisible.setVisibility(View.GONE);
                passwordVisible.setVisibility(View.VISIBLE);
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        passwordVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordInvisible.setVisibility(View.VISIBLE);
                passwordVisible.setVisibility(View.GONE);
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        confirmPasswordVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPasswordInvisible.setVisibility(View.VISIBLE);
                confirmPasswordVisible.setVisibility(View.GONE);
                confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        confirmPasswordInvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPasswordInvisible.setVisibility(View.GONE);
                confirmPasswordVisible.setVisibility(View.VISIBLE);
                confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equalsIgnoreCase("")) {
                    name.setError("Name Required");
                } else if (!name.getText().toString().trim().matches(namePattern)) {
                    name.setError("Enter Only Alphabetical Character");
                } else if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    email.setError("Email Id Required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Valid Email Id Required");
                } else if (contact.getText().toString().trim().equalsIgnoreCase("")) {
                    contact.setError("Contact No. Required");
                } else if (contact.getText().toString().length() < 10 || contact.getText().toString().length() > 10) {
                    contact.setError("Valid Contact No. Required");
                } else if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    password.setError("Password Required");
                } else if (confirmPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    confirmPassword.setError("Confirm Password Required");
                } else if (!password.getText().toString().matches(confirmPassword.getText().toString())) {
                    confirmPassword.setError("Password Not Match");
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new ToastIntentClass(SignupActivity.this, "Please Select Gender");
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