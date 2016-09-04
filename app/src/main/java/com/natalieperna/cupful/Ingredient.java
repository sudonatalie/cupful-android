package com.natalieperna.cupful;

class Ingredient {
    private int id;
    private String name;
    private double baseDensity;

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
