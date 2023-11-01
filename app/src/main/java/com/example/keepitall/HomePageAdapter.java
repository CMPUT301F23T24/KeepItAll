package com.example.keepitall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePageAdapter extends BaseAdapter {
    Context context;
    Item[] items;
    LayoutInflater inflater;

    public HomePageAdapter(Context context, Item[] items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_grid, null);
        TextView name = view.findViewById(R.id.gridDataName);
        ImageView image = view.findViewById(R.id.gridDataImage);
        name.setText(items[position].getMake());
        image.setImageResource(R.drawable.app_icon);
        return view;
    }
}
