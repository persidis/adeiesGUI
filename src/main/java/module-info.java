module com.dipezak.adeiesgui {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.dipezak.adeiesgui to javafx.fxml;
    exports com.dipezak.adeiesgui;
    requires com.opencsv;
}
