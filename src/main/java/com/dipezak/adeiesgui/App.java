package com.dipezak.adeiesgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.beans.value.ChangeListener;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 380, 240);
        stage.setResizable(false);
        stage.setTitle("Σύγκριση αδειών Payroll και Myschool");
        stage.setScene(scene);
        mainStage = stage;
        stage.show();
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue)
                -> System.out.println("Height: " + stage.getHeight() + " Width: " + stage.getWidth());

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static void setRoot(Parent fxmlLoader) throws IOException {
        scene.setRoot(fxmlLoader);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Stage getStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }

}
