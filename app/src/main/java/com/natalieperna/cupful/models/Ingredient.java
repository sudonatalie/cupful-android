package com.natalieperna.cupful.models;

import com.natalieperna.cupful.data.Units;

import org.jscience.physics.amount.Amount;

import javax.measure.quantity.VolumetricDensity;

public class Ingredient {
    private final String name;
    private final Amount<VolumetricDensity> density;

    public Ingredient(String name, double baseDensity) {
        this.name = name;
        this.density = Amount.valueOf(baseDensity, Units.G_PER_CUP);
    }

    public Amount<VolumetricDensity> getDensity() {
        return density;
    }

    @Override
    public String toString() {
        return name;
    }
}
