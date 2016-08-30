package com.natalieperna.cupful;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

// Ref: http://stackoverflow.com/questions/1625249/android-how-to-bind-spinner-to-custom-object-list
public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    private Context context;
    private List<Ingredient> values;

    public IngredientAdapter(Context context, int textViewResourceId, List<Ingredient> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    public Ingredient getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Ingredients array) and the current position
        // You can NOW reference each method you has created in your bean object (Ingredient class)
        label.setText(getItem(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(getItem(position).getName());

        return label;
    }
}
