package com.application.maven;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import ai.Config;
import ai.ConfigFileLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ConfigModeleController {
		Config F;
		Config M;
		Config D;
	@FXML
	Pane pane = new Pane();


	//initialisation des labels
	@FXML
	Label difficultyLabel = new Label();
	@FXML
	Label hiddenLayerSizeLabel = new Label();
	@FXML
	Label numberOfHiddenLayersLabel = new Label();
	@FXML
	Label learningRateLabel = new Label();
	@FXML
	Label easyLabel = new Label();
	@FXML
	Label mediumLabel = new Label();
	@FXML
	Label hardLabel = new Label();


		@FXML
		Button cancelButton = new Button();
		@FXML
		Button saveButton = new Button();
	// textField suivi de la ligne de difficulte (F,M,D), suivi de l'id du field
		@FXML
		TextField textFieldFHiddenLayerSize = new TextField(); 
		
		@FXML
		TextField textFieldFNumberOfHiddenLayers = new TextField(); // Number of Hidden Layers Facile
		
		@FXML
		TextField textFieldFLearningRate = new TextField(); // LearningRate Facile
		
		@FXML
		TextField textFieldMHiddenLayerSize = new TextField(); 
		
		@FXML
		TextField textFieldMNumberOfHiddenLayers = new TextField(); // Number of Hidden Layers Moyen
		
		@FXML
		TextField textFieldMLearningRate = new TextField();
		
		@FXML
		TextField textFieldDHiddenLayerSize = new TextField(); 
		
		@FXML
		TextField textFieldDNumberOfHiddenLayers = new TextField(); // Number of Hidden Layers Difficile
		
		@FXML
		TextField textFieldDLearningRate = new TextField();
		
		@FXML
		void initialize() throws IOException { // ClickConfiguration, ouverture config.txt
			File configFile = new File("src/main/resources/com/application/maven/config.txt");
			//Je vérifie si le fichier existe, si il n'existe pas je le crée avec des valeurs par défaut
			if (!configFile.exists()) {
				configFile.createNewFile();
				FileWriter fw = new FileWriter(configFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("F:1:128:0.01\n" +
						"M:2:256:0.001\n" +
						"D:3:512:0.001");
				bw.close();
			}
			ConfigFileLoader loaderConfig = new ConfigFileLoader();
		    loaderConfig.loadConfigFile("src/main/resources/com/application/maven/config.txt");

		    System.out.println(loaderConfig.get("F"));
		    System.out.println(loaderConfig.get("M"));
		    System.out.println(loaderConfig.get("D"));
		    F = loaderConfig.get("F");
		    M = loaderConfig.get("M");
		    D = loaderConfig.get("D");
		    

		    if (F != null) {
		        textFieldFHiddenLayerSize.setText(""+F.hiddenLayerSize);
		        textFieldFNumberOfHiddenLayers.setText("" + F.numberOfhiddenLayers);
		        textFieldFLearningRate.setText("" + F.learningRate);
		    }
		    if (M != null) {
		        textFieldMHiddenLayerSize.setText(""+M.hiddenLayerSize);
		        textFieldMNumberOfHiddenLayers.setText("" + M.numberOfhiddenLayers);
		        textFieldMLearningRate.setText("" + M.learningRate);
		    }
		    if (D != null) {
		    	textFieldDHiddenLayerSize.setText(""+D.hiddenLayerSize);
		        textFieldDNumberOfHiddenLayers.setText("" + D.numberOfhiddenLayers);
		        textFieldDLearningRate.setText("" + D.learningRate);
		    }
		    
		    
		    
		}
		
		@FXML
		void CancelbuttonPressed() { //Ferme la fenetre de config.txt
			Stage stage = (Stage) cancelButton.getScene().getWindow();
	        stage.close();
		}
		
		boolean emptyInputF() { //return vrai si un des champs de Facile est vide
			String F1;
			String F2;
			String F3;
			F1=textFieldFHiddenLayerSize.getText();
			F2=textFieldFNumberOfHiddenLayers.getText();
			F3=textFieldFLearningRate.getText();
			return (F1.isEmpty()||F2.isEmpty()||F3.isEmpty());
		}
		boolean emptyInputM() { //return vrai si un des champs de Moyen est vide
			String F1;
			String F2;
			String F3;
			F1=textFieldMHiddenLayerSize.getText();
			F2=textFieldMNumberOfHiddenLayers.getText();
			F3=textFieldMLearningRate.getText();
			return (F1.isEmpty()||F2.isEmpty()||F3.isEmpty());
		}
		boolean emptyInputD() { //return vrai si un des champs de Difficile est vide
			String F1;
			String F2;
			String F3;
			F1=textFieldDHiddenLayerSize.getText();
			F2=textFieldDNumberOfHiddenLayers.getText();
			F3=textFieldDLearningRate.getText();
			return (F1.isEmpty()||F2.isEmpty()||F3.isEmpty());
		}
		
		boolean isNumeric(String str) { // Vérifie qu'une chaîne ne contiennent que des nombres
	        return Pattern.matches("^\\d+(\\.\\d+)?$", str);
	    }
		
		void ErrorRedBorder(TextField textField) {
		    textField.getStyleClass().add("error-text-field");
		}
		
		@FXML
		void SaveButtonPressed() throws IOException { // modifie le fonction config.txt, 
			boolean stay=false;
			String Path="src/main/resources/com/application/maven/config.txt";
			File file = new File(Path);
			String content="";
			if(!emptyInputF()) { // Je vérifie si le champ est vide
				if(!isNumeric(textFieldFHiddenLayerSize.getText())) { // Je vérifie si l'utilisateur n'a pas mis de texte
					ErrorRedBorder(textFieldFHiddenLayerSize); // Si il a mis du texte il ne peut pas save et les bordure des champs deviendront rouge.
					stay=true;
				}
				if(!isNumeric(textFieldFLearningRate.getText()) ) {
					ErrorRedBorder(textFieldFLearningRate);
					stay=true;
				}
				if(!isNumeric(textFieldFNumberOfHiddenLayers.getText())) {
					ErrorRedBorder(textFieldFNumberOfHiddenLayers);
					stay=true;
				}
				if(isNumeric(textFieldFHiddenLayerSize.getText())&&isNumeric(textFieldFLearningRate.getText())&&isNumeric(textFieldFNumberOfHiddenLayers.getText())){
					content+="F:"+textFieldFNumberOfHiddenLayers.getText()+":"+textFieldFHiddenLayerSize.getText()+":"+textFieldFLearningRate.getText()+"\n";
				}
			}
			if(!emptyInputM()) {
				if(!isNumeric(textFieldMHiddenLayerSize.getText())) { // Je vérifie si l'utilisateur n'a pas mis de texte
					ErrorRedBorder(textFieldMHiddenLayerSize); // Si il a mis du texte il ne peut pas save et les bordure des champs deviendront rouge.
					stay=true;
				}
				if(!isNumeric(textFieldMLearningRate.getText()) ) {
					ErrorRedBorder(textFieldMLearningRate);
					stay=true;
				}
				if(!isNumeric(textFieldMNumberOfHiddenLayers.getText())) {
					ErrorRedBorder(textFieldMNumberOfHiddenLayers);
					stay=true;
				}
				if(isNumeric(textFieldFHiddenLayerSize.getText())&&isNumeric(textFieldFLearningRate.getText())&&isNumeric(textFieldFNumberOfHiddenLayers.getText())){
					content+="M:"+textFieldMNumberOfHiddenLayers.getText()+":"+textFieldMHiddenLayerSize.getText()+":"+textFieldMLearningRate.getText()+"\n";
				}

			}
			if(!emptyInputD()) {
				if(!isNumeric(textFieldDHiddenLayerSize.getText())) { // Je vérifie si l'utilisateur n'a pas mis de texte
					ErrorRedBorder(textFieldDHiddenLayerSize); // Si il a mis du texte il ne peut pas save et les bordure des champs deviendront rouge.
					stay=true;
				}
				if(!isNumeric(textFieldDLearningRate.getText()) ) {
					ErrorRedBorder(textFieldDLearningRate);
					stay=true;
				}
				if(!isNumeric(textFieldDNumberOfHiddenLayers.getText())) {
					ErrorRedBorder(textFieldDNumberOfHiddenLayers);
					stay=true;
				}
				if(isNumeric(textFieldFHiddenLayerSize.getText())&&isNumeric(textFieldFLearningRate.getText())&&isNumeric(textFieldFNumberOfHiddenLayers.getText())){
					content+="D:"+textFieldDNumberOfHiddenLayers.getText()+":"+textFieldDHiddenLayerSize.getText()+":"+textFieldDLearningRate.getText()+"\n";
				}

			}
			
			// créer le fichier s'il n'existe pas
			if (!file.exists()) {
			file.createNewFile();

			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			if(!content.isEmpty()) {
				bw.write(content);
			}
			
			bw.close();
			if(!stay) {
				Stage stage = (Stage) saveButton.getScene().getWindow();
		        stage.close();
			}
			
			
		}
}
