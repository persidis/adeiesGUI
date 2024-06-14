package com.dipezak.adeiesgui;

import com.dipezak.adeiesgui.Adeies.PersonKey;
import com.opencsv.exceptions.CsvValidationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class PrimaryController implements Initializable {

    private File payrollFile;
    private File mySchoolFile;

    @FXML
    private Button payrollButton;
    @FXML
    private Button mySchoolButton;
    @FXML
    private Button compareButton;
    @FXML
    private Button numberButton;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView check1;
    @FXML
    private ImageView check2;
    @FXML
    private Tooltip payrollButtonTooltip;
    @FXML
    private Tooltip mySchoolButtonTooltip;

    @FXML
    private void payrollButtonClicked() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Payroll CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        payrollFile = fileChooser.showOpenDialog(payrollButton.getScene().getWindow());
        if (payrollFile != null) {
            check1.setVisible(true);
            numberButton.setDisable(false);
            if (mySchoolFile != null) {
                compareButton.setDisable(false);
            }
        } else {
            check1.setVisible(false);
            compareButton.setDisable(true);
            numberButton.setDisable(true);
        }
    }

    @FXML
    private void mySchoolButtonClicked() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open MySchool CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        mySchoolFile = fileChooser.showOpenDialog(mySchoolButton.getScene().getWindow());
        if (mySchoolFile != null) {
            check2.setVisible(true);
            if (payrollFile != null) {
                compareButton.setDisable(false);
            }
        } else {
            check2.setVisible(false);
            compareButton.setDisable(true);
        }
    }

    @FXML
    private void compareButtonClicked() throws IOException {
        try {
            List<Adeia> diffs = Adeies.createDiffList(mySchoolFile.getCanonicalPath(), payrollFile.getCanonicalPath());
            FXMLLoader loader = new FXMLLoader(App.class.getResource("secondary.fxml"));
            Parent sec = loader.load();
            SecondaryController controller = (SecondaryController) loader.getController();
            controller.setData(diffs); // Call the method we wrote before
            App.getStage().setResizable(true);
            App.setRoot(sec);
            App.getStage().centerOnScreen();
        } catch (FileNotFoundException | UnsupportedEncodingException | CsvValidationException | ParseException | StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            errorLabel.setText("Επιλέχθηκαν λάθος αρχεία. Παρακαλώ ξαναπροσπαθήστε.");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void numberButtonClicked() throws IOException, CsvValidationException, ParseException {
        try {
            List<Adeia> payrollAdeies = Adeies.createPayrollList(payrollFile.getCanonicalPath());
            showFilteredPayrollAdeies(payrollAdeies);
        } catch (FileNotFoundException | UnsupportedEncodingException | CsvValidationException | ParseException | StringIndexOutOfBoundsException ex) {
            errorLabel.setText("Επιλέχθηκαν λάθος αρχεία. Παρακαλώ ξαναπροσπαθήστε.");
            errorLabel.setVisible(true);
        }
    }

    private void showFilteredPayrollAdeies(List<Adeia> payrollAdeies) throws UnsupportedEncodingException {
        List<Adeia> filteredList = new ArrayList<>();
        // Αφαίρεση όλων εκτός των αναρρωτικών
        for (int i = 0; i < payrollAdeies.size(); i++) {
            if (payrollAdeies.get(i).getType().equals("ΑΔΕΙΑ ΑΣΘΕΝΕΙΑΣ")) {
                filteredList.add(payrollAdeies.get(i));
            }
        }
        // Εύρεση των υπερβάλλουσων αναρρωτικών αδειών
        Map<PersonKey, Long> result = Adeies.findLongLeavePersons(filteredList);
        // Create a TextArea to display the results
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        result.forEach((person, days) -> textArea.appendText(person.lastName() + " " + person.firstName() + " " + days + " ημέρες \n"));
        // Create an Alert to show the results
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Αποτελέσματα ελέγχου αναρρωτικών αδειών");
        alert.setHeaderText("Αναπληρωτές εκπαιδευτικοί με αναρρωτικές άδειες  > 15 ημερών");
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        payrollButtonTooltip.setShowDuration(Duration.seconds(10));
        payrollButtonTooltip.setShowDelay(Duration.millis(600));
        mySchoolButtonTooltip.setShowDuration(Duration.seconds(10));
        mySchoolButtonTooltip.setShowDelay(Duration.millis(600));
    }
}
