package com.application.maven;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javax.sound.sampled.*;

public class Main extends Application {

	private static Thread musicThread;
	private static boolean musicPlaying ;
	private static int resolutionX;
	private static int resolutionY;

	@Override
	public void start(Stage primaryStage)throws IOException {

		setResolutions();
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainFXML.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), resolutionX, resolutionY);
        primaryStage.setTitle("Tic Tac Toe");
		primaryStage.setMinWidth(resolutionX);
		primaryStage.setMinHeight(resolutionY);
		primaryStage.setMaxWidth(resolutionX);
		primaryStage.setMaxHeight(resolutionY);
        primaryStage.setScene(scene);
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX((screenBounds.getWidth() - resolutionX) / 2);
		primaryStage.setY((screenBounds.getHeight() - resolutionY) / 2);
        primaryStage.show();

		MusicPlayer.playMusic();
	}

	public static void setResolutions(){
		File settingsFile = new File("src/main/resources/com/application/maven/settings.txt");
		// SI le fichier n'existe pas, on le crée
		if (!settingsFile.exists()) {
			try {
				System.out.println("Creating settings file");
				settingsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		resolutionX = 1280;
		resolutionY = 720;

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

							break;
						case "resolution":
							String[] resolutionParts = value.split("x");
							if (resolutionParts.length == 2) {
								resolutionX = Integer.parseInt(resolutionParts[0]);
								resolutionY = Integer.parseInt(resolutionParts[1]);
							}
							break;
						case "musicalTheme":
							break;
						default:
							break;
					}
				}
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}

	}

	public static int getResolutionX() {
		return resolutionX;
	}

	public static int getResolutionY() {
		return resolutionY;
	}

	public static void setResolutionX(int resolutionX) {
		Main.resolutionX = resolutionX;
	}

	public static void setResolutionY(int resolutionY) {
		Main.resolutionY = resolutionY;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
