package com.natalieperna.cupful.data;

import com.natalieperna.cupful.models.DisplayUnit;

import javax.measure.quantity.Volume;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public class Units {
    // Custom volume units
    public static final Unit<Volume> TABLESPOON_UK = NonSI.OUNCE_LIQUID_UK.divide(1.6);
    public static final Unit<Volume> TEASPOON_UK = TABLESPOON_UK.divide(3);
    public static final Unit<Volume> TABLESPOON_US = NonSI.OUNCE_LIQUID_US.divide(2);
    public static final Unit<Volume> TEASPOON_US = TABLESPOON_US.divide(3);
    public static final Unit<Volume> CUP_UK = NonSI.OUNCE_LIQUID_UK.times(10);
    public static final Unit<Volume> CUP_US = NonSI.OUNCE_LIQUID_US.times(8);

    // Density unit
    public static final Unit<VolumetricDensity> G_PER_CUP = SI.GRAM.divide(CUP_US).asType(VolumetricDensity.class);

    private static final DisplayUnit[] UNITS = {
            new DisplayUnit<>(CUP_US, "cup (US)"),
            new DisplayUnit<>(TABLESPOON_US, "tbsp (US)"),
            new DisplayUnit<>(TEASPOON_US, "tsp (US)"),
            new DisplayUnit<>(SI.GRAM, "g"),
            new DisplayUnit<>(SI.KILOGRAM, "kg"),
            new DisplayUnit<>(NonSI.OUNCE, "oz"),
            new DisplayUnit<>(NonSI.POUND, "lb"),
            new DisplayUnit<>(SI.MILLI(NonSI.LITER), "mL"),
            new DisplayUnit<>(NonSI.LITER, "L"),
            new DisplayUnit<>(NonSI.OUNCE_LIQUID_US, "fl oz (US)"),
            new DisplayUnit<>(CUP_UK, "cup (UK)"),
            new DisplayUnit<>(TABLESPOON_UK, "tbsp (UK)"),
            new DisplayUnit<>(TEASPOON_UK, "tsp (UK)"),
            new DisplayUnit<>(NonSI.OUNCE_LIQUID_UK, "fl oz (UK)"),
    };

    public static DisplayUnit[] getAll() {
        return UNITS;
    }
}
