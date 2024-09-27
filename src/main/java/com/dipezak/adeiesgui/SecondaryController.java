package com.dipezak.adeiesgui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SecondaryController implements Initializable {

    private File saveFile;
    private List<Adeia> diffs;
    private final ObservableList<Adeia> removedAdeies;
    private ObservableList<String> typesWithoutDuplicates;
    private ObservableList<Adeia> data;
    private SortedList<Adeia> sortedData;

    @FXML
    private Label resultLabel;
    @FXML
    private CheckComboBox<String> checkComboBox;
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
        this.typesWithoutDuplicates = FXCollections.observableArrayList();
        this.checkComboBox = new CheckComboBox<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.getStage().setMinHeight(400);
        App.getStage().setWidth(1000);
        setupTableColumns();
        setupRowFactory();
    }

    private void setupTableColumns() {
        columnAFM.setCellValueFactory(new PropertyValueFactory<>("afm"));
        columnLastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        columnFirstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        setupDateColumn(columnStartDate, "startDate");
        setupDateColumn(columnEndDate, "endDate");
        columnFrom.setCellValueFactory(new PropertyValueFactory<>("from"));
    }

    private void setupDateColumn(TableColumn<Adeia, LocalDate> column, String property) {
        column.setCellFactory(col -> new TableCell<>() {
            private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : format.format(item));
            }
        });
        column.setCellValueFactory(new PropertyValueFactory<>(property));
    }

    private void setupRowFactory() {
        tableView.setRowFactory(tv -> {
            TableRow<Adeia> row = new TableRow<>() {
                @Override
                protected void updateItem(Adeia adeia, boolean empty) {
                    super.updateItem(adeia, empty);
                    if (adeia == null || empty) {
                        setStyle("");
                    } else {
                        boolean shouldDisable = !checkComboBox.getCheckModel().isChecked(adeia.getType());
                        if (shouldDisable) {
                            removedAdeies.add(adeia);
                            data.remove(adeia);
                        }
                    }
                }
            };
            row.itemProperty().addListener((obs, oldAdeia, newAdeia) -> {
                if (newAdeia != null) {
                    row.pseudoClassStateChanged(PseudoClass.getPseudoClass("highlighted"), "MySchool".equals(newAdeia.getFrom()));
                }
            });
            delayedRefresh();
            return row;
        });
    }

    public void setData(List<Adeia> adeies) {
        setDiffs(adeies);
        data = FXCollections.observableArrayList(getDiffs());
        sortedData = new SortedList<>(data);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        tableView.getSortOrder().addAll(columnLastname, columnStartDate);
        setTableHeight();
        setupCheckComboBox();
        setupDataListeners();
    }

     private void setupCheckComboBox() {
        setTypesWithoutDuplicates();
        checkComboBox.getItems().add("Επιλογή / απεπιλογή όλων");
        checkComboBox.getItems().addAll(getTypesWithoutDuplicates());
        checkComboBox.setShowCheckedCount(true);
        checkComboBox.getCheckModel().checkAll();
        checkComboBox.setTitle("Επιλεγμένοι τύποι αδειών: ");
        checkComboBox.focusedProperty().addListener((o, ov, nv) -> {
            if (nv) {
                checkComboBox.show();
            } else {
                checkComboBox.hide();
            }
        });
    }
     
    private void setupDataListeners() {
        data.addListener((ListChangeListener<Adeia>) c -> {
            setTableHeight();
            delayedRefresh();
        });

        checkComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) c -> {
            boolean changing = false;
            while (c.next()) {
                if (c.wasAdded()) {
                    String addedType = c.getAddedSubList().get(0);
                    if (!changing && addedType.equals("Επιλογή / απεπιλογή όλων")) {
                        changing = true;
                        for (int i = 1; i < checkComboBox.getCheckModel().getItemCount(); i++) {
                            if (!checkComboBox.getCheckModel().isChecked(i)) {
                                checkComboBox.getCheckModel().check(i);
                            }
                        }
                        changing = false;
                    }
                    Iterator<Adeia> iterator = removedAdeies.iterator();
                    while (iterator.hasNext()) {
                        Adeia adeia = iterator.next();
                        if (addedType.equals(adeia.getType())) {
                            data.add(adeia);
                            iterator.remove();
                        }
                    }
                }
                else if (c.wasRemoved()) {
                    String addedType = c.getRemoved().get(0);
                    if (!changing && addedType.equals("Επιλογή / απεπιλογή όλων")) {
                        changing = true;
                        checkComboBox.getCheckModel().clearChecks();
                        changing = false;
                    }
                }
            }
            tableView.sort();
            delayedRefresh();
        });
    }

    private void setTableHeight() {
        App.getStage().setHeight(Math.max(30 * Math.min(15, data.size()) + 130, App.getStage().getHeight()));
    }

    @FXML
    private void saveCSVButtonClicked() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Payroll CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
        saveFile = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        if (saveFile != null) {
            boolean writeSuccess = Adeies.writeToFile(data, saveFile.getCanonicalPath());
            resultLabel.setText(writeSuccess ? "Επιτυχία εγγραφής αρχείου .csv" : "Αποτυχία εγγραφής αρχείου .csv");
            resultLabel.setVisible(true);
        }
    }

    private void setTypesWithoutDuplicates() {
        List<String> typesWithDuplicates = getDiffs().stream()
                .map(Adeia::getType)
                .collect(Collectors.toList());
        List<String> typesWithoutDupl = typesWithDuplicates.stream()
                .distinct()
                .collect(Collectors.toList());
        typesWithoutDuplicates = FXCollections.observableArrayList(typesWithoutDupl);
    }

    private ObservableList<String> getTypesWithoutDuplicates() {
        return typesWithoutDuplicates;
    }

    private void delayedRefresh() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(tableView::refresh);
            }
        }, 1000);
    }

    public List<Adeia> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<Adeia> diffs) {
        this.diffs = diffs;
    }
}