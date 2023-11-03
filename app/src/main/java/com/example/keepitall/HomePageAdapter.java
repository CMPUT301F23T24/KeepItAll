package com.example.keepitall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomePageAdapter extends BaseAdapter {
    Context context;
    ItemManager itemList;
    LayoutInflater inflater;

    public HomePageAdapter(Context context, ItemManager itemList) {
        this.context = context;
        this.itemList = itemList;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return itemList.getAllItems().size();
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
        name.setText(itemList.getItem(position).getMake());
        image.setImageResource(R.drawable.app_icon);
        return view;
    }
}
