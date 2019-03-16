package com.natalieperna.cupful.models;

public class Ingredient {
    private final String name;
    private final double baseDensity;

    public Ingredient(String name, double baseDensity) {
        this.name = name;
        this.baseDensity = baseDensity;
    }

    // TODO Optimize
    public double convert(double val, Unit from, Unit to) {
        // Convert input value to base unit
        double baseVal = val * from.getToBase();

        // If converting from weight->volume or vice versa,
        // convert to base unit of other type
        if (from.getType() != to.getType()) {
            if (from.getType() == Unit.Type.VOLUME) {
                baseVal *= this.baseDensity;
            } else {
                baseVal /= this.baseDensity;
            }
        }

        // Convert base value to goal unit
        return to.getFromBase() * baseVal;
    }

    @Override
    public String toString() {
        return name;
    }
}
