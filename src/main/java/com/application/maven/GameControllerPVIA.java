package com.application.maven;
import ai.MultiLayerPerceptron;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

public class GameControllerPVIA {

    private String modelName;
    @FXML
    Label victoryLabel = new Label(); //Label pour afficher le gagnant

    @FXML
    Label turnLabel; // Label pour afficher le Turn of player

    @FXML
    Button restartButton; // Bouton pour recommencer la partie
    @FXML
    Button returnToMain;
    boolean turn = false; // False = le joueur 1(croix) Joue, True = le joueur 2(Rond) Joue
    boolean ingame = true; // True = le jeu est en cours, False = le jeu est fini

    @FXML
    GridPane gameGrid = new GridPane(); // Grille de jeu
    @FXML
    ImageView case0_0 = new ImageView();
    @FXML
    ImageView case0_1 = new ImageView();
    @FXML
    ImageView case0_2 = new ImageView();
    @FXML
    ImageView case1_0 = new ImageView();
    @FXML
    ImageView case1_1 = new ImageView();
    @FXML
    ImageView case1_2 = new ImageView();
    @FXML
    ImageView case2_0 = new ImageView();
    @FXML
    ImageView case2_1 = new ImageView();
    @FXML
    ImageView case2_2 = new ImageView();

    @FXML
    Line line1 = new Line();
    @FXML
    Line line2 = new Line();
    @FXML
    Line line3 = new Line();
    @FXML
    Line line4 = new Line();
    @FXML
    Line line5 = new Line();
    @FXML
    Line line6 = new Line();
    @FXML
    Line line7 = new Line();
    @FXML
    Line line8 = new Line();

