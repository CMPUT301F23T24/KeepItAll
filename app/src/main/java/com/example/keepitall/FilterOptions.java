package com.example.keepitall;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDialogFragment;

public class FilterOptions extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_layout, null);


        builder.setView(view)
                .setTitle("Filter By:")
                // Creates the cancel button
                .setNegativeButton("Cancel", (dialogInterface, which) -> {})
                // Creates the confirm button
                .setPositiveButton("Confirm", (dialogInterface, which) -> {});

        AlertDialog dialog = builder.create();

        // Change button text colors after the dialog is shown
        dialog.setOnShowListener(dialogInterface -> {
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            negativeButton.setTextColor(Color.BLACK);
            positiveButton.setTextColor(Color.BLACK);
        });
        return dialog;
    }
}
