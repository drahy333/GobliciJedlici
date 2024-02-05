package com.example.goblicijedlici;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

/**
 * UI
 */
public class GobliciJedlici extends Application {
    /**
     * pocet riadkov
     */
    static final int ROWS = 3;
    /**
     * pocet stlpcov
     */
    static final int COLUMNS = 7;
    /**
     * plocha celej aplikacie
     */
    BorderPane root;
    /**
     * plocha piskvoriek
     */
    PlaygroundPane playground;
    /**
     * ktory hrac je na tahu
     */
    Label lbPlayer = new Label();
    /**
     * nova hra
     */
    Button new_game = new Button("New game");
    /**
     * stav hry
     */

    GobliciJedliciState state = new GobliciJedliciState(1);
    /**
     * prvy klik
     */
    GobliciJedliciState.CardState first_click = null;
    /**
     * druhy klik
     */
    GobliciJedliciState.CardState second_click = null;

    public class CardPane extends Pane {
        int row, col;

        /**
         * riadi pohyb goblikov, ktory goblik ide a kam
         * @param row riadok
         * @param col stlpec
         */
        public CardPane(int row, int col) {
            this.row = row;
            this.col = col;
            this.setWidth(40);
            this.setHeight(40);

            setOnMouseClicked(ev -> {
                System.out.println("klikol si "+ row + " " + col);
                if (state.whoWon == 0){
                    var click = state.get(row, col);
                    if (first_click == null && ! click.goblici.isEmpty() && click.goblici.peek().player == state.actualPlayer) {
                        first_click = click;
                    }
                    else if(first_click != null && second_click == null && (col > 1 && col < 5)) {
                        second_click = state.get(row, col);
                        if (second_click.goblici.isEmpty() || second_click.goblici.peek().size < first_click.goblici.peek().size) {
                            var moveGoblik = first_click.goblici.pop();
                            second_click.goblici.add(moveGoblik);
                            first_click = null;
                            second_click = null;
                            if (state.actualPlayer == 1){
                                if (state.checkIfWon(2)){
                                    System.out.println("Vyhral hrac 2");
                                    state.whoWon = 2;
                                }
                                else if (state.checkIfWon(1)){
                                    System.out.println("Vyhral hrac 1");
                                    state.whoWon = 1;
                                }
                                else {
                                    state.actualPlayer = 2;
                                }
                            }else{
                                if (state.checkIfWon(1)){
                                    System.out.println("Vyhral hrac 1");
                                    state.whoWon = 1;
                                }
                                else if (state.checkIfWon(2)){
                                    System.out.println("Vyhral hrac 2");
                                    state.whoWon = 2;
                                }
                                else {
                                    state.actualPlayer = 1;
                                }
                            }
                            playground.paint();

                        }else{
                            second_click = null;
                        }
                    }
                    else{
                        first_click = null;
                    }
            }});
        }

        /**
         * kresli policko + ak je na nom goblik tak aj toho
         */
        public void paint() {
            double w = 150;
            double h = 150;
            getChildren().clear();
            GobliciJedliciState.CardState pane = state.get(row, col);
            Rectangle r = new Rectangle(0, 0, w, h);
            if (col < 2 || col > 4){
                r.setFill(Color.LIGHTBLUE);
            }else {
                r.setFill(Color.WHITE);
                r.setStroke(Color.BLACK);
                r.setStrokeWidth(2);
            }
            StackPane stackPane = new StackPane(r);
            stackPane.setAlignment(Pos.CENTER);
            getChildren().addAll(stackPane);
            if (!pane.goblici.isEmpty()) {
                GobliciJedliciState.Goblik g = state.get(row,col).goblici.peek();
                ImageView obrazok = g.goblikImage;
                if (obrazok == null){
                    obrazok = g.getGoblikImage();
                }
                obrazok.setFitWidth(g.width);
                obrazok.setFitHeight(g.heigth);
                obrazok.setX(( 150-g.width )/ 2);
                obrazok.setY(( 150-g.heigth )/ 2);
                getChildren().addAll(obrazok);
            }
        }
    }

    public class PlaygroundPane extends GridPane {

        public PlaygroundPane() {
            setWidth(800);
            setHeight(800);
            for (int row = 0; row < GobliciJedlici.ROWS; row++) {
                for (int column = 0; column < GobliciJedlici.COLUMNS; column++) {
                    CardPane card = new CardPane(row, column);
                    this.add(card, column, row);
                    card.paint();
                }
            }
        }

        public void paint() {
            for(Node n: getChildren()){
                if(n instanceof CardPane){
                    CardPane cp = (CardPane) n;
                    cp.paint();
                }

            }
            String s = Integer.toString(state.actualPlayer);
            if (state.whoWon == 0) {
                lbPlayer.setText("Na tahu je hrac cislo : " + s);
            }else{
                lbPlayer.setText("Vyhral hrac " +state.whoWon +", ak chcete zacat novu hru stlacte tlacidlo New game");
            }
        }
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new BorderPane();
        new_game.setOnAction(e -> {
            if (state.whoWon== 1){
                state = new GobliciJedliciState(2);
            }
            else {
                state = new GobliciJedliciState(1);
            }
            playground.paint();
        });

        HBox topPanel = new HBox(lbPlayer);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setSpacing(40);
        root.setTop(topPanel);

        HBox bottomPanel = new HBox(new_game);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setSpacing(20);
        root.setBottom(bottomPanel);

        root.setCenter(playground = new PlaygroundPane());

        primaryStage.setTitle("Goblici jedlici");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        playground.paint();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
