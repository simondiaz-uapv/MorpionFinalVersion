package com.application.maven;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsController {

    private File settingsFile;
    private int lastMusic;
    private int lastVolume;


    @FXML
    private ComboBox<String> resolutionComboBox;
    @FXML
    private ComboBox<String> musicalThemeComboBox;
    @FXML
    private Slider volumeSlider;
    @FXML
            private Button saveButton;

    int volume;
    int resolutionX;
    int resolutionY;
    int musicalTheme;

    @FXML
    void initialize() throws IOException{
        //J'initialise un listener de volumeSlider
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                onSlideChangeVolume();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        settingsFile = new File("src/main/resources/com/application/maven/settings.txt");
        // SI le fichier n'existe pas, on le crée
        if (!settingsFile.exists()) {
            try {
                System.out.println("Creating settings file");
                settingsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        volume = 25; // Valeurs par défaut
        resolutionX = 1280;
        resolutionY = 720;
        musicalTheme = 1;

        // Lecture du fichier et extraction des valeurs
        try (BufferedReader br = new BufferedReader(new FileReader(settingsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "volume":
                            volume = Integer.parseInt(value);
                            lastVolume = volume;
                            break;
                        case "resolution":
                            String[] resolutionParts = value.split("x");
                            if (resolutionParts.length == 2) {
                                resolutionX = Integer.parseInt(resolutionParts[0]);
                                resolutionY = Integer.parseInt(resolutionParts[1]);
                            }
                            break;
                        case "musicalTheme":

                            musicalTheme = Integer.parseInt(value);
                            lastMusic = musicalTheme;
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        volumeSlider.adjustValue(volume);
        resolutionComboBox.setValue(resolutionX + "x" + resolutionY);
        if(musicalTheme == 1) {
            musicalThemeComboBox.setValue("Electronic");
        } else {
            musicalThemeComboBox.setValue("Retro");
        }
        if(resolutionX==1280 && resolutionY==720){
            resolutionComboBox.getItems().addAll("1280x720", "1600x900");
        }
        else{
            resolutionComboBox.getItems().addAll("1600x900", "1280x720");
        }
        if(musicalTheme == 1){
            musicalThemeComboBox.getItems().add( "Retro");
        }
        else{
            musicalThemeComboBox.getItems().add("Electronic");
        }

    }
    @FXML
    void returnToMainMenu(ActionEvent event) throws IOException {
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("MainFXML.fxml"));
        Parent mainMenuContent = mainMenuLoader.load();
        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FadeTransition ft = new FadeTransition(Duration.millis(1000), mainMenuContent);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        currentStage.getScene().setRoot(mainMenuContent);
    }

    @FXML
    void saveSettings(ActionEvent event) throws IOException {

        String resolution = resolutionComboBox.getValue();
        String[] resolutionParts = resolution.split("x");
        resolutionX = Integer.parseInt(resolutionParts[0]);
        resolutionY = Integer.parseInt(resolutionParts[1]);
        volume = (int) volumeSlider.getValue();
        String musicalThemeString = musicalThemeComboBox.getValue();
        if(musicalThemeString.equals("Electronic")) {
            musicalTheme = 1;
        } else {
            musicalTheme = 2;
        }
        if(volume < 0) {
            volume = 0;
        } else if(volume > 100) {
            volume = 100;
        }
        if(resolutionX < 1280){
            resolutionX = 1280;
        }
        else if(resolutionX > 1600){
            resolutionX = 1600;
        }
        if(resolutionY < 720){
            resolutionY = 720;
        }
        else if(resolutionY > 900){
            resolutionY = 900;
        }
        String settings = "volume:" + volume + "\nresolution:" + resolutionX + "x" + resolutionY + "\nmusicalTheme:" + musicalTheme;
        FileWriter fw = new FileWriter(settingsFile.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        if(!settings.isEmpty()) {
            bw.write(settings);
        }
        bw.close();


        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("MainFXML.fxml"));
        Parent mainMenuContent = mainMenuLoader.load();
        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FadeTransition ft = new FadeTransition(Duration.millis(1000), mainMenuContent);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        if(musicalTheme != lastMusic){
            System.out.println("Changing music");
            MusicPlayer.changeMusic();
        }
        if(lastVolume!=volume){
            MusicPlayer.changeVolume(volume);
        }
        if(resolutionX!=Main.getResolutionX()){
            Main.setResolutionX(resolutionX);
            Main.setResolutionY(resolutionY);
            currentStage.setMinWidth(resolutionX);
            currentStage.setMinHeight(resolutionY);
            currentStage.setMaxWidth(resolutionX);
            currentStage.setMaxHeight(resolutionY);
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds(); // Centrer la fenêtre
            currentStage.setX((screenBounds.getWidth() - resolutionX) / 2);
            currentStage.setY((screenBounds.getHeight() - resolutionY) / 2);
        }
        currentStage.getScene().setRoot(mainMenuContent);

    }

    @FXML
    void onSlideChangeVolume() throws IOException {
        MusicPlayer.changeVolume((int) volumeSlider.getValue());
    }

}
