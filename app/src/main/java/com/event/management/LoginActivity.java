package com.event.management;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    TextView signup, forgotPassword;
    EditText email, password;
    Button login;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        signup = findViewById(R.id.login_signup);
        forgotPassword = findViewById(R.id.login_forgot_password);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().trim().equalsIgnoreCase("")) {
                    email.setError("Email Id/Contact No. Required");
                } else if (password.getText().toString().trim().equalsIgnoreCase("")) {
                    password.setError("Password Required");
                } else {
                    //new ToastIntentClass(LoginActivity.this,HomeActivity.class);
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Select User Type");
                    builder.setPositiveButton("Student", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sp.edit().putString(ConstantSp.ID, "3").commit();
                            sp.edit().putString(ConstantSp.TYPE, "Student").commit();
                            sp.edit().putString(ConstantSp.NAME, "Richard").commit();
                            sp.edit().putString(ConstantSp.EMAIL, "richard@gmail.com").commit();
                            sp.edit().putString(ConstantSp.CONTACT, "9876543210").commit();
                            sp.edit().putString(ConstantSp.PASSWORD, "richard@123").commit();
                            sp.edit().putString(ConstantSp.GENDER, "Male").commit();
                            sp.edit().putString(ConstantSp.COLLEGEID, "1").commit();
                            sp.edit().putString(ConstantSp.COLLEGENAME, "College Name").commit();
                            sp.edit().putString(ConstantSp.STREAM, "CE").commit();
                            sp.edit().putString(ConstantSp.SEMESTER, "1").commit();
                            sp.edit().putString(ConstantSp.VOLUNTEER, "No").commit();
                            new ToastIntentClass(LoginActivity.this, DashboardNavigationActivity.class);
                        }
                    });
                    builder.setNegativeButton("Volunteer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sp.edit().putString(ConstantSp.ID, "2").commit();
                            sp.edit().putString(ConstantSp.TYPE, "Volunteer").commit();
                            sp.edit().putString(ConstantSp.NAME, "John").commit();
                            sp.edit().putString(ConstantSp.EMAIL, "john@gmail.com").commit();
                            sp.edit().putString(ConstantSp.CONTACT, "9876543211").commit();
                            sp.edit().putString(ConstantSp.PASSWORD, "john@123").commit();
                            sp.edit().putString(ConstantSp.GENDER, "Male").commit();
                            sp.edit().putString(ConstantSp.COLLEGEID, "1").commit();
                            sp.edit().putString(ConstantSp.COLLEGENAME, "College Name").commit();
                            sp.edit().putString(ConstantSp.STREAM, "CE").commit();
                            sp.edit().putString(ConstantSp.SEMESTER, "1").commit();
                            sp.edit().putString(ConstantSp.VOLUNTEER, "Yes").commit();
                            new ToastIntentClass(LoginActivity.this, VolunteerNavigationActivity.class);
                        }
                    });
                    builder.setNeutralButton("Admin", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sp.edit().putString(ConstantSp.ID, "1").commit();
                            sp.edit().putString(ConstantSp.TYPE, "Admin").commit();
                            sp.edit().putString(ConstantSp.NAME, "Admin").commit();
                            sp.edit().putString(ConstantSp.EMAIL, "admin@gmail.com").commit();
                            sp.edit().putString(ConstantSp.CONTACT, "9876543212").commit();
                            sp.edit().putString(ConstantSp.PASSWORD, "admin@123").commit();
                            sp.edit().putString(ConstantSp.GENDER, "Male").commit();
                            sp.edit().putString(ConstantSp.COLLEGEID, "1").commit();
                            sp.edit().putString(ConstantSp.COLLEGENAME, "College Name").commit();
                            sp.edit().putString(ConstantSp.STREAM, "CE").commit();
                            sp.edit().putString(ConstantSp.SEMESTER, "1").commit();
                            sp.edit().putString(ConstantSp.VOLUNTEER, "No").commit();
                            new ToastIntentClass(LoginActivity.this, AdminHomeActivity.class);

                        }
                    });
                    builder.show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ToastIntentClass(LoginActivity.this, SignupActivity.class);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ToastIntentClass(LoginActivity.this, Forgot_password.class);
            }
        });

    }

    private class loginData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("email", email.getText().toString());
            hashMap.put("password", password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.URL + "login.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equalsIgnoreCase("True")) {
                    new ToastIntentClass(LoginActivity.this, object.getString("Message"));
                    JSONArray array = object.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        sp.edit().putString(ConstantSp.ID, jsonObject.getString("id")).commit();
                        sp.edit().putString(ConstantSp.TYPE, jsonObject.getString("type")).commit();
                        sp.edit().putString(ConstantSp.NAME, jsonObject.getString("name")).commit();
                        sp.edit().putString(ConstantSp.EMAIL, jsonObject.getString("email")).commit();
                        sp.edit().putString(ConstantSp.CONTACT, jsonObject.getString("contact")).commit();
                        sp.edit().putString(ConstantSp.PASSWORD, jsonObject.getString("password")).commit();
                        sp.edit().putString(ConstantSp.GENDER, jsonObject.getString("gender")).commit();
                        sp.edit().putString(ConstantSp.COLLEGEID, jsonObject.getString("collegeId")).commit();
                        sp.edit().putString(ConstantSp.COLLEGENAME, jsonObject.getString("collegeName")).commit();
                        sp.edit().putString(ConstantSp.STREAM, jsonObject.getString("stream")).commit();
                        sp.edit().putString(ConstantSp.SEMESTER, jsonObject.getString("semester")).commit();
                        sp.edit().putString(ConstantSp.VOLUNTEER, jsonObject.getString("volunteer")).commit();
                        if (jsonObject.getString("type").equalsIgnoreCase("Admin")) {
                            new ToastIntentClass(LoginActivity.this, AdminHomeActivity.class);
                        } else {
                            if (jsonObject.getString("volunteer").equalsIgnoreCase("Yes")) {
                                new ToastIntentClass(LoginActivity.this, VolunteerNavigationActivity.class);
                            } else {
                                new ToastIntentClass(LoginActivity.this, DashboardNavigationActivity.class);
                            }
                        }
                    }
                    //onBackPressed();
                } else {
                    new ToastIntentClass(LoginActivity.this, object.getString("Message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                finish();
            }
            //onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }
}