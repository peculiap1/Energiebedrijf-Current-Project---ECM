package com.example.ecm.db;

import com.example.ecm.model.Klant;
import com.example.ecm.model.Tarieven;
import com.example.ecm.model.Verbruik;
import com.example.ecm.model.VerbruikOverzicht;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    // Database connectiegegevens
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/ecm";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";

    // Verkrijgt een connectie met de database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    // Voegt een nieuwe klant toe aan de database
    public void voegKlantToe(Klant klant) throws SQLException {
        String sql = "INSERT INTO Klanten (klantnummer, voornaam, achternaam, jaarlijksVoorschot) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, klant.getKlantnummer());
            pstmt.setString(2, klant.getVoornaam());
            pstmt.setString(3, klant.getAchternaam());
            pstmt.setDouble(4, klant.getJaarlijksVoorschot());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Specifieke foutafhandeling voor duplicaat klantnummer
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Klantnummer is al bezet. Kies een ander klantnummer.");
            } else {
                throw e;
            }
        }
    }

    // Haalt een klant op uit de database op basis van klantnummer
    public Klant haalKlantOp(int klantnummer) {
        String sql = "SELECT * FROM Klanten WHERE klantnummer = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, klantnummer);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Klant(rs.getInt("klantnummer"), rs.getString("voornaam"), rs.getString("achternaam"), rs.getDouble("jaarlijksVoorschot"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Voegt verbruiksgegevens toe voor een klant
    public void voegVerbruikToe(Verbruik verbruik) {
        String sql = "INSERT INTO verbruik (klantnummer, stroomVerbruik, gasVerbruik, verbruikStartDatum, verbruikEindDatum) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, verbruik.getKlantnummer());
            pstmt.setDouble(2, verbruik.getStroomInKwh());
            pstmt.setDouble(3, verbruik.getGasInM3());
            pstmt.setDate(4, Date.valueOf(verbruik.getDatumStartPeriode()));
            pstmt.setDate(5, Date.valueOf(verbruik.getDatumEindPeriode()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Kan verbruiksgegevens niet opslaan: " + e.getMessage());
        }
    }

    // Voegt tariefinformatie toe voor een klant
    public void voegTarievenToe(int klantnummer, double stroomTarief, LocalDate stroomVanaf, LocalDate stroomTot, double gasTarief, LocalDate gasVanaf, LocalDate gasTot) {
        // Aparte SQL statements voor stroom en gas tarieven
        String stroomSql = "INSERT INTO Stroom (klantnummer, tariefKwh, datumVanaf, datumTot) VALUES (?, ?, ?, ?)";
        String gasSql = "INSERT INTO Gas (klantnummer, tariefGas, datumVanaf, datumTot) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stroomPstmt = conn.prepareStatement(stroomSql);
             PreparedStatement gasPstmt = conn.prepareStatement(gasSql)) {

            stroomPstmt.setInt(1, klantnummer);
            stroomPstmt.setDouble(2, stroomTarief);
            stroomPstmt.setDate(3, Date.valueOf(stroomVanaf));
            stroomPstmt.setDate(4, Date.valueOf(stroomTot));
            stroomPstmt.executeUpdate();

            gasPstmt.setInt(1, klantnummer);
            gasPstmt.setDouble(2, gasTarief);
            gasPstmt.setDate(3, Date.valueOf(gasVanaf));
            gasPstmt.setDate(4, Date.valueOf(gasTot));
            gasPstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Kan tarieven niet opslaan: " + e.getMessage());
        }
    }

    // Haalt de meest recente tarieven op voor een klant
    public Tarieven haalRecenteTarievenOp(int klantnummer) {
        // SQL statements voor het ophalen van de meest recente stroom- en gastarieven
        String stroomSql = "SELECT tariefKwh FROM Stroom WHERE klantnummer = ? ORDER BY datumTot DESC LIMIT 1";
        String gasSql = "SELECT tariefGas FROM Gas WHERE klantnummer = ? ORDER BY datumTot DESC LIMIT 1";

        double stroomTarief = 0.0;
        double gasTarief = 0.0;

        try (Connection conn = getConnection();
             PreparedStatement stroomPstmt = conn.prepareStatement(stroomSql);
             PreparedStatement gasPstmt = conn.prepareStatement(gasSql)) {

            // Ophalen en instellen van het meest recente stroomtarief
            stroomPstmt.setInt(1, klantnummer);
            ResultSet stroomRs = stroomPstmt.executeQuery();
            if (stroomRs.next()) {
                stroomTarief = stroomRs.getDouble("tariefKwh");
            }

            // Ophalen en instellen van het meest recente gastarief
            gasPstmt.setInt(1, klantnummer);
            ResultSet gasRs = gasPstmt.executeQuery();
            if (gasRs.next()) {
                gasTarief = gasRs.getDouble("tariefGas");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Kan tarieven niet ophalen: " + e.getMessage());
        }

        Tarieven tarieven = new Tarieven(stroomTarief, gasTarief);

        return tarieven;
    }


    // Haalt een overzicht op van het verbruik voor een klant (week)
    public List<VerbruikOverzicht> haalVerbruikOverzichtOp(int klantnummer) {
        List<VerbruikOverzicht> overzichtsLijst = new ArrayList<>();

        Tarieven tarieven = haalRecenteTarievenOp(klantnummer);

        // SQL statement voor het ophalen van verbruiksoverzicht gegroepeerd op startdatum
        String sql = "SELECT verbruikStartDatum, SUM(stroomVerbruik) AS totaalStroom, SUM(gasVerbruik) AS totaalGas FROM Verbruik WHERE klantnummer = ? GROUP BY verbruikStartDatum ORDER BY verbruikStartDatum";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, klantnummer);
            ResultSet rs = pstmt.executeQuery();

            // Verwerken van de resultaten en aanmaken van VerbruikOverzicht objecten
            while (rs.next()) {
                LocalDate startDatum = rs.getDate("verbruikStartDatum").toLocalDate();
                double totaalStroom = rs.getDouble("totaalStroom");
                double totaalGas = rs.getDouble("totaalGas");

                // Totale kosten berekening
                double kosten = (totaalStroom * tarieven.getStroomTarief()) + (totaalGas * tarieven.getGasTarief());

                VerbruikOverzicht overzicht = new VerbruikOverzicht(klantnummer, startDatum.toString(), totaalStroom, totaalGas, kosten);
                overzichtsLijst.add(overzicht);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Kan verbruiksoverzicht niet ophalen: " + e.getMessage());
        }

        return overzichtsLijst;
    }
    // Vult een TableView met gegevens van het verbruiksoverzicht
    public void vulTableViewMetGegevens(TableView<VerbruikOverzicht> table, int klantnummer) {
        ObservableList<VerbruikOverzicht> overzichtsGegevens = FXCollections.observableArrayList();
        overzichtsGegevens.addAll(haalVerbruikOverzichtOp(klantnummer));

        table.setItems(overzichtsGegevens);
    }
    // Vult een TableView met gegevens van het verbruiksoverzicht (maand)
    public List<VerbruikOverzicht> haalMaandelijksVerbruikOverzichtOp(int klantnummer) {
        List<VerbruikOverzicht> overzichtsLijst = new ArrayList<>();
        String sql = "SELECT YEAR(verbruikStartDatum) AS jaar, MONTH(verbruikStartDatum) AS maand, SUM(stroomVerbruik) AS totaalStroom, SUM(gasVerbruik) AS totaalGas FROM Verbruik WHERE klantnummer = ? GROUP BY jaar, maand ORDER BY jaar DESC, maand DESC";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, klantnummer);
            ResultSet rs = pstmt.executeQuery();

            Tarieven tarieven = haalRecenteTarievenOp(klantnummer);

            while (rs.next()) {
                int jaar = rs.getInt("jaar");
                int maand = rs.getInt("maand");
                double totaalStroom = rs.getDouble("totaalStroom");
                double totaalGas = rs.getDouble("totaalGas");
                double kosten = (totaalStroom * tarieven.getStroomTarief()) + (totaalGas * tarieven.getGasTarief());

                String periode = String.format("%d-%02d", jaar, maand);
                VerbruikOverzicht overzicht = new VerbruikOverzicht(klantnummer, periode, totaalStroom, totaalGas, kosten);
                overzichtsLijst.add(overzicht);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Kan maandelijks verbruiksoverzicht niet ophalen: " + e.getMessage());
        }

        return overzichtsLijst;
    }

    // Vult een TableView met gegevens van het verbruiksoverzicht (jaar)
    public List<VerbruikOverzicht> haalJaarlijksVerbruikOverzichtOp(int klantnummer) {
        List<VerbruikOverzicht> overzichtsLijst = new ArrayList<>();
        String sql = "SELECT YEAR(verbruikStartDatum) AS jaar, SUM(stroomVerbruik) AS totaalStroom, SUM(gasVerbruik) AS totaalGas FROM Verbruik WHERE klantnummer = ? GROUP BY jaar ORDER BY jaar DESC";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, klantnummer);
            ResultSet rs = pstmt.executeQuery();

            Tarieven tarieven = haalRecenteTarievenOp(klantnummer);

            while (rs.next()) {
                int jaar = rs.getInt("jaar");
                double totaalStroom = rs.getDouble("totaalStroom");
                double totaalGas = rs.getDouble("totaalGas");
                double kosten = (totaalStroom * tarieven.getStroomTarief()) + (totaalGas * tarieven.getGasTarief());

                String periode = String.valueOf(jaar);
                VerbruikOverzicht overzicht = new VerbruikOverzicht(klantnummer, periode, totaalStroom, totaalGas, kosten);
                overzichtsLijst.add(overzicht);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Kan jaarlijks verbruiksoverzicht niet ophalen: " + e.getMessage());
        }

        return overzichtsLijst;
    }

    // Updatet verbruiksgegevens in de database voor een klant
    public void updateVerbruikInDatabase(int klantnummer, double stroomVerbruik, double gasVerbruik, LocalDate startDatum, LocalDate eindDatum) {
        String sql = "UPDATE verbruik SET stroomVerbruik = ?, gasVerbruik = ?, verbruikStartDatum = ?, verbruikEindDatum = ? WHERE klantnummer = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, stroomVerbruik);
            pstmt.setDouble(2, gasVerbruik);
            pstmt.setDate(3, Date.valueOf(startDatum));
            pstmt.setDate(4, Date.valueOf(eindDatum));
            pstmt.setInt(5, klantnummer);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Kan verbruiksgegevens niet bijwerken: " + e.getMessage());
        }
    }

    // Verwijdert verbruiksgegevens uit de database voor een klant
    public void verwijderVerbruik(int klantnummer, LocalDate startDatum) {
        String sql = "DELETE FROM verbruik WHERE klantnummer = ? AND verbruikStartDatum = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, klantnummer);
            pstmt.setDate(2, Date.valueOf(startDatum));
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Verwijderen mislukt, geen rijen beïnvloed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Kan verbruik niet verwijderen: " + e.getMessage());
        }
    }
}
