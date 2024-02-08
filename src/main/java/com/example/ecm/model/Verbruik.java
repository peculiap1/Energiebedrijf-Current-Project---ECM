package com.example.ecm.model;

import java.time.LocalDate;

public class Verbruik {
    private int klantnummer;
    private double stroomInKwh;
    private double gasInM3;
    private LocalDate datumStartPeriode;
    private LocalDate datumEindPeriode;

    // Constructor
    public Verbruik(int klantnummer, double stroomInKwh, double gasInM3, LocalDate datumStartPeriode, LocalDate datumEindPeriode) {
        this.klantnummer = klantnummer;
        this.stroomInKwh = stroomInKwh;
        this.gasInM3 = gasInM3;
        this.datumStartPeriode = datumStartPeriode;
        this.datumEindPeriode = datumEindPeriode;
    }

    // Getters en Setters

    public double getStroomInKwh() {
        return stroomInKwh;
    }

    public void setStroomInKwh(double stroomInKwh) {
        this.stroomInKwh = stroomInKwh;
    }

    public double getGasInM3() {
        return gasInM3;
    }

    public void setGasInM3(double gasInM3) {
        this.gasInM3 = gasInM3;
    }

    public LocalDate getDatumStartPeriode() {
        return datumStartPeriode;
    }

    public void setDatumStartPeriode(LocalDate datumStartPeriode) {
        this.datumStartPeriode = datumStartPeriode;
    }

    public LocalDate getDatumEindPeriode() {
        return datumEindPeriode;
    }

    public void setDatumEindPeriode(LocalDate datumEindPeriode) {
        this.datumEindPeriode = datumEindPeriode;
    }

    public int getKlantnummer() {
        return klantnummer;
    }

    public void setKlantnummer(int klantnummer) {
        this.klantnummer = klantnummer;
    }
}
