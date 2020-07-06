package com.natalieperna.cupful;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

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
        LayoutInflater inflater = LayoutInflater.from(getPopupContext());
        View dialogView = inflater.inflate(R.layout.searchable_spinner_dialog, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.dialog_list_view);

        // Build dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getPopupContext())
                .setView(dialogView)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();

        // Set dialog adapter
        DialogAdapter dialogAdapter = new DialogAdapter(ingredients, pos -> {
            setSelection(pos);
            dialog.dismiss();
        });
        recyclerView.setAdapter(dialogAdapter);

        return dialog;
    }

    private static class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.ViewHolder> {
        private List<Ingredient> ingredients;
        private OnItemSelectedListener listener;

        public DialogAdapter(List<Ingredient> ingredients, OnItemSelectedListener listener) {
            this.ingredients = ingredients;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View ingredientView = inflater.inflate(R.layout.ingredient_item, parent, false);
            return new ViewHolder(ingredientView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Ingredient ingredient = ingredients.get(position);
            holder.textView.setText(ingredient.getName());
            holder.itemView.setOnClickListener(view -> listener.onItemSelected(position));
        }

        @Override
        public int getItemCount() {
            return ingredients.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = ((TextView) itemView.findViewById(R.id.ingredient_name));
            }
        }

        interface OnItemSelectedListener {
            void onItemSelected(int position);
        }
    }
}
