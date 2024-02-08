package com.example.ecm.model;

public class Tarieven {
    private double stroomTarief;
    private double gasTarief;

    // Constructor
    public Tarieven(double stroomTarief, double gasTarief) {
        this.stroomTarief = stroomTarief;
        this.gasTarief = gasTarief;
    }

    // Getters en Setters
    public double getStroomTarief() {
        return stroomTarief;
    }

    public void setStroomTarief(double stroomTarief) {
        this.stroomTarief = stroomTarief;
    }

    public double getGasTarief() {
        return gasTarief;
    }

    public void setGasTarief(double gasTarief) {
        this.gasTarief = gasTarief;
    }
}
