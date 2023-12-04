package com.example.keepitall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.HashSet;
import java.util.Set;

/**
 * Adapter used for displaying the user's items (HomePage)
 */
public class HomePageAdapter extends BaseAdapter {
    // Private variables
    private Context context;
    private ItemManager itemList;
    private LayoutInflater inflater;
    private boolean isSelectionMode = false;
    private Set<Integer> selectedItems = new HashSet<>();

    /**
     * Constructor for the HomePageAdapter
     * @param context (activity)
     * @param itemList (itemManager)
     */
    public HomePageAdapter(Context context, ItemManager itemList) {
        this.context = context;
        this.itemList = itemList;
        inflater = (LayoutInflater.from(context));
    }

    public ItemManager getItemList() {
        return itemList;
    }
    public void updateItems(ItemManager newItems) {
        this.itemList = newItems;
    }

    /**
     * Gets the number of items in the itemManager
     * @return - number of items in the itemManager
     */
    @Override
    public int getCount() {
        return itemList.getAllItems().size();
    }

    /**
     * Gets the item at the specified position
     * @param position - position of the item
     * @return - item at the specified position (as an object)
     */
    @Override
    public Item getItem(int position) {
        return itemList.getAllItems().get(position);
    }

    /**
     * Gets the item's id at the specified position
     * @param position - position of the item
     * @return - item's id at the specified position (currently not used)
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Gets the view at the specified position and inflates it
     * @param position - position of the item
     * @param convertView - view
     * @param parent - parent view
     * @return - view at the specified position
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_grid, null);
        TextView name = view.findViewById(R.id.gridDataName);
        ImageView image = view.findViewById(R.id.gridDataImage);
        Item item = itemList.getItem(position);
        name.setText(itemList.getItem(position).getName());
        image.setImageResource(R.drawable.app_icon);

        if (selectedItems.contains(position)) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }

        return view;
    }

    /**
     * Toggles the selection mode for tag selection in HomePage
     */
    public void toggleSelectionMode() {
        isSelectionMode = !isSelectionMode;
        notifyDataSetChanged();
    }

    /**
     * Adds the items into the itemSelection, if item is selected again,
     * remove it from the selectedItems
     * @param position: item position
     */
    public void toggleItemSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
        } else {
            selectedItems.add(position);
        }
        notifyDataSetChanged();
    }

    /**
     * Gets the selected items from the homepage
     * @return items the user have selected
     */
    public Set<Integer> getSelectedItems() {
        return selectedItems;
    }

    /**
     * Clears the selectedItems
     */
    public void clearSelection() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    /**
     * Returns if homePage is in selection mode
     * @return boolean: if homePage is in selection mode or not
     */
    public boolean isSelectionMode() {
        return isSelectionMode;
    }
}
