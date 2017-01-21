package com.natalieperna.cupful.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.natalieperna.cupful.R;
import com.natalieperna.cupful.database.DatabaseHelper;
import com.natalieperna.cupful.models.Ingredient;
import com.natalieperna.cupful.models.Unit;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    // Spinner and TextView widgets
    private Spinner ingredientSpinner, unitSpinner1, unitSpinner2;
    private TextView inputView, outputView;

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
        Button buttonDot, buttonBackspace;
        ImageButton buttonSwap;
        Button buttonQuarter, buttonThird, buttonHalf;

        // Set up view widgets
        ingredientSpinner = (Spinner) findViewById(R.id.ingredient);
        unitSpinner1 = (Spinner) findViewById(R.id.unit1);
        unitSpinner2 = (Spinner) findViewById(R.id.unit2);

        inputView = (TextView) findViewById(R.id.value1);
        outputView = (TextView) findViewById(R.id.value2);

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
        buttonSwap = (ImageButton) findViewById(R.id.swap);

        buttonQuarter = (Button) findViewById(R.id.button_quarter);
        buttonThird = (Button) findViewById(R.id.button_third);
        buttonHalf = (Button) findViewById(R.id.button_half);

        // Setup database
        dbHelper = new DatabaseHelper(this);

        // Show ingredients in spinner
        List<Ingredient> ingredients = dbHelper.getIngredients();
        ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, ingredients);
        ingredientAdapter.setDropDownViewResource(R.layout.spinner_layout);
        ingredientSpinner.setAdapter(ingredientAdapter);

        // Show units in spinners
        Unit[] units = Unit.getUnits();
        ArrayAdapter<Unit> unitAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, units);
        unitAdapter.setDropDownViewResource(R.layout.spinner_layout);
        unitSpinner1.setAdapter(unitAdapter);
        unitSpinner2.setAdapter(unitAdapter);

        // Set initial values
        // TODO Avoid hard-coding
        ingredientSpinner.setSelection(191); // Flour, all-purpose
        unitSpinner1.setSelection(4); // "cup (US)"
        unitSpinner2.setSelection(0); // "gram"

        // Setup swap button
        buttonSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapInputOutput();
            }
        });

        // Auto-update (convert) on changing ingredient/units
        AdapterView.OnItemSelectedListener spinnerInputListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        ingredientSpinner.setOnItemSelectedListener(spinnerInputListener);

        // Whichever unit is changed, always change the second row's value accordingly
        unitSpinner1.setOnItemSelectedListener(spinnerInputListener);
        unitSpinner2.setOnItemSelectedListener(spinnerInputListener);

        // Set up number input buttons
        // Auto-update (convert) on changing input
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
        // Click erases last character of inputView widget
        buttonBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backspace();
            }
        });

        // Long click erases entire inputView widget
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
                addFraction(view);
            }
        };

        buttonQuarter.setOnClickListener(fractionInputListener);
        buttonThird.setOnClickListener(fractionInputListener);
        buttonHalf.setOnClickListener(fractionInputListener);
    }

    private void convert() {
        // To/from value and spinner widgets
        TextView fromEdit, toEdit;
        Spinner fromSpinner, toSpinner;

        // TODO Cleanup unnecessary vars
        fromEdit = inputView;
        toEdit = outputView;
        fromSpinner = unitSpinner1;
        toSpinner = unitSpinner2;

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
        // Prepend a 0 in case user submitted value beginning with decimal point
        fromString = "0" + fromString;
        fromVal = fromString.isEmpty() ? 0 : Double.parseDouble(fromString);

        // Convert to new value
        toVal = ingredient.convert(fromVal, fromUnit, toUnit);

        // Display new value
        toEdit.setText(naturalFormat(toVal));
    }

    private void swapInputOutput() {
        CharSequence tmpVal = inputView.getText();
        inputView.setText(outputView.getText());
        outputView.setText(tmpVal);

        int tmpUnit = unitSpinner1.getSelectedItemPosition();
        unitSpinner1.setSelection(unitSpinner2.getSelectedItemPosition());
        unitSpinner2.setSelection(tmpUnit);
    }

    // TODO Listen for changes to EditText widgets rather than calling convert all over the place

    private void insertNumber(Button button) {
        Editable field = inputView.getEditableText();
        if (field.length() < 10)
            field.append(button.getText()); // TODO There has to be a better way

        convert();
    }

    private void insertDot() {
        Editable field = inputView.getEditableText();
        // If empty, insert 0.
        if (field.toString().isEmpty())
            field.append("0.");
        // Don't insert superfluous decimal places
        else if (!field.toString().contains("."))
            field.append(".");

        convert();
    }

    private void backspace() {
        Editable field = inputView.getEditableText();
        int length = field.length();
        if (length > 0) {
            field.delete(length - 1, length);
        }

        convert();
    }

    private void erase() {
        Editable field = inputView.getEditableText();

        field.clear();

        convert();
    }

    private void addFraction(View view) {
        String focusString = inputView.getText().toString();

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

        inputView.setText(naturalFormat(val));

        convert();
    }
}
