package com.example.ecm.model;

import java.time.LocalDate;

public class Gas {
    private int id;
    private int klantnummer;
    private double tariefGas;
    private LocalDate datumVanaf;
    private LocalDate datumTot;

    // Constructor
    public Gas(int id, int klantnummer, double tariefGas, LocalDate datumVanaf, LocalDate datumTot) {
        this.id = id;
        this.klantnummer = klantnummer;
        this.tariefGas = tariefGas;
        this.datumVanaf = datumVanaf;
        this.datumTot = datumTot;
    }


    public double getTariefGas() {
        return tariefGas;
    }

    public void setTariefGas(double tariefGas) {
        this.tariefGas = tariefGas;
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
