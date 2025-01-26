package com.event.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<NotificationLists> arrayList;
    NotificationAdapter adapter;
    FloatingActionButton floatingActionButton1;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        recyclerView = findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        floatingActionButton1 = findViewById(R.id.notification_fab1);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this, AddNotificationActivity.class);
                startActivity(intent);
            }
        });

        if (sp.getString(ConstantSp.TYPE, "").equalsIgnoreCase("Admin")) {
            floatingActionButton1.setVisibility(View.VISIBLE);
        } else {
            if (sp.getString(ConstantSp.VOLUNTEER, "").equalsIgnoreCase("Yes")) {
                floatingActionButton1.setVisibility(View.VISIBLE);
            } else {
                floatingActionButton1.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NotificationLists lists = new NotificationLists();
            lists.setId(String.valueOf(i));
            lists.setMessage("New Event Added");
            lists.setEventName("Outdoor Game");
            lists.setCollegeName("College Name");
            lists.setDate("28-02-2023");
            arrayList.add(lists);
        }
        adapter = new NotificationAdapter(NotificationActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHolder> {

        Context context;
        ArrayList<NotificationLists> arrayList;
        int iPosition;
        String sId;

        public NotificationAdapter(NotificationActivity notificationActivity, ArrayList<NotificationLists> arrayList) {
            this.context = notificationActivity;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public NotificationAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_notification, parent, false);
            return new NotificationAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationAdapter.MyHolder holder, int position) {
            holder.title.setText(arrayList.get(position).getEventName() + " ( " + arrayList.get(position).getCollegeName() + " )");
            holder.message.setText(arrayList.get(position).getMessage());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sId = arrayList.get(position).getId();
                    iPosition = position;
                    Toast.makeText(context, "Deleted Succcessfully", Toast.LENGTH_SHORT).show();
                    arrayList.remove(iPosition);
                    adapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            TextView title, message;
            ImageView delete;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.custom_notification_name);
                message = itemView.findViewById(R.id.custom_notification_message);
                delete = itemView.findViewById(R.id.custom_notification_delete);
            }
        }
    }
}