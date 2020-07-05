package com.natalieperna.cupful;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;

public class SearchableSpinner extends AppCompatSpinner {
    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean performClick() {
        showPopup();
        return true;
    }

    private void showPopup() {
        SpinnerAdapter adapter = getAdapter();
        if (adapter == null) {
            return;
        }

        // TODO Try extending AlertDialog and doing the ListView stuff onCreate?
        AlertDialog.Builder builder = new AlertDialog.Builder(getPopupContext())
                .setView(R.layout.searchable_spinner_dialog)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        ListView listView = dialog.findViewById(R.id.dialog_list_view);
        listView.setAdapter((ListAdapter) adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Propagate dialog selection to Spinner and close
            getOnItemSelectedListener().onItemSelected(parent, view, position, id);
            dialog.dismiss();
        });
    }
}
