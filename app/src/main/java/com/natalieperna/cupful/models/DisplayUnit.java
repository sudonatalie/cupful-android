package com.natalieperna.cupful.models;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public class DisplayUnit<Q extends Quantity> {
    private final Unit<Q> unit;
    private final String name;

    public DisplayUnit(Unit<Q> unit, String name) {
        this.unit = unit;
        this.name = name;
    }

    public Unit<Q> getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return name;
    }
}
