package com.application.maven;

import java.io.File;
import java.util.HashMap;

import ai.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class viewProgressBar {

	private int model;

	@FXML
	private ProgressBar progressBar;


	@FXML
	private Button progressButton;

	@FXML
	private Button easyButton;
	@FXML
	private Button mediumButton;
	@FXML
	private Button hardButton;
	
	@FXML
	private Label progressLabel;
	
	@FXML
	private MenuBar progressMenu;

	@FXML
	void initialize(){

	}
	public int getModel() {
		return model;
	}
	public void initModel(int difficulty) {
		this.model = difficulty;
		updateView();
	}

	// Mettez à jour la vue basée sur le modèle
	private void updateView() {
		progressLabel.setText("This model does not exist yet click on learn to create it.");
		if(model==1){
			easyButton.setVisible(false);
			mediumButton.setText("LEARN");
			mediumButton.setVisible(true);
			hardButton.setVisible(false);
			mediumButton.setOnAction(event -> onClickLearnF());
		}
		else if(model==2){
			easyButton.setVisible(false);
			mediumButton.setText("LEARN");
			mediumButton.setVisible(true);
			hardButton.setVisible(false);
			mediumButton.setOnAction(event -> onClickLearnM());
		}
		else if(model==3){
			easyButton.setVisible(false);
			mediumButton.setText("LEARN");
			mediumButton.setVisible(true);
			hardButton.setVisible(false);
			mediumButton.setOnAction(event -> onClickLearnD());
		}
		else{
			easyButton.setVisible(true);
			mediumButton.setVisible(true);
			hardButton.setVisible(true);
			easyButton.setOnAction(event -> onClickLearnF());
			mediumButton.setOnAction(event -> onClickLearnM());
			hardButton.setOnAction(event -> onClickLearnD());
		}
	}
	@FXML
	void onClickLearnF(){
		ConfigFileLoader loaderConfig = new ConfigFileLoader();
		loaderConfig.loadConfigFile("src/main/resources/com/application/maven/config.txt");
		Config F = loaderConfig.get("F");
		if(F==null){
			learn(128, 1 ,0.01,"Easy"); //Base config
		}
		else{
			learn(F.numberOfhiddenLayers, F.hiddenLayerSize, F.learningRate,"Easy");
		}
	}

	@FXML
	void onClickLearnM(){
		ConfigFileLoader loaderConfig = new ConfigFileLoader();
		loaderConfig.loadConfigFile("src/main/resources/com/application/maven/config.txt");
		Config M = loaderConfig.get("M");
		if(M==null){
			learn(256, 2 ,0.001,"Medium"); //Base config
		}
		else{
			learn(M.numberOfhiddenLayers, M.hiddenLayerSize, M.learningRate,"Medium");
		}
	}
	@FXML
	void onClickLearnD(){
		ConfigFileLoader loaderConfig = new ConfigFileLoader();
		loaderConfig.loadConfigFile("src/main/resources/com/application/maven/config.txt");
		Config D = loaderConfig.get("D");
		if(D==null){
			learn(512, 3 ,0.001,"Hard"); //Base config
		}
		else {
			learn(D.numberOfhiddenLayers, D.hiddenLayerSize, D.learningRate, "Hard");
		}
	}

    void learn(int l, int h, double lr,String difficulty) {
    	
    	int size = 9;

    	HashMap<Integer, Coup> mapTrain = Test.loadCoupsFromFile("src/main/resources/com/application/maven/train_dev_test/train.txt");
    	
    	boolean verbose = true;
    	
    	double epochs = 10000;
    	
    	//part 1
    	if ( verbose ) {
			System.out.println();
			System.out.println("START TRAINING ...");
			System.out.println();
		}
		//
		//			int[] layers = new int[]{ size, 128, 128, size };
		int[] layers = new int[l+2];
		layers[0] = size ;
		for (int i = 0; i < l; i++) {
			layers[i+1] = h ;
		}
		layers[layers.length-1] = size ;
		//
		
		MultiLayerPerceptron net = new MultiLayerPerceptron(layers, lr, new SigmoidalTransferFunction());
    	
		//part 2
    	Task<Double> task = new Task<Double>() {
			
			@Override
			protected Double call() throws Exception {
				
				System.out.println(epochs);
				
				double error = 0.0 ;
				double bestError = 1000.0;
				for(int i = 0; i < epochs; i++){

					Coup c = null ;
					while ( c == null )
						c = mapTrain.get((int)(Math.round(Math.random() * mapTrain.size())));
					
					System.out.println("while");
					
					error += net.backPropagate(c.in, c.out);

					System.out.println(error);
					if ( i!= 0  && bestError < error) {
						bestError = error;
						updateMessage("Error at step "+i+" is "+ (error/(double)i));

					}
					else {
						System.out.println("Error at step "+i+" is "+ (error/(double)i));

					}
						
					
					System.out.println(i + "/"+epochs);
					updateProgress(i, epochs);
				}
				net.save("src/main/resources/com/application/maven/models/model_"+difficulty+"_"+l+"_"+h+"_"+lr+".srl");

				return error ;
			}
		};
		progressBar.progressProperty().bind(task.progressProperty());
		task.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				progressLabel.setText(newValue);
			}
		});

		new Thread(task).start();
		//J'attend le fin du thread pour fermer la fenetre
		task.setOnSucceeded(event -> {
			progressLabel.setText("Training done");
			Stage stage = (Stage) mediumButton.getScene().getWindow();
			stage.close();
		});
    }
}
