package com.natalieperna.cupful.models;

// TODO Look at how other unit converter Java apps organize classes because this is bad
public class Unit {

    private final String name;
    // Some terrible programming practices at work
    private final UnitType type;
    private final double toBase; // multiplier for to OZ (for weights) or to CUPS (for volumes)

    public Unit(String name, UnitType type, double toBase) {
        this.toBase = toBase;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public UnitType getType() {
        return type;
    }

    public double getToBase() {
        return toBase;
    }

    public double getFromBase() {
        return 1.0 / toBase;
    }

    @Override
    public String toString() {
        return name;
    }
}
