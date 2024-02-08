package com.example.ecm.model;

import java.time.LocalDate;

public class Stroom {
    private int id;
    private int klantnummer;
    private double tariefKwh;
    private LocalDate datumVanaf;
    private LocalDate datumTot;

    // Constructor
    public Stroom(int id, int klantnummer, double tariefKwh, LocalDate datumVanaf, LocalDate datumTot) {
        this.id = id;
        this.klantnummer = klantnummer;
        this.tariefKwh = tariefKwh;
        this.datumVanaf = datumVanaf;
        this.datumTot = datumTot;
    }

    public double getTariefKwh() {
        return tariefKwh;
    }

    public void setTariefKwh(double tariefKwh) {
        this.tariefKwh = tariefKwh;
    }

    public LocalDate getDatumVanaf() {
        return datumVanaf;
    }

    public void setDatumVanaf(LocalDate datumVanaf) {
        this.datumVanaf = datumVanaf;
    }

    public LocalDate getDatumTot() {
        return datumTot;
    }

    public void setDatumTot(LocalDate datumTot) {
        this.datumTot = datumTot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKlantnummer() {
        return klantnummer;
    }

    public void setKlantnummer(int klantnummer) {
        this.klantnummer = klantnummer;
    }
}
