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
            new Unit("oz", Unit.Type.WEIGHT, 28.3495),
            new Unit("cups", Unit.Type.VOLUME, 1),
            new Unit("grams", Unit.Type.WEIGHT, 1),
            new Unit("tsp", Unit.Type.VOLUME, 0.0208333)
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
