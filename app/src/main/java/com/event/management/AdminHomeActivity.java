package com.event.management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {

    ListView admin_Listview;
    String[] activities = {"Manage Students", "Manage Volunteer", "Manage Colleges", "Manage Events", "Manage Registered Student","Notification","Reports", "Log Out"};

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        getSupportActionBar().setTitle("Admin Panel");
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        admin_Listview = findViewById(R.id.admin_list);

        AdminAdapter adminAdapter = new AdminAdapter(this, activities);
        admin_Listview.setAdapter((ListAdapter) adminAdapter);

        admin_Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                sp.edit().putString(ConstantSp.ADMIN_CLICK_POSITION, String.valueOf(position)).commit();
                switch (position) {
                    case 0:
                        Intent intent = new Intent(AdminHomeActivity.this, AdminUserListActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        startActivity(new Intent(AdminHomeActivity.this, AdminUserListActivity.class));
                        break;
                    case 2:
                        Intent intent1 = new Intent(AdminHomeActivity.this, CollegeListActivity.class);
                        startActivity(intent1);
                        break;
                    case 3:
                        Intent intent2 = new Intent(AdminHomeActivity.this, EventListActivity.class);
                        startActivity(intent2);
                        break;
                    case 4:
                        startActivity(new Intent(AdminHomeActivity.this, EventHistoryActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(AdminHomeActivity.this, NotificationActivity.class));
                        break;
                    case 6:
                        break;
                    case 7:
                        sp.edit().remove(ConstantSp.ID).commit();
                        sp.edit().remove(ConstantSp.TYPE).commit();
                        sp.edit().remove(ConstantSp.NAME).commit();
                        sp.edit().remove(ConstantSp.EMAIL).commit();
                        sp.edit().remove(ConstantSp.CONTACT).commit();
                        sp.edit().remove(ConstantSp.PASSWORD).commit();
                        sp.edit().remove(ConstantSp.GENDER).commit();
                        sp.edit().remove(ConstantSp.COLLEGEID).commit();
                        sp.edit().remove(ConstantSp.COLLEGENAME).commit();
                        sp.edit().remove(ConstantSp.STREAM).commit();
                        sp.edit().remove(ConstantSp.SEMESTER).commit();
                        sp.edit().remove(ConstantSp.VOLUNTEER).commit();
                        startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}