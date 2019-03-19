package com.natalieperna.cupful.activities;

import android.os.Bundle;
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
import com.natalieperna.cupful.models.DisplayUnit;
import com.natalieperna.cupful.models.Ingredient;
import com.natalieperna.cupful.models.Kitchen;

import org.jscience.physics.amount.Amount;

import java.util.List;
import java.util.Locale;

import javax.measure.quantity.Mass;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

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
        Button[] button = new Button[10];
        Button buttonDot;
        ImageButton buttonSwap, buttonBackspace;
        Button buttonQuarter, buttonThird, buttonHalf;

        // Set up view widgets
        ingredientSpinner = findViewById(R.id.ingredient);
        unitSpinner1 = findViewById(R.id.unit1);
        unitSpinner2 = findViewById(R.id.unit2);

        inputView = findViewById(R.id.value1);
        outputView = findViewById(R.id.value2);

        button[0] = findViewById(R.id.button_0);
        button[1] = findViewById(R.id.button_1);
        button[2] = findViewById(R.id.button_2);
        button[3] = findViewById(R.id.button_3);
        button[4] = findViewById(R.id.button_4);
        button[5] = findViewById(R.id.button_5);
        button[6] = findViewById(R.id.button_6);
        button[7] = findViewById(R.id.button_7);
        button[8] = findViewById(R.id.button_8);
        button[9] = findViewById(R.id.button_9);

        buttonDot = findViewById(R.id.button_dot);
        buttonBackspace = findViewById(R.id.button_back);
        buttonSwap = findViewById(R.id.swap);

        buttonQuarter = findViewById(R.id.button_quarter);
        buttonThird = findViewById(R.id.button_third);
        buttonHalf = findViewById(R.id.button_half);

        // Setup database
        dbHelper = new DatabaseHelper(this);

        // Show ingredients in spinner
        List<Ingredient> ingredients = dbHelper.getIngredients();
        ArrayAdapter<Ingredient> ingredientAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, ingredients);
        ingredientAdapter.setDropDownViewResource(R.layout.spinner_layout);
        ingredientSpinner.setAdapter(ingredientAdapter);

        // Show units in spinners
        DisplayUnit[] units = {
                new DisplayUnit(SI.GRAM, "g"),
                new DisplayUnit(SI.KILOGRAM, "kg"),
                new DisplayUnit(NonSI.POUND, "lb"),
                new DisplayUnit(NonSI.OUNCE, "oz"),

                new DisplayUnit(Kitchen.CUP_US, "cup (US)"),
                new DisplayUnit(Kitchen.CUP_UK, "cup (UK)"),
                new DisplayUnit(NonSI.LITER, "L"),
                new DisplayUnit(SI.MILLI(NonSI.LITER), "mL"),
                new DisplayUnit(NonSI.OUNCE_LIQUID_US, "fl oz (US)"),
                new DisplayUnit(NonSI.OUNCE_LIQUID_UK, "fl oz (UK)"),
                new DisplayUnit(Kitchen.TABLESPOON_US, "tbsp (US)"),
                new DisplayUnit(Kitchen.TABLESPOON_UK, "tbsp (UK)"),
                new DisplayUnit(Kitchen.TEASPOON_US, "tsp (US)"),
                new DisplayUnit(Kitchen.TEASPOON_UK, "tsp (UK)"),
        };

        ArrayAdapter<DisplayUnit> unitAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, units);
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

        for (Button b : button) b.setOnClickListener(numberInputListener);

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
        fromUnit = ((DisplayUnit) fromSpinner.getSelectedItem()).getUnit();
        toUnit = ((DisplayUnit) toSpinner.getSelectedItem()).getUnit();

        // Get from value
        String fromString = fromEdit.getText().toString();
        // Prepend a 0 in case user submitted value beginning with decimal point
        fromString = "0" + fromString;
        fromVal = fromString.isEmpty() ? 0 : Double.parseDouble(fromString);

        // Convert to new value
        Amount from = Amount.valueOf(fromVal, fromUnit);
        Amount to;
        if (fromUnit.isCompatible(toUnit))
            to = from.to(toUnit);
        else {
            // Mass -> Volume
            if (fromUnit.isCompatible(Mass.UNIT)) {
                to = from.divide(ingredient.getDensity()).to(toUnit);
            }
            // Volume -> Mass
            else {
                to = from.times(ingredient.getDensity()).to(toUnit);
            }
        }
        toVal = to.getEstimatedValue();

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
        String field = inputView.getText().toString();
        if (field.length() < 10)
            inputView.append(button.getText()); // TODO There has to be a better way

        convert();
    }

    private void insertDot() {
        String field = inputView.getText().toString();
        // If empty, insert 0.
        if (field.isEmpty())
            inputView.append("0.");
            // Don't insert superfluous decimal places
        else if (!field.contains("."))
            inputView.append(".");

        convert();
    }

    private void backspace() {
        Editable field = inputView.getEditableText();
        if (field != null) {
            int length = field.length();
            if (length > 0) {
                field.delete(length - 1, length);
            }
        }

        convert();
    }

    private void erase() {
        inputView.setText("");
        outputView.setText("");
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
