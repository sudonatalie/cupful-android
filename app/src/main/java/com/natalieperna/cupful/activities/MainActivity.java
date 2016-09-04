package com.natalieperna.cupful.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.natalieperna.cupful.R;
import com.natalieperna.cupful.database.DatabaseHelper;
import com.natalieperna.cupful.models.Ingredient;
import com.natalieperna.cupful.models.Unit;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    // Spinner and EditText widgets
    private Spinner ingredientSpinner, unitSpinner1, unitSpinner2;
    private EditText valEdit1, valEdit2;

    // Last focused value edit widget
    EditText focused = valEdit1;

    // Conversion direction
    boolean forward = true;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Prepare instance state and set view to main
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Local sqlite database helper
        DatabaseHelper dbHelper;

        // Button widgets
        Button button0, button1, button2, button3, button4,
                button5, button6, button7, button8, button9;
        Button buttonDot, buttonBackspace, buttonConvert;
        Button buttonQuarter, buttonThird, buttonHalf;

        // Set up view widgets
        ingredientSpinner = (Spinner) findViewById(R.id.ingredient);
        unitSpinner1 = (Spinner) findViewById(R.id.unit1);
        unitSpinner2 = (Spinner) findViewById(R.id.unit2);

        valEdit1 = (EditText) findViewById(R.id.value1);
        valEdit2 = (EditText) findViewById(R.id.value2);

        button0 = (Button) findViewById(R.id.button_0);
        button1 = (Button) findViewById(R.id.button_1);
        button2 = (Button) findViewById(R.id.button_2);
        button3 = (Button) findViewById(R.id.button_3);
        button4 = (Button) findViewById(R.id.button_4);
        button5 = (Button) findViewById(R.id.button_5);
        button6 = (Button) findViewById(R.id.button_6);
        button7 = (Button) findViewById(R.id.button_7);
        button8 = (Button) findViewById(R.id.button_8);
        button9 = (Button) findViewById(R.id.button_9);

        buttonDot = (Button) findViewById(R.id.button_dot);
        buttonBackspace = (Button) findViewById(R.id.button_back);
        buttonConvert = (Button) findViewById(R.id.convert);

        buttonQuarter = (Button) findViewById(R.id.button_quarter);
        buttonThird = (Button) findViewById(R.id.button_third);
        buttonHalf = (Button) findViewById(R.id.button_half);

        // Disallow input with keyboard for numerical fields,
        // in favour of on-screen input buttons
        valEdit1.setRawInputType(InputType.TYPE_CLASS_TEXT);
        valEdit2.setRawInputType(InputType.TYPE_CLASS_TEXT);
        valEdit1.setTextIsSelectable(true);
        valEdit2.setTextIsSelectable(true);

        // Keep current focus and direction updated with listeners
        valEdit1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focused = valEdit1;
                    // TODO Isn't there a keyword like `this` that could replace valEdit2 here?
                    forward = true;
                }
            }
        });
        valEdit2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    focused = valEdit2;
                    forward = false;
                }
            }
        });

        // Setup database
        dbHelper = new DatabaseHelper(this);

        // Show ingredients in spinner
        List<Ingredient> ingredients = dbHelper.getIngredients();
        ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ingredients);
        ingredientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientSpinner.setAdapter(ingredientAdapter);

        // Show units in spinners
        Unit[] units = Unit.getUnits();
        ArrayAdapter<Unit> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner1.setAdapter(unitAdapter);
        unitSpinner2.setAdapter(unitAdapter);

        // Setup convert button
        // TODO Remove, no longer necessary
        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convert();
            }
        });

        // Auto-update (convert) on changing units
        // Whichever unit is changed, always change the second row's value accordingly
        unitSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        unitSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Set up number input buttons
        // Auto-update (convert) on changing values
        // Whichever value is changed, the other value changes accordingly
        View.OnClickListener numberInputListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber((Button) view);
            }
        };

        button0.setOnClickListener(numberInputListener);
        button1.setOnClickListener(numberInputListener);
        button2.setOnClickListener(numberInputListener);
        button3.setOnClickListener(numberInputListener);
        button4.setOnClickListener(numberInputListener);
        button5.setOnClickListener(numberInputListener);
        button6.setOnClickListener(numberInputListener);
        button7.setOnClickListener(numberInputListener);
        button8.setOnClickListener(numberInputListener);
        button9.setOnClickListener(numberInputListener);

        // Set up dot button
        buttonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDot();
            }
        });

        // Set up backspace buttons
        // Click erases last character of focused widget
        buttonBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backspace();
            }
        });

        // Long click erases entire focused widget
        buttonBackspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                erase();
                return true;
            }
        });

        // Set up fractional input buttons
        View.OnClickListener fractionInputListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFraction((Button) view);
            }
        };

        buttonQuarter.setOnClickListener(fractionInputListener);
        buttonThird.setOnClickListener(fractionInputListener);
        buttonHalf.setOnClickListener(fractionInputListener);
    }

    private void convert() {
        // To/from value and spinner widgets
        EditText fromEdit, toEdit;
        Spinner fromSpinner, toSpinner;

        // Use forward parameter to determine "direction" of conversion
        // Forward => update lower value based on upper
        // Backward => update upper value based on lower (when lower value is the widget changing)
        if (forward) {
            fromEdit = valEdit1;
            toEdit = valEdit2;
            fromSpinner = unitSpinner1;
            toSpinner = unitSpinner2;
        } else {
            fromEdit = valEdit2;
            toEdit = valEdit1;
            fromSpinner = unitSpinner2;
            toSpinner = unitSpinner1;
        }

        // Get values from widgets
        Ingredient ingredient;
        Unit fromUnit;
        Unit toUnit;
        double fromVal;
        double toVal;

        // Get ingredients and units
        ingredient = (Ingredient) ingredientSpinner.getSelectedItem();
        fromUnit = (Unit) fromSpinner.getSelectedItem();
        toUnit = (Unit) toSpinner.getSelectedItem();

        // Get from value
        String fromString = fromEdit.getText().toString();
        fromVal = fromString.isEmpty() ? 0 : Double.parseDouble(fromString);

        // Convert to new value
        toVal = ingredient.convert(fromVal, fromUnit, toUnit);

        // Display new value
        toEdit.setText(naturalFormat(toVal));
    }

    private void insertNumber(Button button) {
        Editable field = focused.getText();
        field.append(button.getText());

        convert();
    }

    private void insertDot() {
        Editable field = focused.getText();
        if (!field.toString().contains("."))
            field.append(".");

        convert();
    }

    private void backspace() {
        Editable field = focused.getText();
        int length = field.length();
        if (length > 0) {
            field.delete(length - 1, length);
        }

        convert();
    }

    private void erase() {
        Editable field = focused.getText();

        field.clear();

        convert();
    }

    private void addFraction(Button button) {
        String focusString = focused.getText().toString();

        double val = focusString.isEmpty() ? 0 : Double.valueOf(focusString);

        switch (button.getText().toString()) {
            case "¼":
                val += 0.25;
                break;
            case "⅓":
                val += 0.33;
                break;
            case "½":
                val += 0.5;
                break;
        }

        // Weird rounding mostly to account for +⅓ three times
        double fractional = val % 1;
        if (fractional <= 0.01 || fractional >= 0.99) {
            val = Math.round(val);
        }

        focused.setText(naturalFormat(val));
        focused.setSelection(focused.getText().length());

        convert();
    }
}
