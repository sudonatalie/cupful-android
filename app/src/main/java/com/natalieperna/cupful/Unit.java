package com.natalieperna.cupful;

// TODO Look at how other unit converter Java apps organize classes because this is bad
public class Unit {
    String name;
    // Some terrible programming practices at work
    UnitType type;
    double toBase; // multiplier for to OZ (for weights) or to CUPS (for volumes)

    public Unit(String name, UnitType type, double toBase) {
        this.toBase = toBase;
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public double toBase() {
        return toBase;
    }

    public double fromBase() {
        return 1.0 / toBase;
    }
}
