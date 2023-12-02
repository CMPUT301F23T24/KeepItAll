package com.example.keepitall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a custom DialogFragment used for selecting tags. It displays a list of available tags and allows the user to select multiple tags.
 */
public class TagSelectionDialog extends DialogFragment {

    private List<String> availableTags;
    private TagSelectionListener tagSelectionListener;

    /**
     * Interface definition for a callback to be invoked when tags are selected.
     */
    public interface TagSelectionListener {
        void onTagsSelected(List<String> selectedTags);
    }

    /**
     * Sets the listener that will receive callbacks when tags are selected.
     * @param listener The listener that will be notified of tag selections.
     */
    public void setTagSelectionListener(TagSelectionListener listener) {
        this.tagSelectionListener = listener;
    }

    /**
     * Sets the list of tags to be displayed in the dialog.
     * @param availableTags A list of tags that will be available for selection in the dialog.
     */
    public void setAvailableTags(List<String> availableTags) {
        this.availableTags = availableTags;
    }

    /**
     * Called to create the dialog itself. Constructs the dialog with multi-choice items (tags), an OK button, and a Cancel button.
     * @param savedInstanceState The last saved instance state of the Fragment, or null if this is a freshly created Fragment.
     * @return The newly created dialog.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Convert tag list to a CharSequence array for the dialog
        CharSequence[] tagItems = availableTags.toArray(new CharSequence[0]);
        boolean[] checkedItems = new boolean[tagItems.length]; // Tracks the checked state of each tag.

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Tags")
                .setMultiChoiceItems(tagItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index, boolean isChecked) {
                        // Update the checked state of each tag when the user interacts with the checkbox.
                        checkedItems[index] = isChecked;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Construct a list of selected tags based on the state of checkboxes.
                        List<String> selectedTags = new ArrayList<>();
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                selectedTags.add(availableTags.get(i));
                            }
                        }
                        // Notify the listener of the selected tags when the OK button is pressed.
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
