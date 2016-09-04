package com.natalieperna.cupful;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    DatabaseHelper dbHelper = null;

    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    Button buttonDot, buttonBackspace, buttonConvert;
    Button buttonQuarter, buttonThird, buttonHalf;

    Spinner ingredientSpinner, unitSpinner1, unitSpinner2;
    EditText valEdit1, valEdit2;

    public static String naturalFormat(double d) {
        if (d == 0)
            return "";

        // Format with 2 decimal places
        String s = String.format("%.2f", d);

        // Remove trailing zeros
        s = s.replaceAll("0*$", "");

        // Remove trailing dot
        s = s.replaceAll("\\.$", "");

        return s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up view elements
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

        ingredientSpinner = (Spinner) findViewById(R.id.ingredient);
        unitSpinner1 = (Spinner) findViewById(R.id.unit1);
        unitSpinner2 = (Spinner) findViewById(R.id.unit2);

        valEdit1 = (EditText) findViewById(R.id.value1);
        valEdit2 = (EditText) findViewById(R.id.value2);

        // Disallow input with keyboard for numerical fields
        valEdit1.setRawInputType(InputType.TYPE_CLASS_TEXT);
        valEdit2.setRawInputType(InputType.TYPE_CLASS_TEXT);
        valEdit1.setTextIsSelectable(true);
        valEdit2.setTextIsSelectable(true);

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
        List<Ingredient> ingredients = dbHelper.getIngredients();
        ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ingredients);
        ingredientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientSpinner.setAdapter(ingredientAdapter);

        // Show units in from and to spinners
        ArrayAdapter<Unit> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner1.setAdapter(unitAdapter);
        unitSpinner2.setAdapter(unitAdapter);

        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convert(true);
            }
        });

        // Auto-update (convert) on typing or changing units
        // Whichever row is changed, the other row updates accordingly (units remaining constant)

        unitSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                convert(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        unitSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                convert(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Set up number input buttons, dot, and backspace
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

        buttonDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDot();
            }
        });

        buttonBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backspace();
            }
        });

        buttonBackspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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

    private void convert(boolean forward) {
        EditText fromEdit, toEdit;
        Spinner fromSpinner, toSpinner;

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

        Ingredient ingredient;
        Unit fromUnit;
        Unit toUnit;
        double fromVal;
        double toVal;

        ingredient = (Ingredient) ingredientSpinner.getSelectedItem();
        fromUnit = (Unit) fromSpinner.getSelectedItem();
        toUnit = (Unit) toSpinner.getSelectedItem();

        String fromString = fromEdit.getText().toString();
        fromVal = fromString.isEmpty() ? 0 : Double.parseDouble(fromString);

        double baseVal = fromVal * fromUnit.toBase;
        if (fromUnit.type != toUnit.type) {
            if (fromUnit.type == UnitType.WEIGHT) {
                baseVal /= ingredient.getBaseDensity();
            } else {
                baseVal *= ingredient.getBaseDensity();
            }
        }
        toVal = toUnit.fromBase() * baseVal;

        toEdit.setText(naturalFormat(toVal));
    }

    private void insertNumber(Button button) {
        boolean forward = valEdit1.hasFocus();
        EditText focused = forward ? valEdit1 : valEdit2;

        Editable field = focused.getText();
        field.append(button.getText());

        convert(forward);
    }

    private void insertDot() {
        boolean forward = valEdit1.hasFocus();
        EditText focused = forward ? valEdit1 : valEdit2;

        Editable field = focused.getText();
        if (!field.toString().contains("."))
            field.append(".");

        convert(forward);
    }

    private void backspace() {
        boolean forward = valEdit1.hasFocus();
        EditText focused = forward ? valEdit1 : valEdit2;

        Editable field = focused.getText();
        int length = field.length();
        if (length > 0) {
            field.delete(length - 1, length);
        }

        convert(forward);
    }

    private void erase() {
        boolean forward = valEdit1.hasFocus();
        EditText focused = forward ? valEdit1 : valEdit2;

        Editable field = focused.getText();

        field.clear();

        convert(forward);
    }

    private void addFraction(Button button) {
        EditText focused = valEdit2.hasFocus() ? valEdit2 : valEdit1;

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
    }
}
