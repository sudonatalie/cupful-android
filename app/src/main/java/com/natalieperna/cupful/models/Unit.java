package com.natalieperna.cupful.models;

// TODO Look at how other unit converter Java apps organize classes because this is bad
public class Unit {

    public enum Type {
        WEIGHT, VOLUME
    }

    private final String name;
    // Some terrible programming practices at work
    private final Type type;
    private final double toBase; // multiplier for to OZ (for weights) or to CUPS (for volumes)

    // Units available for conversion
    public Unit(String name, Type type, double toBase) {
        this.toBase = toBase;
        this.name = name;
        this.type = type;
    }

    private static Unit[] units = {
            new Unit("gram", Unit.Type.WEIGHT, 1),
            new Unit("kilogram", Unit.Type.WEIGHT, 1000),
            new Unit("pound (lb)", Unit.Type.WEIGHT, 453.59237),
            new Unit("ounce", Unit.Type.WEIGHT, 28.349523125),

            // TODO Better way to organize US/UK/metric
            new Unit("cup (US)", Unit.Type.VOLUME, 1),
            new Unit("cup (UK)", Unit.Type.VOLUME, 1.0566882094),
            new Unit("gallon (UK)", Unit.Type.VOLUME, 19.215198808),
            new Unit("gallon (US, dry)", Unit.Type.VOLUME, 18.618355102),
            new Unit("gallon (US, liquid)", Unit.Type.VOLUME, 16),
            new Unit("liter", Unit.Type.VOLUME, 4.2267528377),
            new Unit("milliliter", Unit.Type.VOLUME, 0.0042267528377),
            new Unit("fluid ounce (UK)", Unit.Type.VOLUME, 0.12009499255),
            new Unit("fluid ounce (US)", Unit.Type.VOLUME, 0.125),
            new Unit("pint (UK)", Unit.Type.VOLUME, 2.401899851),
            new Unit("pint (US, dry)", Unit.Type.VOLUME, 2.3272943877),
            new Unit("pint (US, liquid)", Unit.Type.VOLUME, 2),
            new Unit("quart (UK)", Unit.Type.VOLUME, 4.803799702),
            new Unit("quart (US, dry)", Unit.Type.VOLUME, 4.6545887754),
            new Unit("quart (US, liquid)", Unit.Type.VOLUME, 4),
            new Unit("tablespoon (metric)", Unit.Type.VOLUME, 0.063401292566),
            new Unit("tablespoon (UK)", Unit.Type.VOLUME, 0.060047496275),
            new Unit("tablespoon (US)", Unit.Type.VOLUME, 0.0625),
            new Unit("teaspoon (metric)", Unit.Type.VOLUME, 0.021133764189),
            new Unit("teaspoon (UK)", Unit.Type.VOLUME, 0.015011874069),
            new Unit("teaspoon (US)", Unit.Type.VOLUME, 0.020833333334)
    };

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public double getToBase() {
        return toBase;
    }

    public double getFromBase() {
        return 1.0 / toBase;
    }

    public static Unit[] getUnits() {
        return units;
    }

    @Override
    public String toString() {
        return name;
    }
}
