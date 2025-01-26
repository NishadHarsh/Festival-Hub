package com.event.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class EventListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<EventLists> arrayList;
    EventAdapter adapter;
    FloatingActionButton floatingActionButton1;

    SharedPreferences sp;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        getSupportActionBar().setTitle("Event Lists");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        recyclerView = findViewById(R.id.event_list_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        floatingActionButton1 = findViewById(R.id.event_list_fab1);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventListActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

        if (sp.getString(ConstantSp.TYPE, "").equalsIgnoreCase("Admin")) {
            floatingActionButton1.setVisibility(View.VISIBLE);
        } else {
            floatingActionButton1.setVisibility(View.GONE);
        }

        searchView = findViewById(R.id.event_list_searchview);

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            EventLists lists = new EventLists();
            lists.setId(String.valueOf(i));
            lists.setName("ROBOTO");
            lists.setImage(R.mipmap.ic_launcher);
            lists.setDate("15-02-2023");
            arrayList.add(lists);
        }
        adapter = new EventAdapter(EventListActivity.this, arrayList);
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

    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyHolder> {

        Context context;
        ArrayList<EventLists> arrayList;
        int iPosition;
        String sId;
        private ArrayList<EventLists> searchList;

        public EventAdapter(EventListActivity eventListActivity, ArrayList<EventLists> arrayList) {
            this.context = eventListActivity;
            this.arrayList = arrayList;
            this.searchList = new ArrayList<EventLists>();
            this.searchList.addAll(arrayList);
        }

        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(searchList);
            } else {
                for (EventLists s : searchList) {
                    if (s.getName().toLowerCase(Locale.getDefault()).contains(charText)
                            || s.getDate().toLowerCase(Locale.getDefault()).contains(charText)) {
                        arrayList.add(s);
                    }
                }
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public EventAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_event, parent, false);
            return new EventAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventAdapter.MyHolder holder, int position) {
            holder.name.setText(arrayList.get(position).getName());
            Picasso.with(context).load(ConstantSp.IMAGE_URL + arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.iv);

            if (sp.getString(ConstantSp.TYPE, "").equalsIgnoreCase("Admin")) {
                holder.delete.setVisibility(View.VISIBLE);
            } else {
                holder.delete.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.edit().putString(ConstantSp.EVENTID, arrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantSp.EVENTNAME, arrayList.get(position).getName()).commit();
                    context.startActivity(new Intent(context, EventCollegeListActivity.class));
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sId = arrayList.get(position).getId();
                    iPosition = position;
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
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

            TextView name;
            ImageView delete, iv;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.custom_event_name);
                iv = itemView.findViewById(R.id.custom_event_image);
                delete = itemView.findViewById(R.id.custom_event_delete);
            }
        }

    }
}