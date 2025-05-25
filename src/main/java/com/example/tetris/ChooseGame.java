package com.example.tetris;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import java.io.IOException;

public class ChooseGame {
    public Scene chooseScene(Stage primarystage){

        GameOnePlayer singlePlay= new GameOnePlayer();
        GameTwoPlayer twoPlay=new GameTwoPlayer();

        Button onePlayerButton=new Button("1-PLAYER");
        Button twoPlayerButton=new Button("2-PLAYER");
        Button backbutton=new Button("RETURN");

        onePlayerButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        twoPlayerButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        backbutton.setMaxWidth(Double.MAX_VALUE);

        HBox buttonBox= new HBox(0, onePlayerButton, twoPlayerButton);
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(onePlayerButton, Priority.ALWAYS);
        HBox.setHgrow(twoPlayerButton, Priority.ALWAYS);

        backbutton.setAlignment(Pos.BOTTOM_CENTER);

        VBox vBox=new VBox(0, buttonBox, backbutton);
        vBox.setPrefSize(1520,800);
        VBox.setVgrow(buttonBox, Priority.ALWAYS);

        Tetris mainReturn=new Tetris();

        onePlayerButton.setOnAction(e-> {Scene onePlayer = singlePlay.singleplayerScene(primarystage);
        primarystage.setScene(onePlayer);});

        twoPlayerButton.setOnAction(e-> {Scene twoPlayer = twoPlay.twoplayerScene(primarystage);
            primarystage.setScene(twoPlayer);});

        backbutton.setOnAction(e-> {
            try {
                mainReturn.mainMenuScene(primarystage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
                return new Scene(vBox);
    }
}