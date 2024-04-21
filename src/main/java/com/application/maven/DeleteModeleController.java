package com.application.maven;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DeleteModeleController {

    @FXML
    private Button buttonQuit;

    @FXML
    private ListView<String> listViewModele;

    // Map pour stocker l'état des CheckBoxes
    private Map<String, Boolean> checkBoxStates = new HashMap<>();

    @FXML
    private void quitButtonPressed() {
        Stage stage = (Stage) buttonQuit.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        /*
            * Initialiser la ListView avec les fichiers de modèle
            * et ajouter des CheckBoxes pour chaque élément
            * pour permettre à l'utilisateur de sélectionner les fichiers à supprimer
         */
        loadModelFiles();
        listViewModele.setCellFactory(lv -> new ListCell<String>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(item);
                    checkBox.setSelected(checkBoxStates.getOrDefault(item, false));  // Utilisez la Map pour définir l'état

                    setOnMouseClicked(event -> {
                        boolean newState = !checkBox.isSelected();
                        checkBox.setSelected(newState);
                        checkBoxStates.put(item, newState);  // Mise à jour de l'état dans la Map
                    });

                    setGraphic(checkBox);
                }
            }
        });
    }

    private void loadModelFiles() {
        /*
            * Charger les fichiers de modèle à partir du dossier src/main/resources/com/application/maven/models
         */
        File folder = new File("src/main/resources/com/application/maven/models");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            System.out.println("Files in folder: ");
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    listViewModele.getItems().add(file.getName());
                    checkBoxStates.put(file.getName(), false);  // Initialisez tous les états à false
                }
            }
        }
    }

    @FXML
    public void onClickDelete(){
        /*
            * Supprimer les fichiers de modèle sélectionnés
         */
        for (Map.Entry<String, Boolean> entry : checkBoxStates.entrySet()) {
            if (entry.getValue()) {
                File file = new File("src/main/resources/com/application/maven/models/" + entry.getKey());
                if (file.delete()) {
                    System.out.println("File deleted successfully: " + entry.getKey());
                } else {
                    System.out.println("Failed to delete the file: " + entry.getKey());
                }
            }
        }
        listViewModele.getItems().clear();
        checkBoxStates.clear();
        loadModelFiles();
        Stage stage = (Stage) buttonQuit.getScene().getWindow();
        stage.close();
    }
}