package com.example.keepitall;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class SortOptions extends AppCompatDialogFragment {
    private SortOptionsListener listener;
    private String selectedSort;
    private String selectedSortOrder = "ASCENDING";
    private Button previouslySelectedButton;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sort_layout, null);

        // Setup Buttons
        Button sortMakeButton = view.findViewById(R.id.sortMakeButton);
        Button sortTagButton = view.findViewById(R.id.sortTagButton);
        Button sortDescButton = view.findViewById(R.id.sortDescriptionButton);
        Button sortDateButton = view.findViewById(R.id.sortDateButton);
        Button sortValueButton = view.findViewById(R.id.sortValueButton);

        // Set click listeners for each button
        sortMakeButton.setOnClickListener(v -> handleSortSelection((Button) v, "MAKE"));
        sortTagButton.setOnClickListener(v -> handleSortSelection((Button) v, "TAG"));
        sortDescButton.setOnClickListener(v -> handleSortSelection((Button) v,"DESCRIPTION"));
        sortDateButton.setOnClickListener(v -> handleSortSelection((Button) v, "DATE"));
        sortValueButton.setOnClickListener(v -> handleSortSelection((Button) v, "VALUE"));

        builder.setView(view)
                .setTitle("Sort By:")
                .setNeutralButton("Cancel", (dialog, which) -> {})
                .setNegativeButton("Descending", (dialog, which) -> {
                    selectedSortOrder = "DESCENDING";
                    performSort();
                })
                .setPositiveButton("Ascending", (dialog, which) -> {
                    selectedSortOrder = "ASCENDING";
                    performSort();
                });


        AlertDialog dialog = builder.create();

        // Change button text colors after the dialog is shown
        dialog.setOnShowListener(dialogInterface -> {
            Button cancelButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            Button ascendingButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button descendingButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            cancelButton.setTextColor(Color.BLACK);
            ascendingButton.setTextColor(Color.BLACK);
            descendingButton.setTextColor(Color.BLACK);
        });
        return dialog;
    }

    public void setSelectedSort(String selectedSort) {
        this.selectedSort = selectedSort;
    }

    public void performSort() {
        if (listener != null && selectedSort != null) {
            listener.onSortOptionSelected(selectedSort, selectedSortOrder);
        }
    }

    private void handleSortSelection(Button selectedButton, String sortType) {
        // Reset the background of the previously selected button
        if (previouslySelectedButton != null) {
            previouslySelectedButton.setBackgroundResource(R.drawable.green_button);
        }

        // Update the background of the newly selected button
        selectedButton.setBackgroundResource(R.drawable.gray_button);

        // Update the selected sort type
        setSelectedSort(sortType);

        // Keep a reference to the currently selected button
        previouslySelectedButton = selectedButton;
    }
    public interface SortOptionsListener {
        void onSortOptionSelected(String sortBy, String order);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SortOptionsListener) {
            listener = (SortOptionsListener) context;
        } else {
            throw new ClassCastException(context + " must implement SortOptionsListener");
        }
    }
}
