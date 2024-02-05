package com.example.goblicijedlici;

import javafx.scene.image.ImageView;

import java.io.*;
import java.util.*;

/**
 * logika za UI
 */
public class GobliciJedliciState implements Serializable {
    private static final long serialVersionUID = 918972645L;
    /**
     * hrac na rade
     */
    int actualPlayer = 1;
    /**
     * hracia plocha
     */
    CardState[][] surface = new CardState[GobliciJedlici.ROWS][GobliciJedlici.COLUMNS];
    /**
     * kto vyhral
     */
    int whoWon = 0;

    /**
     * logika policka
     */
    public static class CardState implements Serializable {
        private static final long serialVersionUID = 911775039L;
        int id;
        /**
         * goblici na policku
         */
        Stack<Goblik> goblici = new Stack<>();

        public CardState(int id) {
            this.id = id;
        }
    }

    /**
     * Vygeneruje hraciu plochu
     */
    public GobliciJedliciState(int playersTurn) {
        this.actualPlayer = playersTurn;
        int p = 0;
        for (int i = 0; i < GobliciJedlici.ROWS; i++) {
            for (int j = 0; j < GobliciJedlici.COLUMNS; j++) {
                surface[i][j] = new CardState(p);
                if (j < 2){
                    surface[i][j].goblici.add(new Goblik("Zlty",3-i,1));
                } else if (j > 4) {
                    surface[i][j].goblici.add(new Goblik("Cerveny",3-i,2));
                }
                p++;
            }
        }
    }

    /**
     * trieda pre goblikov
     */
    public static class Goblik implements Serializable{
        /**
         * farba
         */
        String color;
        /**
         * velkost goblika
         */
        int size;
        /**
         * obrazok goblika
         */
        transient ImageView goblikImage;
        /**
         * hrac na rade
         */
        int player;
        /**
         * sirka
         */
        double width;
        /**
         * vyska
         */
        double heigth;
        Goblik(String color, int size, int player){
            this.color = color;
            this.size = size;
            goblikImage = getGoblikImage();
            this.player = player;
            if (size == 3){
                this.width = 140;
                this.heigth = 140;
            } else if (size == 2) {
                this.width = 100;
                this.heigth = 100;
            }
            else if (size == 1) {
                this.width = 50;
                this.heigth = 50;
            }
        }

        /**
         * dostan obrazok goblika
         * @return obrazok
         */
        public ImageView getGoblikImage(){
            return new ImageView("file:goblik"+ color +".png");
        }

    }

    /**
     * skontroluje ci niektory z hracov vyhral
     * @param playerID hrac
     * @return
     */
    public boolean checkIfWon(int playerID){
            for (int i = 0; i < 3; i++) {
                try {
                    if ((surface[i][2].goblici.peek().player == surface[i][3].goblici.peek().player)
                            && (surface[i][3].goblici.peek().player == surface[i][4].goblici.peek().player) && (surface[i][4].goblici.peek().player == playerID)) {
                        return true;
                    }
                }catch (EmptyStackException e){

                }
            }
            for (int i = 2; i < 5; i++) {
                try {
                    if ((surface[0][i].goblici.peek().player == surface[1][i].goblici.peek().player)
                            && (surface[2][i].goblici.peek().player == surface[1][i].goblici.peek().player) && (surface[2][i].goblici.peek().player == playerID)) {
                        return true;
                    }
                }catch (EmptyStackException e){

                }
            }
            try {
                if ((surface[0][2].goblici.peek().player == surface[1][3].goblici.peek().player)
                        && (surface[2][4].goblici.peek().player == surface[1][3].goblici.peek().player) && (surface[2][4].goblici.peek().player == playerID)) {
                    return true;
                }
            }catch (EmptyStackException e){

            }
            try {
                if ((surface[0][4].goblici.peek().player == surface[1][3].goblici.peek().player)
                        && (surface[2][2].goblici.peek().player == surface[1][3].goblici.peek().player) && (surface[2][2].goblici.peek().player == playerID)) {
                    return true;
                }
            }catch (EmptyStackException e){

            }
        return false;
    }

    /**
     * vrati policko
     * @param i riadok
     * @param j stlpec
     * @return
     */
    public CardState get(int i, int j) {
        if (0 <= i && i < surface.length && 0 <= j && j < surface[i].length)
            return surface[i][j];
        else
            return null;
    }
}
