package com.dipezak.adeiesgui;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SecondaryController implements Initializable {

    private List<Adeia> diffs;
    private Text textHolder = new Text();
    private double oldHeight = 0;
    @FXML
    private VBox vBox;
    @FXML
    private Label secondaryLabel;
    @FXML
    private TableColumn<Adeia, String> columnAFM;
    @FXML
    private TableColumn<Adeia, String> columnLastname;
    @FXML
    private TableColumn<Adeia, String> columnFirstname;
    @FXML
    private TableColumn<Adeia, String> columnType;
    @FXML
    private TableColumn<Adeia, Date> columnStartDate;
    @FXML
    private TableColumn<Adeia, Date> columnEndDate;
    @FXML
    private TableColumn<Adeia, String> columnFrom;
    @FXML
    private Button saveCSVButton;
    @FXML
    private TableView<Adeia> tableView;

    public List<Adeia> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<Adeia> diffs) {
        this.diffs = diffs;
    }
    

    @FXML
    private void saveCSVButtonClicked() throws IOException {
        App.setRoot("primary");
    }

    public void setTextToTextArea(List<Adeia> adeies) {
        columnAFM.setCellValueFactory(
                new PropertyValueFactory<Adeia, String>("afm"));
        columnLastname.setCellValueFactory(
                new PropertyValueFactory<Adeia, String>("lastName"));
        columnFirstname.setCellValueFactory(
                new PropertyValueFactory<Adeia, String>("firstName"));
        columnType.setCellValueFactory(
                new PropertyValueFactory<Adeia, String>("type"));
        columnStartDate.setCellFactory(column -> {
            TableCell<Adeia, Date> cell = new TableCell<Adeia, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
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
                new PropertyValueFactory<Adeia, Date>("startDate"));
        columnEndDate.setCellFactory(column -> {
            TableCell<Adeia, Date> cell = new TableCell<Adeia, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
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
                new PropertyValueFactory<Adeia, Date>("endDate"));
        columnFrom.setCellValueFactory(
                new PropertyValueFactory<Adeia, String>("from"));
        ObservableList<Adeia> data = FXCollections.observableArrayList(adeies);
        App.getStage().setHeight(800);
        App.getStage().setWidth(1000);
        App.getStage().setMinHeight(tableView.getLayoutBounds().getHeight() +140);
        tableView.setItems(data);
        // textHolder.textProperty().bind(tableView.textProperty());
        //tableView.setPrefHeight(textHolder.getLayoutBounds().getHeight());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //secondaryLabel.setText("TESTINGGGG");
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
