package com.example.ecm;

import com.example.ecm.db.DatabaseHelper;
import com.example.ecm.model.Klant;
import com.example.ecm.model.Verbruik;
import com.example.ecm.model.VerbruikOverzicht;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Optional;


public class App extends Application {
    private DatabaseHelper databaseHelper = new DatabaseHelper();
    TableView<VerbruikOverzicht> table = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Energiebedrijf Current - Registratie");

        VBox vbox = new VBox();
        vbox.setSpacing(10);

        TextField klantnummerField = new TextField();
        klantnummerField.setPromptText("Klantnummer");

        TextField voornaamField = new TextField();
        voornaamField.setPromptText("Voornaam");

        TextField achternaamField = new TextField();
        achternaamField.setPromptText("Achternaam");

        TextField voorschotField = new TextField();
        voorschotField.setPromptText("Jaarlijks Voorschot");

        // Registratieknop met event handler
        Button saveButton = new Button("Registreren");
        saveButton.setOnAction(event -> {
            // Probeert klantgegevens op te slaan, toont foutmeldingen indien nodig
            try {
                int klantnummer = Integer.parseInt(klantnummerField.getText());
                String voornaam = voornaamField.getText();
                String achternaam = achternaamField.getText();
                double jaarlijksVoorschot = Double.parseDouble(voorschotField.getText());

                Klant klant = new Klant(klantnummer, voornaam, achternaam, jaarlijksVoorschot);
                databaseHelper.voegKlantToe(klant);

                showAlert(primaryStage, "Succes", "Registratie succesvol. Welkom " + voornaam + "!");
                showTarievenScherm(primaryStage, klantnummer);
            } catch (NumberFormatException e) {
                showAlert(primaryStage, "Fout", "Zorg ervoor dat alle velden correct zijn ingevuld.");
            } catch (Exception e) {
                showAlert(primaryStage, "Fout", "Er is een fout opgetreden bij het opslaan van de gegevens: " + e.getMessage());
            }
        });

        // Voegt UI-componenten toe aan de VBox
        vbox.getChildren().addAll(
                new Label("Voer uw klantgegevens in:"),
                klantnummerField,
                voornaamField,
                achternaamField,
                new Label("Jaarlijks Voorschot:"),
                voorschotField,
                saveButton
        );

        // Maakt een scene en toont het hoofdvenster
        Scene scene = new Scene(vbox, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Toont het scherm voor het invoeren van verbruiksgegevens
    private void showVerbruikInvoerScherm(Stage primaryStage, int klantnummer) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        TextField stroomVerbruikField = new TextField();
        stroomVerbruikField.setPromptText("Stroomverbruik in kWh");

        TextField gasVerbruikField = new TextField();
        gasVerbruikField.setPromptText("Gasverbruik in m³");

        DatePicker verbruikStartDatum = new DatePicker();
        DatePicker verbruikEindDatum = new DatePicker();

        Button saveButton = new Button("Verbruik Opslaan");
        saveButton.setOnAction(event -> {
            try {
                double stroomVerbruik = Double.parseDouble(stroomVerbruikField.getText());
                double gasVerbruik = Double.parseDouble(gasVerbruikField.getText());
                LocalDate startDatum = verbruikStartDatum.getValue();
                LocalDate eindDatum = verbruikEindDatum.getValue();

                Verbruik verbruik = new Verbruik(klantnummer, stroomVerbruik, gasVerbruik, startDatum, eindDatum);
                databaseHelper.voegVerbruikToe(verbruik);

                showAlert(primaryStage, "Succes", "Verbruik succesvol opgeslagen!");
                showVerbruikOverzichtScherm(primaryStage, klantnummer);
            } catch (NumberFormatException e) {
                showAlert(primaryStage, "Fout", "Zorg ervoor dat alle velden correct zijn ingevuld en dat verbruikswaarden numeriek zijn.");
            } catch (Exception e) {
                showAlert(primaryStage, "Fout", "Er is een fout opgetreden bij het opslaan van de verbruiksgegevens: " + e.getMessage());
            }
        });

        vbox.getChildren().addAll(
                new Label("Voer uw stroomverbruik in:"),
                stroomVerbruikField,
                new Label("Voer uw gasverbruik in:"),
                gasVerbruikField,
                new Label("Verbruik startdatum:"),
                verbruikStartDatum,
                new Label("Verbruik einddatum:"),
                verbruikEindDatum,
                saveButton
        );

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
    }

    // Toont het scherm voor het invoeren van tarieven
    private void showTarievenScherm(Stage primaryStage, int klantnummer) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        TextField stroomTariefField = new TextField();
        stroomTariefField.setPromptText("Stroomtarief per kWh");

