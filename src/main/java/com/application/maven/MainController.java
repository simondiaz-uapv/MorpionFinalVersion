package com.application.maven;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.awt.Desktop;
import java.util.stream.Collectors;


import ai.Config;
import ai.ConfigFileLoader;

import static ai.MultiLayerPerceptron.load;

public class MainController { // Controller du menu principal


	//Initialisation des BorderPains
	@FXML
	private BorderPane borderPaneMain = new BorderPane();
	@FXML
	private BorderPane borderPaneTop = new BorderPane();

	//Initialisation des éléments de la menubar
	@FXML
	private MenuBar menuBar = new MenuBar();
	@FXML
	private Menu menuAIModel = new Menu("AI Model");
	@FXML
	private MenuItem menuItemConfiguration = new MenuItem("Configuration");
	@FXML
	private MenuItem menuItemVisualisation = new MenuItem("Visualisation");
	@FXML
	private Menu menuDelete = new Menu("Delete");
	@FXML
	private Menu menuHelp = new Menu("Help");
	@FXML
	private MenuItem menuItemGameRules = new MenuItem("Game Rules");

	//Initialisation des éléments du menu principal
	@FXML
	private AnchorPane anchorPaneMid = new AnchorPane();
	@FXML
	private Button buttonPVP = new Button("PvP (PlayerVsPlayer)");
	@FXML
	private Button buttonPVE = new Button("PvE (PlayerVsAI)");
	@FXML
	private Button buttonEasy = new Button("Easy");
	@FXML
	private Button buttonMedium = new Button("Medium");
	@FXML
	private Button buttonTitle = new Button("Title");
	@FXML
	private Button buttonHard = new Button("Hard");
	@FXML
	private Button buttonSettings = new Button("Settings");
	@FXML
	private Button buttonQuit = new Button("Quit");

	boolean visible = false;


	@FXML
	void initialize() {
		borderPaneMain = new BorderPane();


	}

