package com.natalieperna.cupful.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.natalieperna.cupful.R;
import com.natalieperna.cupful.adapters.IngredientAdapter;
import com.natalieperna.cupful.models.Ingredient;

import java.util.List;

public class IngredientSpinner extends AppCompatSpinner {
    private static final String TAG = "IngredientSpinner";

    private List<Ingredient> ingredients;

    public IngredientSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(getPopupContext(), android.R.layout.simple_spinner_item, ingredients);
        setAdapter(spinnerAdapter);
    }

    @Override
    public boolean performClick() {
        if (showPopup()) {
            return true;
        } else {
            Log.e(TAG, "Falling back to default spinner view.");
            return super.performClick();
        }
    }

    private boolean showPopup() {
        if (ingredients == null) {
            Log.e(TAG, "Ingredients not set.");
            return false;
        }

        AlertDialog dialog = createDialog();
        dialog.show();

        return true;
    }

    @NonNull
    private AlertDialog createDialog() {
        // Inflate dialog custom content view
        Context context = getPopupContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.searchable_spinner_dialog, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.dialog_list_view);
        SearchView searchView = (SearchView) dialogView.findViewById(R.id.search);

        // Build dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();

        // Set dialog adapter
        IngredientAdapter dialogAdapter = new IngredientAdapter(ingredients, pos -> {
            setSelection(pos);
            dialog.dismiss();
        });
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(dialogAdapter);

        // Setup search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                dialogAdapter.filter(query);
                return false;
            }
        });

        return dialog;
    }
}
