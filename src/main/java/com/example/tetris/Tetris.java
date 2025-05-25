package com.example.tetris;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Tetris extends Application {
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {

        primaryStage.setTitle("Tetris Project");
        LoginAndRegister landR = new LoginAndRegister();
        Scene lR= landR.login(primaryStage);
        primaryStage.setScene(lR);
        primaryStage.show();

    }

    public Scene mainMenuScene(Stage primaryStage) throws IOException {
        VBox root=new VBox(20);
        Label tetrisLabel= new Label("TETRIS");
        tetrisLabel.setFont(Font.font(150));
        Button playbutton=new Button("PLAY");
        Button lboardbutton=new Button("LEADERBOARD");
        Button settingsbutton=new Button("SETTINGS");

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(tetrisLabel,playbutton,lboardbutton,settingsbutton);
        Scene menuscene=new Scene(root,1520,800);

        ChooseGame gameSce = new ChooseGame();
        Scene gameScene= gameSce.chooseScene(primaryStage);

        LocalScores lBoardSce= new LocalScores();
        Scene lBoardScene= lBoardSce.leaderboard(primaryStage);

        Label label3=new Label("For Future");

        Button backButton2=new Button("RETURN");
        backButton2.setOnAction(e-> primaryStage.setScene(menuscene));
        VBox backline2=new VBox(backButton2);
        backline2.setAlignment(Pos.BOTTOM_LEFT);
        backline2.setPadding(new Insets(10));

        StackPane rootSettings=new StackPane();
        rootSettings.getChildren().addAll(label3,backline2);
        StackPane.setAlignment(backline2, Pos.BOTTOM_LEFT);
        Scene settingsScene=new Scene(rootSettings,1520,800);

        playbutton.setOnAction(e-> primaryStage.setScene(gameScene));

        lboardbutton.setOnAction(e-> primaryStage.setScene(lBoardScene));

        settingsbutton.setOnAction(e-> primaryStage.setScene(settingsScene));

        primaryStage.setScene(menuscene);
        primaryStage.show();

        return menuscene;
    }
    public static void main(String[] args) {

        launch(args);
    }

}

//Scene Authscene=new Scene(root,1520,800);
