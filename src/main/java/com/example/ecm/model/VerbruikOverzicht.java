package com.example.ecm.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class VerbruikOverzicht {
    private SimpleIntegerProperty klantnummer;
    private SimpleStringProperty periode;
    private SimpleDoubleProperty stroomVerbruik;
    private SimpleDoubleProperty gasVerbruik;
    private SimpleDoubleProperty kosten;

    public VerbruikOverzicht(int klantnummer, String periode, double stroomVerbruik, double gasVerbruik, double kosten) {
        this.klantnummer = new SimpleIntegerProperty(klantnummer);
        this.periode = new SimpleStringProperty(periode);
        this.stroomVerbruik = new SimpleDoubleProperty(stroomVerbruik);
        this.gasVerbruik = new SimpleDoubleProperty(gasVerbruik);
        this.kosten = new SimpleDoubleProperty(kosten);
    }

    // Getters
    public String getPeriode() {
        return periode.get();
    }

    public double getStroomVerbruik() {
        return stroomVerbruik.get();
    }

    public double getGasVerbruik() {
        return gasVerbruik.get();
    }

    public double getKosten() {
        return kosten.get();
    }

    public SimpleStringProperty periodeProperty() {
        return periode;
    }


    public SimpleDoubleProperty stroomVerbruikProperty() {
        return stroomVerbruik;
    }

    public SimpleDoubleProperty gasVerbruikProperty() {
        return gasVerbruik;
    }

    public SimpleDoubleProperty kostenProperty() {
        return kosten;
    }

    public void setPeriode(String periode) {
        this.periode.set(periode);
    }

    public void setStroomVerbruik(double stroomVerbruik) {
        this.stroomVerbruik.set(stroomVerbruik);
    }

    public void setGasVerbruik(double gasVerbruik) {
        this.gasVerbruik.set(gasVerbruik);
    }

    public void setKosten(double kosten) {
        this.kosten.set(kosten);
    }

    public int getKlantnummer() {
        return klantnummer.get();
    }

    public SimpleIntegerProperty klantnummerProperty() {
        return klantnummer;
    }

    public void setKlantnummer(int klantnummer) {
        this.klantnummer.set(klantnummer);
    }
}

