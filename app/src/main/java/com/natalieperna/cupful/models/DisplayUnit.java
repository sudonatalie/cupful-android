package com.natalieperna.cupful.models;

import javax.measure.unit.Unit;

import androidx.annotation.NonNull;

public class DisplayUnit {
    private final Unit unit;
    private final String display;

    public DisplayUnit(Unit unit, String display) {
        this.unit = unit;
        this.display = display;
    }

    public Unit getUnit() {
        return unit;
    }

    @NonNull
    @Override
    public String toString() {
        return display;
    }
}
