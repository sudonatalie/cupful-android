package com.natalieperna.cupful.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.natalieperna.cupful.R;
import com.natalieperna.cupful.database.IngredientDatabase;
import com.natalieperna.cupful.models.DisplayUnit;
import com.natalieperna.cupful.models.Ingredient;
import com.natalieperna.cupful.models.Kitchen;

import org.jscience.physics.amount.Amount;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.measure.quantity.Mass;
import javax.measure.quantity.Quantity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public class MainActivity extends Activity {

    boolean ignoreListeners = false;

    private Spinner ingredientInput;
    private Spinner topUnit, bottomUnit;
    private EditText topInput, bottomInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Prepare instance state and set view to main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViewWidgets();

        setupKeyButtons();

        disableSystemKeyboard();

        setupFractionButtons();

        setupSwapButtons();

        setupSpinners();

        setupChangeListeners();
    }

    private void setupViewWidgets() {
        ingredientInput = findViewById(R.id.ingredient);
        topUnit = findViewById(R.id.unit1);
        bottomUnit = findViewById(R.id.unit2);

        topInput = findViewById(R.id.value1);
        bottomInput = findViewById(R.id.value2);
    }

    private void setupKeyButtons() {
        Map<View, Integer> keyButtons = new HashMap<>();
        keyButtons.put(findViewById(R.id.button_0), KeyEvent.KEYCODE_0);
        keyButtons.put(findViewById(R.id.button_1), KeyEvent.KEYCODE_1);
        keyButtons.put(findViewById(R.id.button_2), KeyEvent.KEYCODE_2);
        keyButtons.put(findViewById(R.id.button_3), KeyEvent.KEYCODE_3);
        keyButtons.put(findViewById(R.id.button_4), KeyEvent.KEYCODE_4);
        keyButtons.put(findViewById(R.id.button_5), KeyEvent.KEYCODE_5);
        keyButtons.put(findViewById(R.id.button_6), KeyEvent.KEYCODE_6);
        keyButtons.put(findViewById(R.id.button_7), KeyEvent.KEYCODE_7);
        keyButtons.put(findViewById(R.id.button_8), KeyEvent.KEYCODE_8);
        keyButtons.put(findViewById(R.id.button_9), KeyEvent.KEYCODE_9);
        keyButtons.put(findViewById(R.id.button_dot), KeyEvent.KEYCODE_PERIOD);
        keyButtons.put(findViewById(R.id.button_back), KeyEvent.KEYCODE_DEL);

        for (final Map.Entry<View, Integer> keyButton : keyButtons.entrySet()) {
            keyButton.getKey().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pressKey(keyButton.getValue());
                }
            });
        }
    }

    private void disableSystemKeyboard() {
        topInput.setShowSoftInputOnFocus(false);
        bottomInput.setShowSoftInputOnFocus(false);
    }

    private void setupFractionButtons() {
        View.OnClickListener fractionInputListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFraction(view);
            }
        };

        findViewById(R.id.button_quarter).setOnClickListener(fractionInputListener);
        findViewById(R.id.button_third).setOnClickListener(fractionInputListener);
        findViewById(R.id.button_half).setOnClickListener(fractionInputListener);
    }

    private void setupSwapButtons() {
        findViewById(R.id.swap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ignoreListeners = true;

                // Swap units
                int tempPosition = topUnit.getSelectedItemPosition();
                topUnit.setSelection(bottomUnit.getSelectedItemPosition());
                bottomUnit.setSelection(tempPosition);

                // Swap values
                CharSequence tempText = topInput.getText();
                topInput.setText(bottomInput.getText());
                bottomInput.setText(tempText);

                // Move cursor to end of input
                topInput.requestFocus();
                topInput.setSelection(topInput.getText().length());

                ignoreListeners = false;
            }
        });
    }

    private void setupSpinners() {
        // Show ingredients in spinner
        List<Ingredient> ingredients = IngredientDatabase.getIngredients(this);
        ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, ingredients);
        ingredientAdapter.setDropDownViewResource(R.layout.spinner_layout);
        ingredientInput.setAdapter(ingredientAdapter);

        // Show units in spinners
        DisplayUnit[] units = {
                new DisplayUnit<>(SI.GRAM, "g"),
                new DisplayUnit<>(SI.KILOGRAM, "kg"),
                new DisplayUnit<>(NonSI.POUND, "lb"),
                new DisplayUnit<>(NonSI.OUNCE, "oz"),

                new DisplayUnit<>(Kitchen.CUP_US, "cup (US)"),
                new DisplayUnit<>(Kitchen.CUP_UK, "cup (UK)"),
                new DisplayUnit<>(NonSI.LITER, "L"),
                new DisplayUnit<>(SI.MILLI(NonSI.LITER), "mL"),
                new DisplayUnit<>(NonSI.OUNCE_LIQUID_US, "fl oz (US)"),
                new DisplayUnit<>(NonSI.OUNCE_LIQUID_UK, "fl oz (UK)"),
                new DisplayUnit<>(Kitchen.TABLESPOON_US, "tbsp (US)"),
                new DisplayUnit<>(Kitchen.TABLESPOON_UK, "tbsp (UK)"),
                new DisplayUnit<>(Kitchen.TEASPOON_US, "tsp (US)"),
                new DisplayUnit<>(Kitchen.TEASPOON_UK, "tsp (UK)"),
        };

        ArrayAdapter<DisplayUnit> unitAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, units);
        unitAdapter.setDropDownViewResource(R.layout.spinner_layout);
        topUnit.setAdapter(unitAdapter);
        bottomUnit.setAdapter(unitAdapter);

        // Set initial values
        // TODO Avoid hard-coding
        int flourIndex = 191;
        int cupUsIndex = 4;
        int gramIndex = 0;
        ingredientInput.setSelection(flourIndex);
        topUnit.setSelection(cupUsIndex);
        bottomUnit.setSelection(gramIndex);
    }

    private void setupChangeListeners() {
        // Listen for changes to ingredients/units and convert
        ingredientInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (ignoreListeners) return;
                convert(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        topUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (ignoreListeners) return;
                convert(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        bottomUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (ignoreListeners) return;
                convert(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Listen for changes to input/output and convert
        topInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ignoreListeners) return;
                convert(true);
            }
        });
        bottomInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ignoreListeners) return;
                convert(false);
            }
        });
    }

    private void pressKey(int key) {
        getFocusedInput().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, key));
    }

    private void addFraction(View view) {
        EditText focusedInput = getFocusedInput();
        String focusString = focusedInput.getText().toString();

        double val = focusString.isEmpty() ? 0 : Double.valueOf(focusString);

        switch (view.getId()) {
            case R.id.button_quarter:
                val += 0.25;
                break;
            case R.id.button_third:
                val += 0.33;
                break;
            case R.id.button_half:
                val += 0.5;
                break;
        }

        // Weird rounding mostly to account for +⅓ three times
        double fractional = val % 1;
        if (fractional <= 0.01 || fractional >= 0.99) {
            val = Math.round(val);
        }

        focusedInput.setText(naturalFormat(val));
        focusedInput.setSelection(focusedInput.getText().length());
    }

    private void convert(boolean forward) {
        Spinner fromUnitSpinner, toUnitSpinner;
        EditText fromInput, toInput;
        if (forward) {
            fromUnitSpinner = topUnit;
            toUnitSpinner = bottomUnit;
            fromInput = topInput;
            toInput = bottomInput;
        } else {
            fromUnitSpinner = bottomUnit;
            toUnitSpinner = topUnit;
            fromInput = bottomInput;
            toInput = topInput;
        }

        // Get ingredients and units
        Ingredient ingredient = (Ingredient) ingredientInput.getSelectedItem();
        Unit<? extends Quantity> fromUnit = ((DisplayUnit<? extends Quantity>) fromUnitSpinner.getSelectedItem()).getUnit();
        Unit<? extends Quantity> toUnit = ((DisplayUnit<? extends Quantity>) toUnitSpinner.getSelectedItem()).getUnit();

        // Get from value
        String fromString = fromInput.getText().toString();
        // Prepend a 0 in case user submitted value beginning with decimal point
        double fromVal = Double.parseDouble("0" + fromString);

        // Convert to new value
        Amount<? extends Quantity> from = Amount.valueOf(fromVal, fromUnit);
        Amount<? extends Quantity> to;
        if (fromUnit.isCompatible(toUnit))
            to = from.to(toUnit);
        else {
            // Mass -> Volume
            if (Mass.UNIT.isCompatible(fromUnit)) {
                to = from.divide(ingredient.getDensity()).to(toUnit);
            }
            // Volume -> Mass
            else {
                to = from.times(ingredient.getDensity()).to(toUnit);
            }
        }
        double toVal = to.getEstimatedValue();

        // Display new value
        ignoreListeners = true;
        toInput.setText(naturalFormat(toVal));
        ignoreListeners = false;
    }

    private EditText getFocusedInput() {
        return bottomInput.hasFocus() ? bottomInput : topInput;
    }

    private static String naturalFormat(double d) {
        if (d == 0)
            return "";

        // Format with 2 decimal places
        // TODO Should have as many decimal places as input as maximum
        String s = String.format(Locale.US, "%.2f", d);

        // Remove trailing zeros
        s = s.replaceAll("0*$", "");

        // Remove trailing dot
        s = s.replaceAll("\\.$", "");

        return s;
    }
}
