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
import javafx.collections.transformation.SortedList;
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
    private ObservableList<Adeia> removedAdeies;
    private ObservableList<Adeia> data;
    private SortedList<Adeia> sortedData;

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

    public SecondaryController() {
        this.removedAdeies = FXCollections.observableArrayList();
        this.data = FXCollections.observableArrayList();
    }

    public void setData(List<Adeia> adeies) {
        data = FXCollections.observableArrayList(adeies);
        data.addListener(new ListChangeListener<Adeia>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Adeia> c) {
                setTableHeight();
                tableView.refresh();
            }
        });
        sortedData = new SortedList<>(data);
        // this ensures the sortedData is sorted according to the sort columns in the table:
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        // programmatically set a sort column:
        tableView.getSortOrder().addAll(columnLastname, columnStartDate);
        setTableHeight();
    }
    
    private void setTableHeight() {
        // Assuming 15+ rows visible by default, adjust as needed
        App.getStage().setHeight(30 * Math.min(15, data.size()) + 130);
    }

    @FXML
    private void saveCSVButtonClicked() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Payroll CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        saveFile = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (saveFile != null) {
            boolean writeSuccess = Adeies.writeToFile(data, saveFile.getCanonicalPath());
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
        // Set initial tableHeight
        App.getStage().setMinHeight(340);
        App.getStage().setWidth(1000);
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

        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected && removedAdeies != null && !removedAdeies.isEmpty()) {
                data.addAll(removedAdeies);
                removedAdeies.clear();
            }
            tableView.sort();
            tableView.refresh();
        });

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
                            removedAdeies.add(adeia);
                            data.remove(adeia);
                        }
                    }
                }
            };
            row.itemProperty().addListener((obs, oldAdeia, newAdeia) -> {
                if (newAdeia != null) {
                    row.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighted"), newAdeia.getFrom().equals("MySchool"));
                }
            });
            tableView.refresh();
            return row;
        });
    }
}
