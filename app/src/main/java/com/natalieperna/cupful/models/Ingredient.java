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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBaseDensity() {
        return baseDensity;
    }

    public String toString() {
        return name;
    }
}
