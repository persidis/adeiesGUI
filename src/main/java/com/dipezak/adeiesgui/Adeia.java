package com.dipezak.adeiesgui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Author: user
 */
public class Adeia {
    private final StringProperty afm = new SimpleStringProperty(this, "afm", "");
    private final StringProperty lastName = new SimpleStringProperty(this, "lastName", "");
    private final StringProperty firstName = new SimpleStringProperty(this, "firstName", "");
    private final StringProperty type = new SimpleStringProperty(this, "type", "");
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>(this, "startDate");
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>(this, "endDate");
    private final StringProperty from = new SimpleStringProperty(this, "from", "");

    private String matchType(String type) {
        return switch (type) {
            case "Αιμοδοτική", "Αιμοληψίας (σε εργάσιμη ημέρα)" -> "ΑΔΕΙΑ ΑΙΜΟΔΟΣΙΑΣ/ΑΙΜΟΛΗΨΙΑΣ";
            case "ΑΝΑΡΡΩΤΙΚΗ - με Ιατρική Γνωμάτευση", "ΑΝΑΡΡΩΤΙΚΗ - με Υπεύθυνη Δήλωση", "ΑΝΑΡΡΩΤΙΚΗ - με Γνωμάτευση Νοσοκομείου (ν.3528/2007 άρ.56, παρ.3)" -> "ΑΔΕΙΑ ΑΣΘΕΝΕΙΑΣ";
            case "Ανατροφής παιδιού (με πλήρεις αποδοχές)" -> "ΑΔΕΙΑ ΑΝΑΤΡΟΦΗΣ ΤΕΚΝΟΥ ΤΡΙΜΗΝΗ (Ν. 4599/2019)";
            case "Ασθένειας τέκνου" -> "ΑΔΕΙΑ ΑΣΘΕΝΕΙΑΣ ΤΕΚΝΟΥ";
            case "Γάμου" -> "ΑΔΕΙΑ ΓΑΜΟΥ/ΣΥΜΦΩΝΟΥ ΣΥΜΒΙΩΣΗΣ";
            case "Για επιμορφωτικούς ή επιστημονικούς λόγους" -> "ΑΔΕΙΑ ΓΙΑ ΕΠΙΣΤΗΜΟΝΙΚΟΥΣ ΚΑΙ ΕΠΙΜΟΡΦΩΤΙΚΟΥΣ ΛΟΓΟΥΣ";
            case "Για ετήσιο γυναικολογικό έλεγχο" -> "ΑΔΕΙΑ ΕΤΗΣΙΟΥ ΓΥΝΑΙΚΟΛΟΓΙΚΟΥ ΕΛΕΓΧΟΥ";
            case "Ειδική λόγω Αναπηρίας" -> "ΑΔΕΙΑ ΑΝΑΠΗΡΙΑΣ ΕΙΔΙΚΗ";
            case "Εκλογική" -> "ΑΔΕΙΑ ΕΚΛΟΓΙΚΗ ΕΙΔΙΚΗ";
            case "Εξετάσεων" -> "ΑΔΕΙΑ ΕΞΕΤΑΣΕΩΝ (μαθητές, σπουδαστές ή φοιτητές)";
            case "Θανάτου (συζύγου ή συγγενούς έως και β βαθμού)" -> "ΑΔΕΙΑ ΘΑΝΑΤΟΥ ΣΥΓΓΕΝΟΥΣ";
            case "Κανονική" -> "ΑΔΕΙΑ ΚΑΝΟΝΙΚΗ";
            case "ΜΗΤΡΟΤΗΤΑΣ - Κύησης" -> "ΑΔΕΙΑ ΚΥΗΣΗΣ";
            case "ΜΗΤΡΟΤΗΤΑΣ - Λοχείας" -> "ΑΔΕΙΑ ΛΟΧΕΙΑΣ";
            case "ΜΗΤΡΟΤΗΤΑΣ - Προγεννητικού Ελέγχου" -> "ΑΔΕΙΑ ΠΡΟΓΕΝΝΗΤΙΚΩΝ ΕΞΕΤΑΣΕΩΝ";
            case "Παρακολούθησης σχολικής επίδοσης τέκνου" -> "ΑΔΕΙΑ ΠΑΡΑΚΟΛΟΥΘΗΣΗΣ ΣΧΟΛΙΚΗΣ ΕΠΙΔΟΣΗΣ ΤΕΚΝΩΝ";
            case "Πατρότητας" -> "ΑΔΕΙΑ ΠΑΤΡΟΤΗΤΑΣ";
            default -> type;
        };
    }

    // Getters and setters using StringProperty and ObjectProperty
    public String getAfm() {
        return afm.get();
    }

    public void setAfm(String afm) {
        this.afm.set(afm);
    }

    public StringProperty afmProperty() {
        return afm;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(matchType(type));
    }

    public StringProperty typeProperty() {
        return type;
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public ObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    public ObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public String getFrom() {
        return from.get();
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public StringProperty fromProperty() {
        return from;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "afm=" + getAfm() + "\t lastName=" + getLastName() + "\t firstName=" + getFirstName() +
                "\t type=" + getType() + "\t startDate=" + (getStartDate() != null ? dateFormat.format(getStartDate()) : "null") +
                "\t endDate=" + (getEndDate() != null ? dateFormat.format(getEndDate()) : "null") + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Adeia that = (Adeia) o;
        return Objects.equals(getAfm(), that.getAfm()) &&
               Objects.equals(getLastName(), that.getLastName()) &&
               Objects.equals(getFirstName(), that.getFirstName()) &&
               Objects.equals(getType(), that.getType()) &&
               Objects.equals(getStartDate(), that.getStartDate()) &&
               Objects.equals(getEndDate(), that.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAfm(), getLastName(), getFirstName(), getType(), getStartDate(), getEndDate());
    }

    public String toCSVString() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "\'" + getAfm() + "\';" + getLastName() + ";" + getFirstName() + ";" + getType() + ";" +
                (getStartDate() != null ? dateFormat.format(getStartDate()) : "") + ";" +
                (getEndDate() != null ? dateFormat.format(getEndDate()) : "") + ";" +
                getFrom() + '\n';
    }

    public Adeia() {
        this("", "", "", "", null, null, "");
    }

    public Adeia(Adeia a) {
        this(a.getAfm(), a.getLastName(), a.getFirstName(), a.getType(), a.getStartDate(), a.getEndDate(), a.getFrom());
    }

    public Adeia(String afm, String lastName, String firstName, String type, LocalDate startDate, LocalDate endDate, String from) {
        setAfm(afm);
        setLastName(lastName);
        setFirstName(firstName);
        setType(type);
        setStartDate(startDate);
        setEndDate(endDate);
        setFrom(from);
    }
}
