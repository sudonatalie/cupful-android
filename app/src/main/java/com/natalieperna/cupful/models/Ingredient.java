package com.natalieperna.cupful.models;

public class Ingredient {
    private final int id;
    private final String name;
    private final double baseDensity;

    public Ingredient(int id, String name, double baseDensity) {
        this.id = id;
        this.name = name;
        this.baseDensity = baseDensity;
    }

    public String getName() {
        return name;
    }

    public double convert(double val, Unit from, Unit to) {
        double baseVal = val * from.getToBase();
        if (from.getType() != to.getType()) {
            if (from.getType() == Unit.Type.WEIGHT) {
                baseVal /= this.baseDensity;
            } else {
                baseVal *= this.baseDensity;
            }
        }

        return to.getFromBase() * baseVal;
    }

    public String toString() {
        return name;
    }
}
