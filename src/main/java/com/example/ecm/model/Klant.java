package com.example.ecm.model;

public class Klant {
    private int klantnummer;
    private String voornaam;
    private String achternaam;
    private double jaarlijksVoorschot;

    public Klant(int klantnummer, String voornaam, String achternaam, double jaarlijksVoorschot) {
        this.klantnummer = klantnummer;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.jaarlijksVoorschot = jaarlijksVoorschot;
    }


    public int getKlantnummer() {
        return klantnummer;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public double getJaarlijksVoorschot() {
        return jaarlijksVoorschot;
    }

}