        DatePicker stroomVanafDatum = new DatePicker();
        DatePicker stroomTotDatum = new DatePicker();

        TextField gasTariefField = new TextField();
        gasTariefField.setPromptText("Gastarief per m³");

        DatePicker gasVanafDatum = new DatePicker();
        DatePicker gasTotDatum = new DatePicker();

        Button saveButton = new Button("Tarieven Opslaan");
        saveButton.setOnAction(event -> {
            try {
                double stroomTarief = Double.parseDouble(stroomTariefField.getText());
                LocalDate stroomVanaf = stroomVanafDatum.getValue();
                LocalDate stroomTot = stroomTotDatum.getValue();

                double gasTarief = Double.parseDouble(gasTariefField.getText());
                LocalDate gasVanaf = gasVanafDatum.getValue();
                LocalDate gasTot = gasTotDatum.getValue();

                databaseHelper.voegTarievenToe(klantnummer, stroomTarief, stroomVanaf, stroomTot, gasTarief, gasVanaf, gasTot);

                showAlert(primaryStage, "Succes", "Tarieven succesvol opgeslagen!");
                showVerbruikInvoerScherm(primaryStage, klantnummer);
            } catch (NumberFormatException e) {
                showAlert(primaryStage, "Fout", "Zorg ervoor dat alle velden correct zijn ingevuld en dat tarieven numeriek zijn.");
            } catch (Exception e) {
                showAlert(primaryStage, "Fout", "Er is een fout opgetreden bij het opslaan van de tarieven: " + e.getMessage());
            }
        });