	@FXML
	void onClickHelp() {
		String filePath = ("src/main/resources/com/application/maven/pdf/fileHelp.pdf");

		File file = new File(filePath);

		if (file.exists()) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("This file doesn't exist.");
		}
	}

	@FXML
	void configModele() throws IOException { // ClickConfiguration, ouverture config.txt
		
	    FXMLLoader configLoader = new FXMLLoader(MainController.class.getResource("ConfigModele.fxml"));
	    Parent root = configLoader.load();

	    Scene sceneConfig = new Scene(root, 854, 480);
	    Stage secondStage = new Stage();
	    secondStage.setTitle("Configuration Page");
	    secondStage.setScene(sceneConfig);
	    secondStage.show();
	}
	
	
	void showProgression() throws IOException {
		FXMLLoader progressLoader = new FXMLLoader(MainController.class.getResource("ProgressIA.fxml"));
		Scene sceneConfig = new Scene(progressLoader.load(), 854, 480);
		Stage secondStage = new Stage();
        secondStage.setScene(sceneConfig);
        secondStage.show();
	}
	
	@FXML
	void launchGame(ActionEvent event)throws IOException{
		FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("Game.fxml"));
        Parent newContent = gameLoader.load();
        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(newContent);
	}


	@FXML
	void callProgressBar() throws IOException {
		FXMLLoader ProgressBarLoader = new FXMLLoader(getClass().getResource("ProgressBar.fxml"));
		Scene sceneProgressBar = new Scene(ProgressBarLoader.load(), 500, 350);

		Stage progressBarStage = new Stage();
		progressBarStage.setMinWidth(500);
		progressBarStage.setMinHeight(350);
		progressBarStage.setMaxWidth(500);
		progressBarStage.setMaxHeight(350);
		progressBarStage.setScene(sceneProgressBar);
		progressBarStage.show();
	}

	@FXML
	void showSettings(ActionEvent event) throws IOException{ //Ecran de de configuration avec transition de fade
		FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));
		Parent settingsContent = settingsLoader.load();
		Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		FadeTransition ft = new FadeTransition(Duration.millis(1000), settingsContent);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
		currentStage.getScene().setRoot(settingsContent);
	}

	@FXML
	public void showDelete() throws IOException {
		FXMLLoader deleteLoader = new FXMLLoader(getClass().getResource("DeleteModel.fxml"));
		Scene sceneDelete = new Scene(deleteLoader.load(), 800, 450);
		Stage deleteStage = new Stage();
		deleteStage.setMinWidth(800);
		deleteStage.setMinHeight(450);
		deleteStage.setMaxWidth(800);
		deleteStage.setMaxHeight(450);
		deleteStage.setScene(sceneDelete);
		deleteStage.show();
	}

	@FXML
	void quitButtonPressed() { //Ferme la fenetre de config.txt
		Stage stage = (Stage) buttonQuit.getScene().getWindow();
		stage.close();
	}



	@FXML
	void onClickPVE(ActionEvent event){
		if (visible == false){
			buttonEasy.setVisible(true);
			buttonMedium.setVisible(true);
			buttonHard.setVisible(true);
			visible = true;
		}
		else{
			buttonEasy.setVisible(false);
			buttonMedium.setVisible(false);
			buttonHard.setVisible(false);
			visible = false;
		}

	}
	@FXML
	public void onClickEasy(ActionEvent event) throws IOException {
		Boolean existingModel = false;
		List<String> list = Files.lines(Paths.get("src/main/resources/com/application/maven/config.txt")).collect(Collectors.toList());

		String[] tab = list.get(0).split(":");

		Path directoryModels = Paths.get("src/main/resources/com/application/maven/models");
		File directory = new File(directoryModels.toString());
		File[] files = directory.listFiles();
		if(files != null){
			for(File f : files){
				System.out.println(f.getName());

				String modelName = "model_Easy_" + tab[1] + "_" + tab[2] + "_" + tab[3] + ".srl";

				if (f.getName().equals(modelName)) {
					existingModel=true;
					FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("GamePVIA.fxml"));
					Parent newContent = gameLoader.load();
					// Obtenir le contrôleur pour le nouveau contenu chargé
					GameControllerPVIA controller = gameLoader.getController();
					// Passer le nom du modèle au contrôleur
					controller.setModelName(modelName);

					Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					currentStage.getScene().setRoot(newContent);
				}
			}
		}
		if(!existingModel){
			callProgressBarWithModel(1);
		}
	}
	@FXML
	public void onClickMedium(ActionEvent event) throws IOException {
		Boolean existingModel=false;
		List<String> list = Files.lines(Paths.get("src/main/resources/com/application/maven/config.txt")).collect(Collectors.toList());

		String[] tab = list.get(1).split(":");

		Path directoryModels = Paths.get("src/main/resources/com/application/maven/models");
		File directory = new File(directoryModels.toString());
		File[] files = directory.listFiles();
		if(files != null){
			for(File f : files){
				System.out.println(f.getName());
				String modelName = "model_Medium_" + tab[1] + "_" + tab[2] + "_" + tab[3] + ".srl";
				System.out.println("modelName ::"+modelName);
				if (f.getName().equals(modelName)) {
					existingModel=true;
					FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("GamePVIA.fxml"));
					Parent newContent = gameLoader.load();
					GameControllerPVIA controller = gameLoader.getController();
					controller.setModelName(modelName);

					Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					currentStage.getScene().setRoot(newContent);
				}
			}
		}
		if(!existingModel){
			callProgressBarWithModel(2);
		}

	}
	@FXML
	public void onClickHard(ActionEvent event) throws IOException {
		Boolean existingModel=false;
		List<String> list = Files.lines(Paths.get("src/main/resources/com/application/maven/config.txt")).collect(Collectors.toList());

		String[] tab = list.get(2).split(":");

		Path directoryModels = Paths.get("src/main/resources/com/application/maven/models");
		File directory = new File(directoryModels.toString());
		File[] files = directory.listFiles();
		if(files != null){

			for(File f : files){
				System.out.println("fName ::"+f.getName());
				System.out.println("model_Hard_" + tab[1] + "_" + tab[2] + "_" + tab[3] + ".srl");
				String modelName = "model_Hard_" + tab[1] + "_" + tab[2] + "_" + tab[3] + ".srl";
				if (f.getName().equals(modelName)) {
					existingModel= !existingModel;
					FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("GamePVIA.fxml"));
					Parent newContent = gameLoader.load();
					GameControllerPVIA controller = gameLoader.getController();
					controller.setModelName(modelName);

					Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
					currentStage.getScene().setRoot(newContent);
				}
			}
		}
		if(!existingModel){
			callProgressBarWithModel(3);
		}
	}
	void callProgressBarWithModel(int difficulty) {
		try {
			FXMLLoader progressBarLoader = new FXMLLoader(getClass().getResource("ProgressBar.fxml"));
			// Charger le contenu FXML et récupérer le contrôleur
			Pane progressBar = progressBarLoader.load(); // Chargez ici pour initialiser les @FXML
			viewProgressBar controller = progressBarLoader.getController();
			controller.initModel(difficulty); // Initialisez le modèle et mettez à jour la vue

			Scene sceneProgressBar = new Scene(progressBar, 500, 350);
			Stage progressBarStage = new Stage();
			progressBarStage.setScene(sceneProgressBar);
			progressBarStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
