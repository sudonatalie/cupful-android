package com.natalieperna.cupful;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    DatabaseHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup database
        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        // Show ingredients in spinner
        Spinner ingredientSpinner = (Spinner) findViewById(R.id.ingredient);
        List<Ingredient> ingredients = dbHelper.getIngredients();
        final IngredientAdapter ingredientAdapter = new IngredientAdapter(this, android.R.layout.simple_spinner_item, ingredients);
        //ArrayAdapter<CharSequence> ingredientAdapter = ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
        ingredientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientSpinner.setAdapter(ingredientAdapter);
        ingredientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> ingredientAdapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Ingredient currentIngredient = ingredientAdapter.getItem(position);
                // Here you can do the action you want to...
                Toast.makeText(getApplicationContext(), "Name: " + currentIngredient.getName(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> ingredientAdapter) {
            }
        });
    }
}
