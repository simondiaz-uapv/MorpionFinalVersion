package com.application.maven;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class MusicPlayer {
    /*
    * Classe pour gérer la lecture de la musique de fond
     */
    private static MediaPlayer mediaPlayer;
    private static int checkVolume() throws IOException {
        List<String> list = Files.lines(Paths.get("src/main/resources/com/application/maven/settings.txt")).collect(Collectors.toList());
        if(list != null) {
            String[] tab = list.get(0).split(":");
            return Integer.parseInt(tab[1]);
        }
        else{
            return 25;
        }
    }
    public static String checkMusic() throws IOException {

        List<String> list = Files.lines(Paths.get("src/main/resources/com/application/maven/settings.txt")).collect(Collectors.toList());
        if(list != null) {
            String[] tab = list.get(2).split(":");

            if(Integer.parseInt(tab[1])==1){
                return "src/main/resources/com/application/maven/music/electronicTheme.wav";
            }
            else{
                return "src/main/resources/com/application/maven/music/retroTheme.wav";
            }
        }
        else{
            return "src/main/resources/com/application/maven/music/electronicTheme.wav";
        }

    }
    public static void playMusic() throws IOException {
        System.out.println("Playing music");
        // Définir le chemin d'accès au fichier musical
        String musicFile = checkMusic();

        // Créer un objet Media
        Media sound = new Media(new File(musicFile).toURI().toString());

        // Créer un MediaPlayer
        if (mediaPlayer != null) {
            System.out.println("NULL");
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer(sound);
        int volume = checkVolume();
        mediaPlayer.setVolume(volume/100.0);
        // Mettre la musique en boucle
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Démarrer la lecture
        mediaPlayer.play();
    }

    public static void changeMusic() throws IOException {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        String music = checkMusic();
        int volume = checkVolume();
        Media sound = new Media(new File(music).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(volume/100.0);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }
    public static void changeVolume(int volume)throws IOException{
        mediaPlayer.setVolume(volume/100.0);
    }
}
