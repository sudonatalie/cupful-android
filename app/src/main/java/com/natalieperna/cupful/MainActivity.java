package com.natalieperna.cupful;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

        // Units available for conversion
        // TODO Store units in database
        Unit[] units = {
                new Unit("oz", UnitType.WEIGHT, 28.3495),
                new Unit("cups", UnitType.VOLUME, 1),
                new Unit("grams", UnitType.WEIGHT, 1),
                new Unit("tsp", UnitType.VOLUME, 0.0208333)
        };

        // Setup database
        dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        // Show ingredients in spinner
        final Spinner ingredientSpinner = (Spinner) findViewById(R.id.ingredient);
        List<Ingredient> ingredients = dbHelper.getIngredients();
        ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ingredients);
        ingredientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientSpinner.setAdapter(ingredientAdapter);

        // Show units in from and to spinners
        final Spinner fromSpinner = (Spinner) findViewById(R.id.fromUnit);
        final Spinner toSpinner = (Spinner) findViewById(R.id.toUnit);
        ArrayAdapter<Unit> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(unitAdapter);
        toSpinner.setAdapter(unitAdapter);

        Button button = (Button) findViewById(R.id.convert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient ingredient;
                Unit fromUnit;
                Unit toUnit;
                double fromVal;
                double toVal;

                ingredient = (Ingredient) ingredientSpinner.getSelectedItem();
                fromUnit = (Unit) fromSpinner.getSelectedItem();
                toUnit = (Unit) toSpinner.getSelectedItem();
                fromVal = Double.parseDouble(((EditText) findViewById(R.id.fromValue)).getText().toString());

                double baseVal = fromVal * fromUnit.toBase;
                if (fromUnit.type != toUnit.type) {
                    if (fromUnit.type == UnitType.WEIGHT) {
                        baseVal /= ingredient.getBaseDensity();
                    } else {
                        baseVal *= ingredient.getBaseDensity();
                    }
                }
                toVal = toUnit.fromBase() * baseVal;

                EditText display = (EditText) findViewById(R.id.toValue);
                display.setText(String.format("%f", toVal));
            }
        });
    }
}
