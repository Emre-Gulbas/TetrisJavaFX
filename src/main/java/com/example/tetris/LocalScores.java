package  com.example.tetris;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalScores {
    public void localSaving(String username, int score, AtomicInteger time){

        String scoreFile = "Score.txt";

        try{
            FileWriter fS = new FileWriter(scoreFile,true);
            BufferedWriter fL= new BufferedWriter(fS);

            fL.write(username+","+score+","+time);
            fL.newLine();
            fL.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Scene leaderboard(Stage primaryStage) throws IOException {
        File scoreFile = new File("Score.txt");
        if (!scoreFile.exists()) {
            scoreFile.createNewFile(); // ✅ Prevent crash before Scanner
        }
        TableView<AllEntries> leaderboard=new TableView<>();
        try{
            Scanner scoreReader= new Scanner(new File(String.valueOf(scoreFile)));
            while(scoreReader.hasNextLine()){
                String Scoreline=scoreReader.nextLine();
                String[] partWays=Scoreline.split(",",3);
                String nameEntry= partWays[0];
                int scoreEntry= Integer.parseInt(partWays[1]);
                int timeEntry= Integer.parseInt(partWays[2]);

                AllEntries entries= new AllEntries(nameEntry,scoreEntry,timeEntry);
                leaderboard.getItems().add(entries);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        TableColumn nameColumn=new TableColumn("Username");
        nameColumn.setMaxWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<AllEntries,String>("username"));
        nameColumn.setSortable(true);
        TableColumn scoreColumn=new TableColumn("Score");
        scoreColumn.setMaxWidth(100);
        scoreColumn.setSortable(true);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<AllEntries,Integer>("score"));
        TableColumn timeColumn=new TableColumn("Time");
        timeColumn.setMaxWidth(100);
        timeColumn.setSortable(true);
        timeColumn.setCellValueFactory(new PropertyValueFactory<AllEntries,Integer>("time"));

        leaderboard.getColumns().addAll(nameColumn,scoreColumn,timeColumn);
        leaderboard.requestFocus();

        Button backbutton=new Button("RETURN");
        Tetris returnMain = new Tetris();
        backbutton.setOnAction(e-> {
            try {
                returnMain.mainMenuScene(primaryStage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        VBox backline=new VBox(backbutton);
        backline.setAlignment(Pos.BOTTOM_LEFT);
        backline.setPadding(new Insets(10));

        StackPane rootLBoard=new StackPane();
        rootLBoard.getChildren().addAll(leaderboard,backline);
        StackPane.setAlignment(backline, Pos.BOTTOM_LEFT);
        Scene lboardScene=new Scene(rootLBoard,1520,800);

        return lboardScene;
    }
}