    int[][] gameBoard = new int[3][3]; // Plateau de jeu en mémoire
    public void setModelName(String modelName) {
        this.modelName = modelName;
        // Vous pouvez utiliser modelName ici pour initialiser la vue ou charger des données
    }
    @FXML
    void initialize() { // à l'initialisation de la fenêtre je lance le jeu
        victoryLabel.setOpacity(0);
        System.out.println("Initialisation du jeu");
        inializeGameBoard();
        turnLabel.setText("Turn of Player 1");
    }
    void inializeGameBoard(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                gameBoard[i][j] = 0;
            }
        }
    }

    @FXML
    void restartGame(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                gameBoard[i][j] = 0;
            }
        }
        case0_0.setImage(null);
        case0_1.setImage(null);
        case0_2.setImage(null);
        case1_0.setImage(null);
        case1_1.setImage(null);
        case1_2.setImage(null);
        case2_0.setImage(null);
        case2_1.setImage(null);
        case2_2.setImage(null);
        victoryLabel.setOpacity(0);
        turn = false;
        ingame = true;
        turnLabel.setVisible(true);
        turnLabel.setText("Turn of player 1");
        line1.setVisible(false);
        line2.setVisible(false);
        line3.setVisible(false);
        line4.setVisible(false);
        line5.setVisible(false);
        line6.setVisible(false);
        line7.setVisible(false);
        line8.setVisible(false);
        victoryLabel.setVisible(false);
    }

    private void showWinningLine(Line line) {
        line.setVisible(true); // Rend la ligne spécifiée visible
    }


    Line checkWinningLine(){
        if(gameBoard[0][0] == gameBoard[0][1] && gameBoard[0][1] == gameBoard[0][2]){
            return line1;
        }
        else if (gameBoard[1][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[1][2]){
            return line5;
        }
        else if (gameBoard[2][0] == gameBoard[2][1] && gameBoard[2][1] == gameBoard[2][2]){
            return line6;
        }
        else if (gameBoard[0][0] == gameBoard[1][0] && gameBoard[1][0] == gameBoard[2][0]){
            return line2;
        }
        else if (gameBoard[0][1] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][1]){
            return line3;
        }
        else if (gameBoard[0][2] == gameBoard[1][2] && gameBoard[1][2] == gameBoard[2][2]){
            return line4;
        }
        else if (gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2]){
            return line7;
        }
        else if (gameBoard[0][2] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][0]){
            return line8;
        }
        else return null;
    }
    void playAI() {
        /*
            * Fonction pour faire jouer l'IA
            * Elle load le modèle et joue sur la case à plus grande probabilité
         */
        if(!ingame){
            return;
        }
        double[] table = {gameBoard[0][0], gameBoard[0][1], gameBoard[0][2], gameBoard[1][0], gameBoard[1][1], gameBoard[1][2], gameBoard[2][0], gameBoard[2][1], gameBoard[2][2]};
        try {
            ai.MultiLayerPerceptron ai = MultiLayerPerceptron.load("src/main/resources/com/application/maven/models/"+modelName);
            double[] probas = ai.forwardPropagation(table);
            int maxIndex = 0;
            boolean moveMade = false;

            while (!moveMade) {
                for (int i = 0; i < 9; i++) {
                    if (probas[i] > probas[maxIndex]) {
                        maxIndex = i;
                    }
                }
                if (gameBoard[maxIndex / 3][maxIndex % 3] == 0) {
                    updateGameBoardUI(maxIndex);
                    moveMade = true;
                }
                probas[maxIndex] = -1;
            }
            int resultat = checkGameIsFinished();
            if(resultat == 1){
                victoryLabel.setText("Victory of player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(resultat == 2){
                victoryLabel.setText("Victory of AI");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            else if(resultat == 3){
                victoryLabel.setText("Tie Game");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            else{
                turnLabel.setText("Turn of player 1");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateGameBoardUI(int maxIndex) {
        Image img = new Image(getClass().getResource("/com/application/maven/images/TicTacToe/circle.png").toString());
        switch (maxIndex) {
            case 0:
                case0_0.setImage(img);
                gameBoard[0][0] = 2;
                break;
            case 1:
                case0_1.setImage(img);
                gameBoard[0][1] = 2;
                break;
            case 2:
                case0_2.setImage(img);
                gameBoard[0][2] = 2;
                break;
            case 3:
                case1_0.setImage(img);
                gameBoard[1][0] = 2;
                break;
            case 4:
                case1_1.setImage(img);
                gameBoard[1][1] = 2;
                break;
            case 5:
                case1_2.setImage(img);
                gameBoard[1][2] = 2;
                break;
            case 6:
                case2_0.setImage(img);
                gameBoard[2][0] = 2;
                break;
            case 7:
                case2_1.setImage(img);
                gameBoard[2][1] = 2;
                break;
            case 8:
                case2_2.setImage(img);
                gameBoard[2][2] = 2;
                break;

        }
    }

    int checkGameIsFinished(){
        // 0 = le jeu n'est pas fini
        // 1 = le joueur 1 a gagné
        // 2 = le joueur 2 a gagné
        // 3 = Tie
        System.out.println("Check Game");
        boolean matchNul = true;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(gameBoard[i][j] == 0){
                    matchNul = false;
                }
            }
        }

        for(int i = 0; i < 3; i++){
            if(gameBoard[i][0] == gameBoard[i][1] && gameBoard[i][1] == gameBoard[i][2]){
                if(gameBoard[i][0] == 1){
                    System.out.println("Victory of player 1");
                    return 1;

                }else if(gameBoard[i][0] == 2){
                    System.out.println("Victory of player 2");
                    return 2;
                }

            }
            else if(gameBoard[0][i] == gameBoard[1][i] && gameBoard[1][i] == gameBoard[2][i]){
                if(gameBoard[0][i] == 1){
                    System.out.println("Victory of player 1");
                    return 1;
                }else if(gameBoard[0][i] == 2){
                    System.out.println("Victory of player 2");
                    return 2;
                }
            }
        }
        if(gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2]){
            if(gameBoard[0][0] == 1){
                System.out.println("Victory of player 1");

                return 1;
            }else if(gameBoard[0][0] == 2){
                System.out.println("Victory of player 2");
                return 2;
            }
        }
        if(gameBoard[0][2] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][0]){
            if(gameBoard[0][2] == 1){
                System.out.println("Victory of player 1");
                return 1;
            }else if(gameBoard[0][2] == 2){
                System.out.println("Victory of player 2");
                return 2;
            }
        }
        if(matchNul){
            System.out.println("Tie");
            return 3;
        }
        return 0;
    }

    @FXML
    void winnerTransition(){ //Fait une transition fade sur le label de victoire qui de base est invisible
        System.out.println("Transition");
        Transition transition = new Transition() {
            {
                setCycleDuration(Duration.millis(1000));
            }
            protected void interpolate(double frac) {
                victoryLabel.setOpacity(frac);
                restartButton.setOpacity(frac);
            }
        };
        if(checkGameIsFinished() != 3){
            showWinningLine(checkWinningLine());
        }
        victoryLabel.setVisible(true);
        restartButton.setVisible(true);
        turnLabel.setVisible(false);
        transition.play();

    }
    @FXML
    void returnToMainMenu(ActionEvent event) throws IOException { //Fonction  du bouton retour
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainFXML.fxml"));
        Parent mainContent = mainLoader.load();
        Stage currentStage = (Stage)((Node)event.getSource()).getScene().getWindow();
        currentStage.getScene().setRoot(mainContent);
    }

    // en dessous les fonctions pour les clicks sur les cases
    @FXML
    void OnClick0_0(){ // Onclick sur la case 0_0 ainsi de suite pour les fonctions suivantes
        System.out.println("Click sur 0_0");
        if(gameBoard[0][0] == 0 && ingame){
            gameBoard[0][0] = 1;
            case0_0.setImage(new Image(getClass().getResource("/com/application/maven/images/TicTacToe/cross.png").toString()));
            turnLabel.setText("Turn of player 2");
            int result = checkGameIsFinished();
            if(result == 1){
                victoryLabel.setText("Victory of player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 2){
                victoryLabel.setText("Victory of player 2");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 3){
                victoryLabel.setText("Tie");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            if(ingame){
                playAI();
            }
        }


    }
    @FXML
    void OnClick0_1(){
        if(gameBoard[0][1] == 0 && ingame){
            gameBoard[0][1] = 1;
            case0_1.setImage(new Image(getClass().getResource("/com/application/maven/images/TicTacToe/cross.png").toString()));
            turnLabel.setText("Tour de l'IA");
            int result = checkGameIsFinished();
            if(result == 1){
                victoryLabel.setText("Victory of player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 2){
                victoryLabel.setText("Victory of player 2");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 3){
                victoryLabel.setText("Tie");
                winnerTransition();
                ingame = false;
            }
            if(ingame){
                playAI();
            }
        }

    }
    @FXML
    void OnClick0_2(){
        if(gameBoard[0][2] == 0 && ingame){
            gameBoard[0][2] = 1;
            case0_2.setImage(new Image(getClass().getResource("/com/application/maven/images/TicTacToe/cross.png").toString()));
            turnLabel.setText("Turn of AI");
            int result = checkGameIsFinished();
            if(result == 1){
                victoryLabel.setText("Victory of player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 2){
                victoryLabel.setText("Victory of player 2");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 3){
                victoryLabel.setText("Tie");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            if(ingame){
                playAI();
            }
        }

    }
    @FXML
    void OnClick1_0(){
        if(gameBoard[1][0] == 0 && ingame){
            System.out.println("Click sur 1_0");
            gameBoard[1][0] = 1;
            case1_0.setImage(new Image(getClass().getResource("/com/application/maven/images/TicTacToe/cross.png").toString()));
            turnLabel.setText("Turn of AI");
            int result = checkGameIsFinished();
            if(result == 1){
                victoryLabel.setText("Victory of player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 2){
                victoryLabel.setText("Victory of player 2");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 3){
                victoryLabel.setText("Tie");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            if(ingame){
                playAI();
            }
        }

    }
    @FXML
    void OnClick1_1(){
        if(gameBoard[1][1] == 0 && ingame){
            gameBoard[1][1] = 1;
            case1_1.setImage(new Image(getClass().getResource("/com/application/maven/images/TicTacToe/cross.png").toString()));
            turnLabel.setText("Turn of AI");
            int result = checkGameIsFinished();
            if(result == 1){
                victoryLabel.setText("Victory of player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 2){
                victoryLabel.setText("Victory of player 2");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 3){
                victoryLabel.setText("Tie");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            if(ingame){
                playAI();
            }
        }

    }
    @FXML
    void OnClick1_2(){
        if(gameBoard[1][2] == 0 && ingame){
            gameBoard[1][2] = 1;
            case1_2.setImage(new Image(getClass().getResource("/com/application/maven/images/TicTacToe/cross.png").toString()));
            turnLabel.setText("Turn of player 2");
            int result = checkGameIsFinished();
            System.out.println("Resultat : " + result);
            if(result == 1){
                victoryLabel.setText("Victory of player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 2){
                victoryLabel.setText("Victory of player 2");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 3){
                victoryLabel.setText("Tie");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            if(ingame){
                playAI();
            }
        }

    }
    @FXML
    void OnClick2_0(){
        if(gameBoard[2][0] == 0 && ingame){
            gameBoard[2][0] = 1;
            case2_0.setImage(new Image(getClass().getResource("/com/application/maven/images/TicTacToe/cross.png").toString()));
            turnLabel.setText("Turn of AI");
            int result = checkGameIsFinished();
            if(result == 1){
                victoryLabel.setText("Victory of player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 2){
                victoryLabel.setText("Victory of player 2");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 3){
                victoryLabel.setText("Tie");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            if(ingame){
                playAI();
            }
        }

    }
    @FXML
    void OnClick2_1(){
        if(gameBoard[2][1] == 0 && ingame){
            gameBoard[2][1] = 1;
            case2_1.setImage(new Image(getClass().getResource("/com/application/maven/images/TicTacToe/cross.png").toString()));
            turnLabel.setText("Turn of AI");
            int result = checkGameIsFinished();
            if(result == 1){
                victoryLabel.setText("Victory of player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 2){
                victoryLabel.setText("Victory of player 2");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 3){
                victoryLabel.setText("Tie");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            if(ingame){
                playAI();
            }
        }

    }
    @FXML
    void OnClick2_2(){
        if(gameBoard[2][2] == 0 && ingame){
            gameBoard[2][2] = 1;
            case2_2.setImage(new Image(getClass().getResource("/com/application/maven/images/TicTacToe/cross.png").toString()));
            turnLabel.setText("Turn of AI");
            int result = checkGameIsFinished();
            if(result == 1){
                victoryLabel.setText("Victory for player 1");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 2){
                victoryLabel.setText("Victory for AI");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }else if(result == 3){
                victoryLabel.setText("Tie Game");
                if(ingame){
                    winnerTransition();
                }
                ingame = false;
            }
            if(ingame){
                playAI();
            }
        }

    }

}
