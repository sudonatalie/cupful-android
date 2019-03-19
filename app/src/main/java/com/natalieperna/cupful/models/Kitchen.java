package com.natalieperna.cupful.models;

import javax.measure.quantity.Volume;
import javax.measure.quantity.VolumetricDensity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

public class Kitchen {
    public static final Unit<Volume> CUP_US = NonSI.OUNCE_LIQUID_US.times(8);
    public static final Unit<Volume> CUP_UK = NonSI.OUNCE_LIQUID_UK.times(10);
    public static final Unit<Volume> TABLESPOON_US = NonSI.OUNCE_LIQUID_US.divide(2);
    public static final Unit<Volume> TABLESPOON_UK = NonSI.OUNCE_LIQUID_UK.times(5 / 8);
    public static final Unit<Volume> TEASPOON_US = NonSI.OUNCE_LIQUID_US.divide(6);
    public static final Unit<Volume> TEASPOON_UK = NonSI.OUNCE_LIQUID_UK.divide(6);
    public static final Unit<VolumetricDensity> G_PER_CUP = SI.GRAM.divide(Kitchen.CUP_US).asType(VolumetricDensity.class);
}
