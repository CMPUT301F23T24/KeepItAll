package com.example.keepitall;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDialogFragment;

public class SortOptions extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sort_layout, null);


        builder.setView(view)
                .setTitle("Sort By:")
                .setNeutralButton("Cancel", (dialog, which) -> {})
                .setNegativeButton("Descending", (dialog, which) -> {})
                .setPositiveButton("Ascending", (dialog, which) -> {});


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
}
