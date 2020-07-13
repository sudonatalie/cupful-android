package com.natalieperna.cupful.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.natalieperna.cupful.R;
import com.natalieperna.cupful.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {
    private static final String TAG = "IngredientAdapter";

    private final List<Ingredient> allIngredients;
    private OnItemSelectedListener listener;
    private List<Ingredient> filteredIngredients;
    private Filter filter;

    public IngredientAdapter(List<Ingredient> ingredients, OnItemSelectedListener listener) {
        this.allIngredients = ingredients;
        this.listener = listener;
        this.filteredIngredients = new ArrayList<>(ingredients);
        this.filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Ingredient> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(ingredients);
                } else {
                    for (Ingredient ingredient : ingredients) {
                        if (matches(ingredient.getName(), constraint.toString())) {
                            filteredList.add(ingredient);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredIngredients.clear();
                filteredIngredients.addAll((List<Ingredient>) results.values);
                notifyDataSetChanged();
            }
        };
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
        Ingredient ingredient = filteredIngredients.get(position);
        holder.textView.setText(ingredient.getName());

        int originalPosition = allIngredients.indexOf(ingredient);
        if (originalPosition == -1) Log.e(TAG, "Ingredient not found in original list.");

        holder.itemView.setOnClickListener(view -> listener.onItemSelected(originalPosition));
    }

    @Override
    public int getItemCount() {
        return filteredIngredients.size();
    }

    public void filter(String query) {
        filter.filter(query);
    }

    /**
     * TODO: This seems fast "enough", but it'd be worth measuring and considering caching/indexing
     *
     * @param source String to search in
     * @param query  Query to search for
     * @return true iff source contains all keywords in query, case-insensitive
     */
    private boolean matches(String source, String query) {
        // Source string to search, case-insensitive
        String s = source.toLowerCase();

        // Quick: Search for whole phrase
        String phrase = query.toLowerCase().trim();
        if (s.contains(phrase)) return true;

        // Slower: Split query into individual keywords and search for all
        String[] keywords = phrase.split("\\P{L}+");
        for (String keyword : keywords) {
            if (!s.contains(keyword)) return false;
        }
        return true;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = ((TextView) itemView.findViewById(R.id.ingredient_name));
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
