package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.util.Optional;

public class EditorSceneController {
    public HTMLEditor htmlEditor;
    public MenuItem mnSave;
    public static MenuItem mnNew;
    public MenuItem mnSaveOver;
    private boolean isTextChanged = false;
    private File fileRead;
    private Stage stage;
    private String title = "Untitled Document";

    @FXML
    void mnAboutOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(new FXMLLoader(this.getClass().getResource("/view/AboutScene.fxml")).load()));
        stage.setTitle("About");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(htmlEditor.getScene().getWindow());
        stage.show();
    }

    @FXML
    void mnCloseOnAction(ActionEvent event) {
        if (isTextChanged) {
            ButtonType buttonTypeSave = new ButtonType("Save");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");
            ButtonType buttonTypeClose = new ButtonType("Close without Saving");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Save Changes to the document before closing",buttonTypeClose,buttonTypeCancel,buttonTypeSave);
            Optional<ButtonType> button = alert.showAndWait();
            if (button.isPresent() && button.get()==buttonTypeSave) {
                mnSave.fire();
            }
            if (button.isPresent() && button.get()==buttonTypeClose) {
                isTextChanged = false;
            }
        }
        if (!isTextChanged) {
            System.exit(0);
        }
    }

    @FXML
    void mnNewOnAction(ActionEvent event) {
        if (isTextChanged) {
            ButtonType buttonTypeSave = new ButtonType("Save");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");
            ButtonType buttonTypeClose = new ButtonType("Close without Saving");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Save Changes to the document before closing",buttonTypeClose,buttonTypeCancel,buttonTypeSave);
            Optional<ButtonType> button = alert.showAndWait();
            if (button.isPresent() && button.get()==buttonTypeSave) {
                stage.setTitle("Untitled Document");
                mnSave.fire();
            }
            if (button.isPresent() && button.get()==buttonTypeClose) {
                stage.setTitle("Untitled Document");
                htmlEditor.setHtmlText("");
            }
        }else {
            htmlEditor.setHtmlText("");
        }
    }

    @FXML
    void mnOpenOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a text file");
        fileRead = fileChooser.showOpenDialog(htmlEditor.getScene().getWindow());
        if (fileRead==null) return;
        FileInputStream fileInputStream = new FileInputStream(fileRead);
        byte[] bytes = fileInputStream.readAllBytes();
        fileInputStream.close();
        htmlEditor.setHtmlText(new String(bytes));
        title = fileRead.getName();
        stage = (Stage) htmlEditor.getScene().getWindow();
        stage.setTitle(title);
    }
    @FXML
    void mnSaveOnAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save a text file");
        File fileWrite = fileChooser.showSaveDialog(htmlEditor.getScene().getWindow());
        if (fileWrite == null) {
            return;
        } else {
            fileRead=fileWrite;
            FileOutputStream fileOutputStream = new FileOutputStream(fileWrite);
            String text = htmlEditor.getHtmlText();
            byte[] bytes = text.getBytes();
            fileOutputStream.write(bytes);
            title=fileRead.getName();
            stage.setTitle(title);
            isTextChanged = false;
            fileOutputStream.close();
        }
    }

    public void mnSaveOverOnAction(ActionEvent actionEvent) throws IOException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileRead);
            String text = htmlEditor.getHtmlText();
            byte[] bytes = text.getBytes();
            fileOutputStream.write(bytes);
            isTextChanged = false;
            fileOutputStream.close();
            stage.setTitle(title);
        } catch (NullPointerException e) {
            mnSave.fire();
        }
    }
}
