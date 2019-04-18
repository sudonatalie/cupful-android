package com.natalieperna.cupful.models;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

public class DisplayUnit<Q extends Quantity> {
    private final Unit<Q> unit;
    private final String display;

    public DisplayUnit(Unit<Q> unit, String display) {
        this.unit = unit;
        this.display = display;
    }

    public Unit<Q> getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return display;
    }
}
