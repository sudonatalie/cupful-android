package com.natalieperna.cupful;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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

        AlertDialog dialog = createDialog();
        dialog.show();
    }

    private AlertDialog createDialog() {
        View dialogView = inflate(getPopupContext(), R.layout.searchable_spinner_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getPopupContext())
                .setView(dialogView)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();

        ListView listView = dialogView.findViewById(R.id.dialog_list_view);
        listView.setAdapter((ListAdapter) getAdapter());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Propagate dialog selection to Spinner and close
            getOnItemSelectedListener().onItemSelected(parent, view, position, id);
            dialog.dismiss();
        });

        return dialog;
    }
}