        vbox.getChildren().addAll(
                new Label("Voer uw stroomtarief in:"),
                stroomTariefField,
                new Label("Geldig vanaf:"), stroomVanafDatum,
                new Label("Geldig tot:"), stroomTotDatum,
                new Label("Voer uw gastarief in:"),
                gasTariefField,
                new Label("Geldig vanaf:"), gasVanafDatum,
                new Label("Geldig tot:"), gasTotDatum,
                saveButton
        );

        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
    }

    // Toont het overzichtsscherm met verbruiksgegevens
    private void showVerbruikOverzichtScherm(Stage primaryStage, int klantnummer) {
        VBox vbox = new VBox();
        vbox.setSpacing(10);

        Klant klant = databaseHelper.haalKlantOp(klantnummer);
        double jaarlijksVoorschot = klant.getJaarlijksVoorschot();
        double maandelijksVoorschot = jaarlijksVoorschot / 12;

        Label maandelijksVoorschotLabel = new Label(String.format("Maandelijks voorschot: €%.2f", maandelijksVoorschot));

        Button toevoegenButton = new Button("Nog een verbruik toevoegen");

        toevoegenButton.setOnAction(event -> {
            showVerbruikInvoerScherm(primaryStage, klantnummer);
        });

        ComboBox<String> overzichtsTypeComboBox = new ComboBox<>();
        overzichtsTypeComboBox.getItems().addAll("Wekelijks", "Maandelijks", "Jaarlijks");
        overzichtsTypeComboBox.setValue("Wekelijks");

        overzichtsTypeComboBox.setOnAction(event -> {
            String geselecteerdType = overzichtsTypeComboBox.getValue();
            updateOverzicht(primaryStage, klantnummer, geselecteerdType);
        });

        table = new TableView<>();
        TableColumn<VerbruikOverzicht, String> periodeColumn = new TableColumn<>("Periode");
        TableColumn<VerbruikOverzicht, Number> stroomColumn = new TableColumn<>("Stroomverbruik (kWh)");
        TableColumn<VerbruikOverzicht, Number> gasColumn = new TableColumn<>("Gasverbruik (m³)");
        TableColumn<VerbruikOverzicht, Number> kostenColumn = new TableColumn<>("Kosten (€)");

        periodeColumn.setCellValueFactory(new PropertyValueFactory<>("periode"));
        stroomColumn.setCellValueFactory(new PropertyValueFactory<>("stroomVerbruik"));
        gasColumn.setCellValueFactory(new PropertyValueFactory<>("gasVerbruik"));
        kostenColumn.setCellValueFactory(new PropertyValueFactory<>("kosten"));

        table.getColumns().addAll(periodeColumn, stroomColumn, gasColumn, kostenColumn);

        databaseHelper.vulTableViewMetGegevens(table, klantnummer);

        table.setRowFactory(tv -> {
            TableRow<VerbruikOverzicht> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            // Rechtermuisknop gebruiken om de menu opties te weergeven voor wijzigen en verwijderen

            MenuItem editItem = new MenuItem("Wijzig");
            editItem.setOnAction(event -> {
                VerbruikOverzicht geselecteerd = row.getItem();
                toonVerbruikWijzigScherm(primaryStage, geselecteerd, klantnummer);
            });

            MenuItem deleteItem = new MenuItem("Verwijder");
            deleteItem.setOnAction(event -> {
                VerbruikOverzicht geselecteerd = row.getItem();
                boolean bevestiging = toonBevestigingsDialoog("Verwijderen bevestigen", "Weet je zeker dat je dit verbruik wilt verwijderen?");
                if (bevestiging) {
                    databaseHelper.verwijderVerbruik(geselecteerd.getKlantnummer(), LocalDate.parse(geselecteerd.getPeriode()));
                    updateTableView(klantnummer);
                }
            });

            contextMenu.getItems().addAll(editItem, deleteItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );

            return row;
        });

        vbox.getChildren().addAll(new Label("Verbruiksoverzicht"), maandelijksVoorschotLabel, overzichtsTypeComboBox, toevoegenButton, table);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
    }

    // Toont een bevestigingsdialoog en retourneert de gebruikerskeuze

    private boolean toonBevestigingsDialoog(String titel, String inhoud) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titel);
        alert.setHeaderText(null);
        alert.setContentText(inhoud);

        Optional<ButtonType> resultaat = alert.showAndWait();
        return resultaat.isPresent() && resultaat.get() == ButtonType.OK;
    }

    // Werkt de TableView bij met nieuwe gegevens
    private void updateTableView(int klantnummer) {
        ObservableList<VerbruikOverzicht> overzichtsGegevens = FXCollections.observableArrayList(databaseHelper.haalVerbruikOverzichtOp(klantnummer));
        table.setItems(overzichtsGegevens);
        }

    // Toont een dialoogvenster voor het wijzigen van verbruiksgegevens
    private void toonVerbruikWijzigScherm(Stage primaryStage, VerbruikOverzicht geselecteerd, int klantnummer) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(primaryStage.getScene().getWindow());
        dialog.setTitle("Wijzig Verbruik");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField stroomVerbruikField = new TextField(String.valueOf(geselecteerd.getStroomVerbruik()));
        TextField gasVerbruikField = new TextField(String.valueOf(geselecteerd.getGasVerbruik()));
        DatePicker verbruikStartDatum = new DatePicker(LocalDate.parse(geselecteerd.getPeriode()));
        DatePicker verbruikEindDatum = new DatePicker();

        grid.add(new Label("Stroomverbruik:"), 0, 0);
        grid.add(stroomVerbruikField, 1, 0);
        grid.add(new Label("Gasverbruik:"), 0, 1);
        grid.add(gasVerbruikField, 1, 1);
        grid.add(new Label("Startdatum:"), 0, 2);
        grid.add(verbruikStartDatum, 1, 2);
        grid.add(new Label("Einddatum:"), 0, 3);
        grid.add(verbruikEindDatum, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            double stroomVerbruik = Double.parseDouble(stroomVerbruikField.getText());
            double gasVerbruik = Double.parseDouble(gasVerbruikField.getText());
            LocalDate startDatum = verbruikStartDatum.getValue();
            LocalDate eindDatum = verbruikEindDatum.getValue();

            databaseHelper.updateVerbruikInDatabase(klantnummer, stroomVerbruik, gasVerbruik, startDatum, eindDatum);

            updateTableView(klantnummer);
        }
    }
    // Update het overzicht op basis van het geselecteerde type (wekelijks, maandelijks, jaarlijks)
    private void updateOverzicht(Stage primaryStage, int klantnummer, String overzichtsType) {
        ObservableList<VerbruikOverzicht> overzichtsGegevens;

        switch (overzichtsType) {
            case "Wekelijks":
                overzichtsGegevens = FXCollections.observableArrayList(databaseHelper.haalVerbruikOverzichtOp(klantnummer));
                break;
            case "Maandelijks":
                overzichtsGegevens = FXCollections.observableArrayList(databaseHelper.haalMaandelijksVerbruikOverzichtOp(klantnummer));
                break;
            case "Jaarlijks":
                overzichtsGegevens = FXCollections.observableArrayList(databaseHelper.haalJaarlijksVerbruikOverzichtOp(klantnummer));
                break;
            default:
                overzichtsGegevens = FXCollections.observableArrayList();
                break;
        }

        table.setItems(overzichtsGegevens);
    }

    // Toont een informatieve melding
    private void showAlert(Stage primaryStage, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.initOwner(primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch();
    }
}