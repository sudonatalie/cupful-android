package com.natalieperna.cupful;

public class Ingredient {
    private int id;
    private String name;
    private double ozPerCup;

    public Ingredient(int id, String name, double ozPerCup) {
        this.id = id;
        this.name = name;
        this.ozPerCup = ozPerCup;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getOzPerCup() {
        return ozPerCup;
    }
}
