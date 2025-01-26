package com.event.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AdminUserListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<UserList> arrayList;
    UserListAdapter adapter;
    SharedPreferences sp;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        if (sp.getString(ConstantSp.ADMIN_CLICK_POSITION, "").equalsIgnoreCase("1")) {
            getSupportActionBar().setTitle("Manage Volunteer");
        } else {
            getSupportActionBar().setTitle("Manage User");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.admin_manage_user_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminUserListActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchView = findViewById(R.id.admin_manage_user_searchview);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(String.valueOf(newText))) {
                    adapter.filter("");
                } else {
                    adapter.filter(String.valueOf(newText));
                }
                return false;
            }
        });

        /*if (new ConnectionDetector(AdminUserListActivity.this).isConnectingToInternet()) {
            new getUserData().execute();
        } else {
            new ConnectionDetector(AdminUserListActivity.this).connectiondetect();
        }*/

        if (sp.getString(ConstantSp.ADMIN_CLICK_POSITION, "").equalsIgnoreCase("1")) {
            arrayList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                UserList list = new UserList();
                list.setId("1");
                list.setName("Volunteer Name");
                list.setEmail("volunteer@gmail.com");
                list.setContact("9090901234");
                list.setGender("Male");
                list.setCollegeName("College Name");
                list.setStream("Stream");
                list.setSemester("Semester");
                list.setCreatedDate("02-03-2023");
                list.setVolunteer("Yes");
                arrayList.add(list);
            }
            adapter = new UserListAdapter(AdminUserListActivity.this, arrayList);
            recyclerView.setAdapter(adapter);
        } else {
            arrayList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                UserList list = new UserList();
                list.setId("1");
                list.setName("Student Name");
                list.setEmail("student@gmail.com");
                list.setContact("9090904321");
                list.setGender("Male");
                list.setCollegeName("College Name");
                list.setStream("Stream");
                list.setSemester("Semester");
                list.setCreatedDate("02-03-2023");
                list.setVolunteer("No");
                arrayList.add(list);
            }
            adapter = new UserListAdapter(AdminUserListActivity.this, arrayList);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyHolder> {

        Context context;
        ArrayList<UserList> arrayList;
        private ArrayList<UserList> searchList;
        int iPosition;
        String sId;

        UserListAdapter(Context context, ArrayList<UserList> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
            this.searchList = new ArrayList<UserList>();
            this.searchList.addAll(arrayList);
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(searchList);
            } else {
                for (UserList s : searchList) {
                    if (s.getVolunteer().toLowerCase(Locale.getDefault()).contains(charText)
                            || s.getStream().toLowerCase(Locale.getDefault()).contains(charText)
                            || s.getSemester().toLowerCase(Locale.getDefault()).contains(charText)
                            || s.getCollegeName().toLowerCase(Locale.getDefault()).contains(charText)
                            || s.getContact().toLowerCase(Locale.getDefault()).contains(charText)
                            || s.getCreatedDate().toLowerCase(Locale.getDefault()).contains(charText)
                            || s.getEmail().toLowerCase(Locale.getDefault()).contains(charText)
                            || s.getGender().toLowerCase(Locale.getDefault()).contains(charText)
                            || s.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        arrayList.add(s);
                    }
                }
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_user, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
            holder.name.setText(arrayList.get(position).getName());
            holder.email.setText(arrayList.get(position).getEmail());
            holder.contact.setText(arrayList.get(position).getContact());
            holder.college.setText(arrayList.get(position).getCollegeName() + " ( " + arrayList.get(position).getStream() + " , Semester : " + arrayList.get(position).getSemester() + " ) ");
            holder.date.setText(arrayList.get(position).getCreatedDate());

            if (arrayList.get(position).getVolunteer().equalsIgnoreCase("Yes")) {
                holder.addVolunteer.setVisibility(View.GONE);
            } else {
                if (sp.getString(ConstantSp.ADMIN_CLICK_POSITION, "").equalsIgnoreCase("1")) {
                    holder.addVolunteer.setVisibility(View.GONE);
                } else {
                    holder.addVolunteer.setVisibility(View.VISIBLE);
                }
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
                    arrayList.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });


        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class MyHolder extends RecyclerView.ViewHolder {

            TextView name, email, contact, college, date, addVolunteer;
            ImageView delete;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.custom_user_name);
                email = itemView.findViewById(R.id.custom_user_email);
                contact = itemView.findViewById(R.id.custom_user_contact);
                college = itemView.findViewById(R.id.custom_user_college);
                delete = itemView.findViewById(R.id.custom_user_delete);
                date = itemView.findViewById(R.id.custom_user_date);
                addVolunteer = itemView.findViewById(R.id.custom_user_add_volunteer);
            }
        }
    }
}
