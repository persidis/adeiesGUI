package com.dipezak.adeiesgui;

import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

public class PrimaryController {

    private File payrollFile;
    private File mySchoolFile;

    @FXML
    private Button payrollButton;

    @FXML
    private Button mySchoolButton;

    @FXML
    private Button compareButton;
    
    @FXML
    private Label errorLabel;

    @FXML
    private void payrollButtonClicked() throws IOException {
        // App.setRoot("secondary");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Payroll CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        payrollFile = fileChooser.showOpenDialog(payrollButton.getScene().getWindow());
        if ((payrollFile != null) && (mySchoolFile != null)) {
            compareButton.setDisable(false);
        }
    }

    @FXML
    private void mySchoolButtonClicked() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open MySchool CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        mySchoolFile = fileChooser.showOpenDialog(mySchoolButton.getScene().getWindow());
        if ((payrollFile != null) && (mySchoolFile != null)) {
            compareButton.setDisable(false);
        }
    }

    @FXML
    private void compareButtonClicked() throws IOException {

        try {
            List <Adeia> diffs = Adeies.main(mySchoolFile.getCanonicalPath(), payrollFile.getCanonicalPath());
            FXMLLoader loader = new FXMLLoader(App.class.getResource("secondary.fxml"));
            Parent sec = loader.load();
            SecondaryController controller = (SecondaryController) loader.getController();
            controller.setTextToTextArea(diffs); // Call the method we wrote before
            App.getStage().setResizable(true);
            App.setRoot(sec);
        } catch (FileNotFoundException | UnsupportedEncodingException | CsvValidationException | ParseException | StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            errorLabel.setText("Επιλέχθηκαν λάθος αρχεία. Παρακαλώ ξαναπροσπαθήστε.");
            errorLabel.setVisible(true);
        }
    }
}