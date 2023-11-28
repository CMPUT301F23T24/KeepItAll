package com.example.keepitall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;

public class TagSelectionDialog extends DialogFragment {

    private List<String> availableTags; // Assuming you have a list of available tags
    private TagSelectionListener tagSelectionListener;

    public interface TagSelectionListener {
        void onTagsSelected(List<String> selectedTags);
    }

    public void setTagSelectionListener(TagSelectionListener listener) {
        this.tagSelectionListener = listener;
    }

    public void setAvailableTags(List<String> availableTags) {
        this.availableTags = availableTags;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Convert your tag list to a CharSequence array for the dialog
        CharSequence[] tagItems = availableTags.toArray(new CharSequence[0]);
        boolean[] checkedItems = new boolean[tagItems.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Tags")
                .setMultiChoiceItems(tagItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index, boolean isChecked) {
                        // Update the state of the checked items
                        checkedItems[index] = isChecked;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        List<String> selectedTags = new ArrayList<>();
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                selectedTags.add(availableTags.get(i));
                            }
                        }
                        if (tagSelectionListener != null) {
                            tagSelectionListener.onTagsSelected(selectedTags);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();
    }
}
