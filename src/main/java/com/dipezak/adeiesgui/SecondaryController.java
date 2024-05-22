package com.dipezak.adeiesgui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

public class SecondaryController implements Initializable {

    private File saveFile;
    private List<Adeia> diffs;

    @FXML
    private Label resultLabel;
    @FXML
    private CheckBox checkBox;
    @FXML
    private TableColumn<Adeia, String> columnAFM;
    @FXML
    private TableColumn<Adeia, String> columnLastname;
    @FXML
    private TableColumn<Adeia, String> columnFirstname;
    @FXML
    private TableColumn<Adeia, String> columnType;
    @FXML
    private TableColumn<Adeia, LocalDate> columnStartDate;
    @FXML
    private TableColumn<Adeia, LocalDate> columnEndDate;
    @FXML
    private TableColumn<Adeia, String> columnFrom;
    @FXML
    private TableView<Adeia> tableView;

    public List<Adeia> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<Adeia> diffs) {
        this.diffs = diffs;
    }

    public void setTextToTextArea(List<Adeia> adeies) {
        setDiffs(adeies);
        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> tableView.refresh());
        columnAFM.setCellValueFactory(
                new PropertyValueFactory<Adeia, String>("afm"));
        columnLastname.setCellValueFactory(
                new PropertyValueFactory<Adeia, String>("lastName"));
        columnFirstname.setCellValueFactory(
                new PropertyValueFactory<Adeia, String>("firstName"));
        columnType.setCellValueFactory(
                new PropertyValueFactory<Adeia, String>("type"));
        columnStartDate.setCellFactory(column -> {
            TableCell<Adeia, LocalDate> cell = new TableCell<Adeia, LocalDate>() {
                private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        this.setText(format.format(item));
                    }
                }
            };
            return cell;
        });
        columnStartDate.setCellValueFactory(
                new PropertyValueFactory<Adeia, LocalDate>("startDate"));
        columnEndDate.setCellFactory(column -> {
            TableCell<Adeia, LocalDate> cell = new TableCell<Adeia, LocalDate>() {
                private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        this.setText(format.format(item));
                    }
                }
            };
            return cell;
        });
        columnEndDate.setCellValueFactory(
                new PropertyValueFactory<>("endDate"));
        columnFrom.setCellValueFactory(
                new PropertyValueFactory<>("from"));
        ObservableList<Adeia> data = FXCollections.observableArrayList(adeies);
        data.addListener(new ListChangeListener<Adeia>() {
  @Override
  public void onChanged(Change<? extends Adeia> c) {
    tableView.refresh();
  }
});
        PseudoClass highlighted = PseudoClass.getPseudoClass("highlighted");
        
        tableView.setRowFactory(tableView2 -> {
            TableRow<Adeia> row = new TableRow<>() {
                @Override
                protected void updateItem(Adeia adeia, boolean empty) {
                    super.updateItem(adeia, empty);
                    if (adeia == null || empty) {
                        setStyle("");
                    } else {
                        boolean shouldDisable = !checkBox.isSelected() && "Απουσία".equals(adeia.getType());
                        if (shouldDisable) {
                            data.remove(adeia);
                        }
                        setDisable(shouldDisable);
                        setVisible(shouldDisable);
                        setStyle(shouldDisable ? "-fx-background-color: lightgray;" : "");
                    }
                }
            };
            row.itemProperty().addListener((obs, oldAdeia, newAdeia) -> {
                if (newAdeia != null) {
                    row.pseudoClassStateChanged(highlighted, newAdeia.getFrom().equals("MySchool"));
                }
            });
            return row;
        });
        tableView.setItems(data);

        // Calculate preferred height of the TableView
        double tableHeight = 30 * Math.min(15, adeies.size()) + 30; // Assuming 15+ rows visible by default, adjust as needed
        App.getStage().setMinHeight(340);
        App.getStage().setHeight(tableHeight + 100);
        App.getStage().setWidth(1000);
    }

    @FXML
    private void saveCSVButtonClicked() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Payroll CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        saveFile = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (saveFile != null) {
            boolean writeSuccess = Adeies.writeToFile(diffs, saveFile.getCanonicalPath());
            if (writeSuccess) {
                resultLabel.setText("Επιτυχία εγγραφής αρχείου .csv");
                resultLabel.setVisible(true);
            } else {
                resultLabel.setText("Αποτυχία εγγραφής αρχείου .csv");
                resultLabel.setVisible(true);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
