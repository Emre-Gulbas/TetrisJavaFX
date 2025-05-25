package com.example.tetris;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.*;
import java.util.Scanner;

public class LoginAndRegister {
    public Scene login(Stage primaryStage) throws FileNotFoundException {
        String logFile = "Login.txt";

        VBox loginHolder = new VBox();
        loginHolder.setAlignment(Pos.CENTER);
        loginHolder.setLayoutX(700);
        loginHolder.setLayoutY(350);

        Label username = new Label("Username: ");
        Label password = new Label("Password: ");

        TextField nameEnter = new TextField();
        PasswordField passEnter = new PasswordField();

        Label result = new Label();

        Button enterButton = new Button("LOGIN");
        Button registerButton=new Button("REGISTER");

        loginHolder.setSpacing(10);

        loginHolder.getChildren().addAll(username,nameEnter,password,passEnter,enterButton,registerButton,result);

        Pane holderAll = new Pane(loginHolder);
        Tetris mainMenu =new Tetris();

        enterButton.setOnAction(e->{
            try {
                Scanner logReader= new Scanner(new File(logFile));
                while(logReader.hasNextLine()){
                    String logLine =logReader.nextLine();
                    String[] partWays=logLine.split(",");

                    if(partWays.length==2 && partWays[0].equals(nameEnter.getText()) &&partWays[1].equals(passEnter.getText())){
                        result.setText("Successfully Logged In.");
                        result.toFront();
                        SessionProfile.username = nameEnter.getText();
                        mainMenu.mainMenuScene(primaryStage);
                        logReader.close();
                        return;
                    }
                }
                result.setText("Incorrect Username or Password.");
                result.toFront();

            } catch (IOException ex) {
                result.setText("Error! There is no login file.");
                ex.printStackTrace();
            }
        });

        registerButton.setOnAction(e-> primaryStage.setScene(registerScene(primaryStage)));

        return new Scene(holderAll,1520,800);
    }

    public Scene registerScene(Stage secondaryStage){
        String logFile = "Login.txt";
        VBox registerHolder=new VBox();

        registerHolder.setLayoutX(700);
        registerHolder.setLayoutY(350);

        Label newUsername = new Label("Username: ");
        Label newPassword = new Label("Password: ");

        TextField newNameEnter = new TextField();
        PasswordField newPassEnter = new PasswordField();

        Label result2 = new Label();

        Button registerButton2=new Button("REGISTER");

        registerHolder.setSpacing(10);

        registerHolder.getChildren().addAll(newUsername,newNameEnter,newPassword,newPassEnter,registerButton2,result2);

        Pane registerRoot = new Pane(registerHolder);

        registerButton2.setOnAction(e-> {
            try {
                FileWriter fW= new FileWriter(logFile,true);
                BufferedWriter bW = new BufferedWriter(fW);

                bW.write(newNameEnter.getText()+","+ newPassEnter.getText());
                bW.newLine();
                bW.close();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                secondaryStage.setScene(login(secondaryStage));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        return new Scene(registerRoot, 1520, 800);
    }

}