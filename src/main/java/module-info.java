module com.dipezak.adeiesgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;
    requires org.controlsfx.controls;

    opens com.dipezak.adeiesgui to javafx.fxml;
    exports com.dipezak.adeiesgui;
}
