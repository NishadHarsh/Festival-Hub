package com.event.management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdminAdapter extends BaseAdapter {
    Context context;
    String[] admin_activity;

    public AdminAdapter(Context admin_home, String[] activities) {
        this.context = admin_home;
        this.admin_activity = activities;
    }

    @Override
    public int getCount() {
        return admin_activity.length;
    }

    @Override
    public Object getItem(int position) {
        return admin_activity[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.admin_layout, null);

        TextView name = view.findViewById(R.id.pro_name);
        name.setText(admin_activity[position]);

        return view;
    }
}
