package com.example.keepitall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter used to display the tags in the ItemTagActivity
 */
public class TagAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Tag> tags;
    private LayoutInflater inflater;

    public TagAdapter(Context context, ArrayList<Tag> tags) {
        this.context = context;
        this.tags = tags;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tags.size();
    }

    @Override
    public Tag getItem(int position) {
        return tags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.tag_grid, parent, false);

        TextView tagNameTextView = convertView.findViewById(R.id.tagName);
        ImageView tagIconImageView = convertView.findViewById(R.id.tagIcon);

        Tag tag = getItem(position);
        if (tag != null) {
            tagNameTextView.setText(tag.getTagName());
            tagIconImageView.setImageResource(R.drawable.app_icon);
        }

        return convertView;
    }
}