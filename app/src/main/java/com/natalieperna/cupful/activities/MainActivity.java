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
import com.natalieperna.cupful.data.IngredientReader;
import com.natalieperna.cupful.data.Units;
import com.natalieperna.cupful.models.DisplayUnit;
import com.natalieperna.cupful.models.Ingredient;

import org.jscience.physics.amount.Amount;

import java.util.List;
import java.util.Locale;

import javax.measure.quantity.Mass;
import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public class MainActivity extends Activity {

    private boolean ignoreListeners = false;

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
        setupFractionButtons();
        setupClearButton();
        setupSwapButton();
        setupSpinners();
        setupChangeListeners();
        disableSystemKeyboard();
    }

    private void setupViewWidgets() {
        ingredientInput = findViewById(R.id.ingredient);
        topUnit = findViewById(R.id.unit1);
        bottomUnit = findViewById(R.id.unit2);

        topInput = findViewById(R.id.value1);
        bottomInput = findViewById(R.id.value2);
    }

    private void setupKeyButtons() {
        setKeyButtonClickListener(R.id.button_0, KeyEvent.KEYCODE_0);
        setKeyButtonClickListener(R.id.button_1, KeyEvent.KEYCODE_1);
        setKeyButtonClickListener(R.id.button_2, KeyEvent.KEYCODE_2);
        setKeyButtonClickListener(R.id.button_3, KeyEvent.KEYCODE_3);
        setKeyButtonClickListener(R.id.button_4, KeyEvent.KEYCODE_4);
        setKeyButtonClickListener(R.id.button_5, KeyEvent.KEYCODE_5);
        setKeyButtonClickListener(R.id.button_6, KeyEvent.KEYCODE_6);
        setKeyButtonClickListener(R.id.button_7, KeyEvent.KEYCODE_7);
        setKeyButtonClickListener(R.id.button_8, KeyEvent.KEYCODE_8);
        setKeyButtonClickListener(R.id.button_9, KeyEvent.KEYCODE_9);
        setKeyButtonClickListener(R.id.button_dot, KeyEvent.KEYCODE_PERIOD);
        setKeyButtonClickListener(R.id.button_back, KeyEvent.KEYCODE_DEL);
    }

    private void setupFractionButtons() {
        setFractionButtonClickListener(R.id.button_quarter, 0.25f);
        setFractionButtonClickListener(R.id.button_third, 0.33f);
        setFractionButtonClickListener(R.id.button_half, 0.5f);
    }

    private void setupClearButton() {
        findViewById(R.id.button_back).setOnLongClickListener(v -> {
            ignoreListeners = true;

            topInput.getText().clear();
            bottomInput.getText().clear();

            ignoreListeners = false;

            return true;
        });
    }

    private void setupSwapButton() {
        findViewById(R.id.swap).setOnClickListener(v -> {
            ignoreListeners = true;

            // Swap units
            int tempPosition = topUnit.getSelectedItemPosition();
            topUnit.setSelection(bottomUnit.getSelectedItemPosition());
            bottomUnit.setSelection(tempPosition);

            // Swap values
            CharSequence tempText = topInput.getText();
            topInput.setText(bottomInput.getText());
            bottomInput.setText(tempText);

            // TODO Maintain cursor location

            ignoreListeners = false;
        });
    }

    private void setupSpinners() {
        // Show ingredients in spinner
        IngredientReader ingredientReader = new IngredientReader(this);
        List<Ingredient> ingredients = ingredientReader.getAll();
        ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ingredients);
        ingredientInput.setAdapter(ingredientAdapter);

        // Show units in spinners
        ArrayAdapter<DisplayUnit> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Units.getAll());
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topUnit.setAdapter(unitAdapter);
        bottomUnit.setAdapter(unitAdapter);

        // Set initial values
        // TODO Avoid hard-coding
        int flourIndex = 185;
        int cupUsIndex = 0;
        int gramIndex = 3;
        ingredientInput.setSelection(flourIndex);
        topUnit.setSelection(cupUsIndex);
        bottomUnit.setSelection(gramIndex);
    }

    private void setupChangeListeners() {
        // Listen for changes to ingredients/units and convert
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (ignoreListeners) return;
                convert(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        ingredientInput.setOnItemSelectedListener(listener);
        topUnit.setOnItemSelectedListener(listener);
        bottomUnit.setOnItemSelectedListener(listener);

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

    private void disableSystemKeyboard() {
        topInput.setShowSoftInputOnFocus(false);
        bottomInput.setShowSoftInputOnFocus(false);
    }

    private void setKeyButtonClickListener(final int resId, final int keyCode) {
        View button = findViewById(resId);
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        button.setOnClickListener(v ->
                getFocusedInput().dispatchKeyEvent(event));
    }

    private void setFractionButtonClickListener(final int resId, final float val) {
        View button = findViewById(resId);
        button.setOnClickListener(v -> addFraction(val));
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

    private void addFraction(float fractionValue) {
        EditText focusedInput = getFocusedInput();
        String focusString = focusedInput.getText().toString();

        double val = focusString.isEmpty() ? 0 : Double.valueOf(focusString);
        val += fractionValue;

        // Weird rounding mostly to account for +⅓ three times
        double fractional = val % 1;
        if (fractional <= 0.01 || fractional >= 0.99) {
            val = Math.round(val);
        }

        focusedInput.setText(naturalFormat(val));
        focusedInput.setSelection(focusedInput.getText().length());
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
