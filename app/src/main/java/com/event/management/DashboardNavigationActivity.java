package com.event.management;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.abdulhakeem.seemoretextview.SeeMoreTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sp;

    RecyclerView recyclerView;
    ArrayList<EventLists> arrayList;
    EventAdapter adapter;
    FloatingActionButton floatingActionButton1;

    RecyclerView upcomingRecyclerView;
    ArrayList<EventCollegeLists> upcomingArrayList;
    EventCollegeAdapter upcomingAdapter;

    RelativeLayout upcomingLayout;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_navigation);
        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View hView = navigationView.getHeaderView(0);
        TextView header_name = (TextView) hView.findViewById(R.id.header_name);
        header_name.setText(sp.getString(ConstantSp.NAME, ""));

        TextView header_email = (TextView) hView.findViewById(R.id.header_email);
        header_email.setText(sp.getString(ConstantSp.EMAIL, ""));

        navigationView.setNavigationItemSelectedListener(this);

        upcomingLayout = findViewById(R.id.content_dashboard_navigation_upcoming_layout);
        recyclerView = findViewById(R.id.content_event_list_recyclerview);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        upcomingRecyclerView = findViewById(R.id.content_dashboard_navigation_upcoming_recyclerview);
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(DashboardNavigationActivity.this));
        upcomingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        upcomingRecyclerView.setNestedScrollingEnabled(false);

        floatingActionButton1 = findViewById(R.id.content_event_list_fab1);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardNavigationActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
        });

        if (sp.getString(ConstantSp.TYPE, "").equalsIgnoreCase("Admin")) {
            floatingActionButton1.setVisibility(View.VISIBLE);
        } else {
            floatingActionButton1.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        upcomingLayout.setVisibility(View.VISIBLE);
        upcomingArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            EventCollegeLists lists = new EventCollegeLists();
            lists.setId(String.valueOf(i));
            lists.setName("GAMING");
            lists.setCollegeName("College Name");
            lists.setEventName("GAMING");
            lists.setPrice("150");
            lists.setMaxAllowed("2");
            lists.setEventDate("10-02-2023");
            lists.setEventTime("2:30 PM");
            lists.setDescription("GAMING Event Description");
            lists.setDate("20-02-2023");
            upcomingArrayList.add(lists);
        }
        upcomingAdapter = new EventCollegeAdapter(DashboardNavigationActivity.this, upcomingArrayList);
        upcomingRecyclerView.setAdapter(upcomingAdapter);

        arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            EventLists lists = new EventLists();
            lists.setId(String.valueOf(i));
            lists.setName("ROBOTO");
            lists.setImage(R.mipmap.ic_launcher);
            lists.setDate("15-04-2023");
            arrayList.add(lists);
        }
        adapter = new EventAdapter(DashboardNavigationActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
    }

    private class EventCollegeAdapter extends RecyclerView.Adapter<EventCollegeAdapter.MyHolder> {

        Context context;
        ArrayList<EventCollegeLists> upcomingArrayList;
        int iPosition;
        String sId;

        public EventCollegeAdapter(Context eventCollegeListActivity, ArrayList<EventCollegeLists> upcomingArrayList) {
            this.context = eventCollegeListActivity;
            this.upcomingArrayList = upcomingArrayList;
        }

        @NonNull
        @Override
        public EventCollegeAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_event_college, parent, false);
            return new EventCollegeAdapter.MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventCollegeAdapter.MyHolder holder, int position) {
            holder.collegeName.setText(upcomingArrayList.get(position).getCollegeName());
            holder.eventName.setText(upcomingArrayList.get(position).getEventName() + " ( " + context.getResources().getString(R.string.Rupees) + upcomingArrayList.get(position).getPrice() + " )");
            holder.maxAllowed.setText(upcomingArrayList.get(position).getMaxAllowed());
            holder.eventDate.setText(upcomingArrayList.get(position).getEventDate());
            holder.eventTime.setText(upcomingArrayList.get(position).getEventTime());

            holder.description.setContent(upcomingArrayList.get(position).getDescription());
            holder.description.setTextMaxLength(100);
            holder.description.toggle();
            holder.description.expandText(false);
            holder.description.setSeeMoreTextColor(android.R.color.holo_red_dark);
            holder.description.setSeeMoreText("ShowMore", "ShowLess");

            if (sp.getString(ConstantSp.TYPE, "").equalsIgnoreCase("Admin")) {
                holder.register.setVisibility(View.GONE);
                holder.delete.setVisibility(View.VISIBLE);
            } else {
                holder.register.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.GONE);
            }

            holder.register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.edit().putString(ConstantSp.EVENTCOLLEGEID, upcomingArrayList.get(position).getId()).commit();
                    sp.edit().putString(ConstantSp.EVENTCOLLEGEPRICE, upcomingArrayList.get(position).getPrice()).commit();
                    sp.edit().putString(ConstantSp.EVENTMAXALLOW, upcomingArrayList.get(position).getMaxAllowed()).commit();
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sId = upcomingArrayList.get(position).getId();
                    iPosition = position;
                    if (new ConnectionDetector(context).isConnectingToInternet()) {
                        new deleteData().execute();
                    } else {
                        new ConnectionDetector(context).connectiondetect();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return upcomingArrayList.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {

            TextView collegeName, eventName, maxAllowed, eventDate, eventTime, register;
            SeeMoreTextView description;
            ImageView delete;

            public MyHolder(@NonNull View itemView) {
                super(itemView);
                collegeName = itemView.findViewById(R.id.custom_event_college_name);
                eventName = itemView.findViewById(R.id.custom_event_college_event_name);
                maxAllowed = itemView.findViewById(R.id.custom_event_college_max_allowed);
                eventDate = itemView.findViewById(R.id.custom_event_college_event_date);
                eventTime = itemView.findViewById(R.id.custom_event_college_event_time);
                description = itemView.findViewById(R.id.custom_event_college_description);
                delete = itemView.findViewById(R.id.custom_event_college_event_delete);
                register = itemView.findViewById(R.id.custom_event_college_register);
            }
        }

        private class deleteData extends AsyncTask<String, String, String> {

            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(context);
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", sId);
                return new MakeServiceCall().MakeServiceCall(ConstantSp.URL + "deleteEventCollege.php", MakeServiceCall.POST, hashMap);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("Status").equalsIgnoreCase("True")) {
                        Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        upcomingArrayList.remove(iPosition);
                        upcomingAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyHolder> {

        Context context;
        ArrayList<EventLists> arrayList;
        int iPosition;
        String sId;

        public EventAdapter(Context eventListActivity, ArrayList<EventLists> arrayList) {
            this.context = eventListActivity;
            this.arrayList = arrayList;
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
                    if (new ConnectionDetector(context).isConnectingToInternet()) {
                        new deleteData().execute();
                    } else {
                        new ConnectionDetector(context).connectiondetect();
                    }
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

        private class deleteData extends AsyncTask<String, String, String> {

            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(context);
                pd.setMessage("Please Wait...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", sId);
                return new MakeServiceCall().MakeServiceCall(ConstantSp.URL + "deleteEvent.php", MakeServiceCall.POST, hashMap);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                pd.dismiss();
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("Status").equalsIgnoreCase("True")) {
                        Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        arrayList.remove(iPosition);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(DashboardNavigationActivity.this, DashboardNavigationActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        if (id == R.id.nav_college_map) {
            startActivity(new Intent(DashboardNavigationActivity.this, CollegeListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        if (id == R.id.nav_order_history) {
            startActivity(new Intent(DashboardNavigationActivity.this, EventHistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        if (id == R.id.nav_profile) {
            startActivity(new Intent(DashboardNavigationActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        if (id == R.id.nav_change_password) {
            startActivity(new Intent(DashboardNavigationActivity.this, Change_password.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        if (id == R.id.nav_notification) {
            Intent intent = new Intent(DashboardNavigationActivity.this, NotificationActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_logout) {
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
            startActivity(new Intent(DashboardNavigationActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

}
