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

    public Type getType() {
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
