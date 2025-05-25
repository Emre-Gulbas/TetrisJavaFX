package com.example.tetris;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class GameTwoPlayer {
    private int num1=0;
    private int num2=0;
    public Scene twoplayerScene(Stage primaryStage){

        final int rows= 20;
        final int columns= 10;

        int [][] grid1= new int[rows][columns];
        int [][] grid2= new int[rows][columns];
        Rectangle[][] grids1=new Rectangle[rows][columns];
        Rectangle[][] grids2=new Rectangle[rows][columns];

        final int cellSize= 30;

        Group gridGroup1 =new Group();
        Group gridGroup2 =new Group();
        Rectangle pieceHolder1=new Rectangle(200,200);
        Rectangle pieceHolder2=new Rectangle(200,200);
        Rectangle pointHolder1=new Rectangle(200,100);
        Rectangle pointHolder2=new Rectangle(200,100);
        Rectangle timeHolder1=new Rectangle(200,100);
        Rectangle timeHolder2=new Rectangle(200,100);

        for (int row = 0; row < 20; row++) {
            for (int column = 0; column < 10; column++) {
                Rectangle cell1 = new Rectangle(cellSize, cellSize);
                Rectangle cell2 = new Rectangle(cellSize, cellSize);
                cell1.setFill(Color.BLACK);
                cell1.setStroke(Color.GRAY);
                cell1.setX(column * cellSize);
                cell1.setY(row * cellSize);
                cell2.setFill(Color.BLACK);
                cell2.setStroke(Color.GRAY);
                cell2.setX(column * cellSize);
                cell2.setY(row * cellSize);
                gridGroup1.getChildren().add(cell1);
                gridGroup2.getChildren().add(cell2);
                grids1[row][column]= cell1;
                grids2[row][column]= cell2;
                grid1[row][column]=0;
                grid2[row][column]=0;

            }
        }

        gridGroup1.setLayoutX(300);
        gridGroup1.setLayoutY(100);
        gridGroup2.setLayoutX(920);
        gridGroup2.setLayoutY(100);

        pieceHolder1.setFill(Color.GRAY);
        pieceHolder1.setStroke(Color.BLACK);
        pieceHolder1.setLayoutX(60);
        pieceHolder1.setLayoutY(100);

        pieceHolder2.setFill(Color.GRAY);
        pieceHolder2.setStroke(Color.BLACK);
        pieceHolder2.setLayoutX(1260);
        pieceHolder2.setLayoutY(100);

        pointHolder1.setFill(Color.GRAY);
        pointHolder1.setStroke(Color.BLACK);
        pointHolder1.setLayoutX(60);
        pointHolder1.setLayoutY(350);

        pointHolder2.setFill(Color.GRAY);
        pointHolder2.setStroke(Color.BLACK);
        pointHolder2.setLayoutX(1260);
        pointHolder2.setLayoutY(350);

        timeHolder1.setFill(Color.GRAY);
        timeHolder1.setStroke(Color.BLACK);
        timeHolder1.setLayoutX(60);
        timeHolder1.setLayoutY(500);

        timeHolder2.setFill(Color.GRAY);
        timeHolder2.setStroke(Color.BLACK);
        timeHolder2.setLayoutX(1260);
        timeHolder2.setLayoutY(500);

        Pane stacker= new Pane();
        Tetromino[] nextPiece1= new Tetromino[1];
        Tetromino[] nextPiece2= new Tetromino[1];
        nextPiece1[0]= newPiece(gridGroup1);
        nextPiece2[0]= newPiece(gridGroup2);
        Group nextGroup1= new Group();
        Group nextGroup2= new Group();
        for (Rectangle block: nextPiece1[0].blocks){
            nextGroup1.getChildren().add(block);
        }
        for (Rectangle block: nextPiece2[0].blocks){
            nextGroup2.getChildren().add(block);
        }
        nextGroup1.setLayoutX(30);
        nextGroup1.setLayoutY(150);

        nextGroup2.setLayoutX(1225);
        nextGroup2.setLayoutY(150);

        Text score1= new Text("Score: "+num1);
        Text score2= new Text("Score: "+num2);
        AtomicInteger count1= new AtomicInteger();
        AtomicInteger count2= new AtomicInteger();

        score1.setLayoutX(75);
        score1.setLayoutY(410);
        score1.setFill(Color.BLACK);
        score1.setFont(Font.font(24));

        score2.setLayoutX(1275);
        score2.setLayoutY(410);
        score2.setFill(Color.BLACK);
        score2.setFont(Font.font(24));

        AtomicInteger time1= new AtomicInteger();
        AtomicInteger time2= new AtomicInteger();
        Text timer1= new Text("Time: "+time1);
        Text timer2= new Text("Time: "+time2);

        timer1.setLayoutX(75);
        timer1.setLayoutY(560);
        timer1.setFill(Color.BLACK);
        timer1.setFont(Font.font(24));

        timer2.setLayoutX(1275);
        timer2.setLayoutY(560);
        timer2.setFill(Color.BLACK);
        timer2.setFont(Font.font(24));

        stacker.getChildren().clear();
        stacker.getChildren().addAll(pieceHolder1, nextGroup1, pointHolder1,score1,timeHolder1,timer1,pieceHolder2,nextGroup2,pointHolder2,score2,timeHolder2,timer2);

        Tetromino[] currentPiece1 = new Tetromino[1];
        Tetromino[] currentPiece2 = new Tetromino[1];
        currentPiece1[0]= newPiece(gridGroup1);
        currentPiece2[0]= newPiece(gridGroup2);

        Tetris mainReturn=new Tetris();
        Pane whenOver=new Pane();

        Timeline timeline = new Timeline();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), e -> {
            if (currentPiece1[0].canMoveDown(grid1,20,10)){
                currentPiece1[0].moveDown(grid1,20,10,30);
            }else{
                currentPiece1[0].lockToGrid(grid1, grids1);

                for(int row=0; row<grid1.length;row++){
                    if(rowControl(grid1,row)){
                        clearRow(grid1,grids1,row);
                        shiftRowsDown(grid1,grids1,row);
                        count1.getAndIncrement();
                        row--;
                    }
                }
                scoreCount1(score1, count1);
                count1.set(0);

                currentPiece1[0].removeFromGroup(gridGroup1);
                currentPiece1[0] = newNextPiece(gridGroup1,nextPiece1[0].getType());
                nextPiece1[0]=newPiece(gridGroup1);

                if (grid1[0][4]==1||grid1[1][4]==1){
                    timeline.stop();
                    VBox overHolder = new VBox();
                    overHolder.setAlignment(Pos.CENTER);
                    Text gameOver= new Text("PLAYER 1 LOSES");
                    gameOver.setFont(Font.font(100));
                    gameOver.setFill(Color.DARKRED);
                    LocalScores scoreSave=new LocalScores();
                    String username= SessionProfile.username;
                    scoreSave.localSaving(username,num1,time1);
                    Button returnOver = new Button("RETURN TO MAIN MENU");
                    returnOver.setOnAction(a-> {
                        try {
                            mainReturn.mainMenuScene(primaryStage);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    overHolder.getChildren().addAll(gameOver,returnOver);
                    overHolder.setLayoutX(300);
                    overHolder.setLayoutY(200);
                    whenOver.getChildren().add(overHolder);
                    whenOver.toFront();
                }

                nextGroup1.getChildren().clear();
                for (Rectangle block: nextPiece1[0].blocks){
                    nextGroup1.getChildren().add(block);
                }
            }

            if (currentPiece2[0].canMoveDown(grid2,20,10)){
                currentPiece2[0].moveDown(grid2,20,10,30);
            }else{
                currentPiece2[0].lockToGrid(grid2, grids2);

                for(int row=0; row<grid2.length;row++){
                    if(rowControl(grid2,row)){
                        clearRow(grid2,grids2,row);
                        shiftRowsDown(grid2,grids2,row);
                        count2.getAndIncrement();
                        row--;
                    }
                }
                scoreCount2(score2, count2);
                count2.set(0);

                currentPiece2[0].removeFromGroup(gridGroup2);
                currentPiece2[0] = newNextPiece(gridGroup2,nextPiece2[0].getType());
                nextPiece2[0]=newPiece(gridGroup2);

                if (grid2[0][4]==1||grid2[1][4]==1){
                    timeline.stop();
                    VBox overHolder = new VBox();
                    overHolder.setAlignment(Pos.CENTER);
                    Text gameOver= new Text("PLAYER 2 LOSES");
                    gameOver.setFont(Font.font(100));
                    gameOver.setFill(Color.DARKRED);
                    LocalScores scoreSave=new LocalScores();
                    String username= SessionProfile.username;
                    scoreSave.localSaving(username,num2,time2);
                    Button returnOver = new Button("RETURN TO MAIN MENU");
                    returnOver.setOnAction(a-> {
                        try {
                            mainReturn.mainMenuScene(primaryStage);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    overHolder.getChildren().addAll(gameOver,returnOver);
                    overHolder.setLayoutX(500);
                    overHolder.setLayoutY(200);
                    whenOver.getChildren().add(overHolder);
                    whenOver.toFront();
                }

                nextGroup2.getChildren().clear();
                for (Rectangle block: nextPiece2[0].blocks){
                    nextGroup2.getChildren().add(block);
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Timeline countTimer=new Timeline();
        countTimer.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e->{
            time1.addAndGet(1);
            time2.addAndGet(1);
            timer1.setText("Time: "+time1+"s");
            timer2.setText("Time: "+time2+"s");

            if (grid1[0][4]==1||grid1[1][4]==1||grid2[0][4]==1||grid2[1][4]==1){
                countTimer.stop();
            }
        }));
        countTimer.setCycleCount(Animation.INDEFINITE);
        countTimer.play();

        Button pause= new Button("PAUSE");
        pause.setLayoutX(750);
        pause.setLayoutY(100);
        AtomicInteger buttonCount = new AtomicInteger();

        Button returnMain= new Button("RETURN TO MAIN MENU");
        returnMain.setLayoutX(750);
        returnMain.setLayoutY(150);

        Pane rootGame = new Pane();
        rootGame.getChildren().addAll(gridGroup1,gridGroup2,stacker,pause,returnMain,whenOver);
        rootGame.requestFocus();

        Scene scene= new Scene(rootGame,1520,800);

        rootGame.requestFocus();

        scene.setOnKeyPressed(e->{
            switch (e.getCode()){
                case LEFT -> currentPiece2[0].moveLeft(grid2,20,10,30);
                case RIGHT -> currentPiece2[0].moveRight(grid2,20,10,30);
                case UP -> {
                    if (currentPiece2[0].canRotate(grid2,20,10)){
                        currentPiece2[0].rotate(grid2,20,10,30);
                    }
                }
                case DOWN -> {
                    if (currentPiece2[0].canMoveDown(grid2,20,10)){
                        currentPiece2[0].moveDown(grid2,20,10,30);
                    }
                }

                case A -> currentPiece1[0].moveLeft(grid1,20,10,30);
                case D -> currentPiece1[0].moveRight(grid1,20,10,30);
                case W -> {
                    if (currentPiece1[0].canRotate(grid1,20,10)){
                        currentPiece1[0].rotate(grid1,20,10,30);
                    }
                }
                case S -> {
                    if (currentPiece1[0].canMoveDown(grid1,20,10)){
                        currentPiece1[0].moveDown(grid1,20,10,30);
                    }
                }
            }
        });

        pause.setOnAction(a->{
            buttonCount.addAndGet(1);
            if(buttonCount.get()%2==1){
                timeline.stop();
                countTimer.stop();
                pause.setText("RESUME");
                rootGame.requestFocus();
            }else{
                timeline.play();
                countTimer.play();
                pause.setText("PAUSE");
                rootGame.requestFocus();
            }
        });

        returnMain.setOnAction(a-> {
            try {
                mainReturn.mainMenuScene(primaryStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return scene;
    }

    private boolean rowControl(int[][]grid, int row){
        for(int column=0;column<grid[0].length;column++){
            if(grid[row][column]==0){
                return false;
            }
        }
        return true;
    }

    private void clearRow(int[][]grid,Rectangle[][]grids, int row){
        for(int column=0;column<grid[0].length;column++){
            grid[row][column]=0;
            grids[row][column].setFill(Color.BLACK);
        }
    }

    private void shiftRowsDown(int[][] grid, Rectangle[][] grids, int clearedRow) {
        for (int row = clearedRow - 1; row >= 0; row--) {
            for (int column = 0; column < grid[0].length; column++) {
                grid[row + 1][column] = grid[row][column];
                grids[row + 1][column].setFill(grids[row][column].getFill());

                grid[row][column] = 0;
                grids[row][column].setFill(Color.BLACK);
            }
        }
    }

    public void scoreCount1(Text score,AtomicInteger count){
        switch (count.get()){
            case 1:
                num1 += 100;
                score.setText("Score: "+num1);
                return;
            case 2:
                num1 += 300;
                score.setText("Score: "+num1);
                return;
            case 3:
                num1 += 600;
                score.setText("Score: "+num1);
                return;
            case 4:
                num1 += 1200;
                score.setText("Score: "+num1);
                return;
            default:
        }
    }

    public void scoreCount2(Text score,AtomicInteger count){
        switch (count.get()){
            case 1:
                num2 += 100;
                score.setText("Score: "+num2);
                return;
            case 2:
                num2 += 300;
                score.setText("Score: "+num2);
                return;
            case 3:
                num2 += 600;
                score.setText("Score: "+num2);
                return;
            case 4:
                num2 += 1200;
                score.setText("Score: "+num2);
                return;
            default:
        }
    }


    abstract static class Tetromino{
        protected Rectangle[] blocks;
        protected int x,y;
        protected String type;

        public abstract void updatePosition(int cellSize);

        private boolean canMove(int dx, int dy, int[][] grid, int rows, int columns) {
            for (Rectangle block : blocks) {
                int column = (int) (block.getX() / 30);
                int row = (int) (block.getY() / 30);

                int newCol = column + dx;
                int newRow = row + dy;

                if (newCol < 0 || newCol >= columns || newRow < 0 || newRow >= rows) {
                    return false;
                }

                if (grid[newRow][newCol] != 0) {
                    return false;
                }
            }
            return true;
        }

        public boolean canMoveDown(int[][] grid, int rows, int cols) {
            return canMove(0, 1, grid, rows, cols);
        }


        public void moveLeft(int[][] grid, int rows, int columns, int cellSize) {
            if (canMove(-1, 0, grid, rows, columns)) {
                x--;
                updatePosition(cellSize);
            }
        }

        public void moveRight(int[][] grid, int rows, int columns, int cellSize) {
            if (canMove(1, 0, grid, rows, columns)) {
                x++;
                updatePosition(cellSize);
            }
        }

        public void moveDown(int[][] grid, int rows, int columns, int cellSize){
            if (canMove(0, 1, grid, rows, columns)) {
                y++;
                updatePosition(cellSize);
            }
        }

        public void lockToGrid(int[][] grid, Rectangle[][] grids){
            int rows = grid.length;
            int columns = grid[0].length;

            for(Rectangle block : blocks){
                int row = (int) (block.getY()/30);
                int column = (int) (block.getX()/30);

                if (row >= 0 && row < rows && column >= 0 && column < columns) {
                    grid[row][column] = 1;
                    grids[row][column].setFill(block.getFill());
                }

            }
        }

        public void removeFromGroup(Group gridGroup){
            for(Rectangle block : blocks){
                gridGroup.getChildren().remove(block);
            }
        }

        abstract boolean canRotate(int[][] grid, int rows, int columns);

        abstract void rotate(int[][] grid, int rows, int columns, int cellSize);

        public String getType(){
            return type;
        }

    }



    private Tetromino newPiece(Group gridGroup){
        int random =(int)(Math.random()*7);
        switch(random){
            case 0:
                return new OPiece(gridGroup, 30);
            case 1:
                return new IPiece(gridGroup, 30);
            case 2:
                return new TPiece(gridGroup, 30);
            case 3:
                return new SPiece(gridGroup,30);
            case 4:
                return new ZPiece(gridGroup,30);
            case 5:
                return new LPiece(gridGroup,30);
            case 6:
                return new JPiece(gridGroup,30);
            default:
                return new OPiece(gridGroup, 30);
        }

    }

    private Tetromino newNextPiece(Group gridGroup, String type){
        switch(type){
            case "O":
                return new OPiece(gridGroup, 30);
            case "I":
                return new IPiece(gridGroup, 30);
            case "T":
                return new TPiece(gridGroup, 30);
            case "S":
                return new SPiece(gridGroup,30);
            case "Z":
                return new ZPiece(gridGroup,30);
            case "L":
                return new LPiece(gridGroup,30);
            case "J":
                return new JPiece(gridGroup,30);
            default:
                return new OPiece(gridGroup, 30);
        }

    }

    static class OPiece extends Tetromino{
        private int rotation=0;
        public OPiece(Group gridGroup, int cellSize) {
            blocks = new Rectangle[4];
            x = 4;
            y = 0;
            this.type="O";

            for (int i = 0; i < 4; i++) {
                blocks[i] = new Rectangle(cellSize, cellSize);
                blocks[i].setFill(Color.GREEN);
                blocks[i].setStroke(Color.GRAY);
                gridGroup.getChildren().add(blocks[i]);
            }
            updatePosition(cellSize);
        }

        @Override

        boolean canRotate(int[][] grid, int rows, int columns){

            int nextRotation=(rotation +1)%4;
            int[][]offsets;

            switch (nextRotation){
                case 0-> offsets = new int[][]{
                        {0,0},
                        {1,0},
                        {0,1},
                        {1,1}
                };

                case 1 -> offsets = new int[][]{
                        {0,0},
                        {0,1},
                        {-1,0},
                        {-1,1}
                };

                case 2 -> offsets = new int[][]{
                        {0,0},
                        {-1,0},
                        {0,-1},
                        {-1,-1}
                };

                case 3 -> offsets = new int[][]{
                        {0,0},
                        {0,-1},
                        {1,0},
                        {1,-1}
                };

                default -> {
                    offsets = new int[2][2];
                }

            }
            for(int i=0;i<4;i++){
                int column = x+ offsets[i][0];
                int row = y+ offsets[i][1];

                if (column < 0 || column >= columns || row < 0 || row >= rows) {
                    return false;
                }

                if (grid[row][column] != 0) {
                    return false;
                }

                if (x + offsets[i][0] >= columns) return false;

            }
            return  true;
        }

        public void rotate(int[][] grid, int rows, int columns, int cellSize){
            if (canRotate(grid,20,10)){
                rotation = (rotation+1)%4;
                updatePosition(cellSize);
            }

        }

        public void updatePosition(int cellSize) {
            switch (rotation){
                case 0:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x+1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y+1) * cellSize);
                    blocks[3].setX((x+1) * cellSize);
                    blocks[3].setY((y+1) * cellSize);
                    return;
                case 1:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y+1) * cellSize);
                    blocks[2].setX((x-1) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x-1) * cellSize);
                    blocks[3].setY((y+1) * cellSize);
                    return;
                case 2:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x-1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y-1) * cellSize);
                    blocks[3].setX((x-1) * cellSize);
                    blocks[3].setY((y-1) * cellSize);
                    return;
                case 3:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y-1) * cellSize);
                    blocks[2].setX((x+1) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x+1) * cellSize);
                    blocks[3].setY((y-1) * cellSize);
                    return;
                default:
            }

        }

    }

    static class IPiece extends Tetromino{
        private int rotation=0;
        public IPiece(Group gridGroup, int cellSize){
            blocks = new Rectangle[4];
            x=4;
            y=0;
            this.type="I";

            for (int i = 0; i < 4; i++) {
                blocks[i] = new Rectangle(cellSize, cellSize);
                blocks[i].setFill(Color.CYAN);
                blocks[i].setStroke(Color.GRAY);
                gridGroup.getChildren().add(blocks[i]);
            }
            updatePosition(cellSize);
        }

        @Override

        boolean canRotate(int[][] grid, int rows, int columns){

            int nextRotation=(rotation +1)%4;
            int[][]offsets;

            switch (nextRotation){
                case 0-> offsets = new int[][]{
                        {0,0},
                        {0,1},
                        {0,2},
                        {0,3}
                };

                case 1 -> offsets = new int[][]{
                        {0,0},
                        {-1,0},
                        {-2,0},
                        {-3,0}
                };

                case 2 -> offsets = new int[][]{
                        {0,0},
                        {0,-1},
                        {0,-2},
                        {0,-3}
                };

                case 3 -> offsets = new int[][]{
                        {0,0},
                        {1,0},
                        {2,0},
                        {3,0}
                };

                default -> {
                    offsets = new int[2][2];
                }

            }
            for(int i=0;i<4;i++){
                int column = x+ offsets[i][0];
                int row = y+ offsets[i][1];

                if (column < 0 || column >= columns || row < 0 || row >= rows) {
                    return false;
                }

                if (grid[row][column] != 0) {
                    return false;
                }

                if (x + offsets[i][0] >= columns) return false;

            }
            return  true;
        }

        public void rotate(int[][] grid, int rows, int columns, int cellSize){
            if (canRotate(grid,20,10)){
                rotation = (rotation+1)%4;
                updatePosition(cellSize);
            }

        }

        public void updatePosition(int cellSize) {
            switch (rotation){
                case 0:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y+1) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y+2) * cellSize);
                    blocks[3].setX((x) * cellSize);
                    blocks[3].setY((y+3) * cellSize);
                    return;
                case 1:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x-1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x-2) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x-3) * cellSize);
                    blocks[3].setY((y) * cellSize);
                    return;
                case 2:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y-1) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y-2) * cellSize);
                    blocks[3].setX((x) * cellSize);
                    blocks[3].setY((y-3) * cellSize);
                    return;
                case 3:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x+1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x+2) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x+3) * cellSize);
                    blocks[3].setY((y) * cellSize);
                    return;
                default:
            }
        }

    }

    static class TPiece extends Tetromino{
        private int rotation=0;
        public TPiece(Group gridGroup, int cellSize){
            blocks = new Rectangle[4];
            x=4;
            y=0;
            this.type="T";

            for (int i = 0; i < 4; i++) {
                blocks[i] = new Rectangle(cellSize, cellSize);
                blocks[i].setFill(Color.PURPLE);
                blocks[i].setStroke(Color.GRAY);
                gridGroup.getChildren().add(blocks[i]);
            }
            updatePosition(cellSize);
        }

        @Override

        boolean canRotate(int[][] grid, int rows, int columns){

            int nextRotation=(rotation +1)%4;
            int[][]offsets;

            switch (nextRotation){
                case 0-> offsets = new int[][]{
                        {0,0},
                        {-1,0},
                        {1,0},
                        {0,1}
                };

                case 1 -> offsets = new int[][]{
                        {0,0},
                        {0,-1},
                        {0,1},
                        {-1,0}
                };

                case 2 -> offsets = new int[][]{
                        {0,0},
                        {1,0},
                        {-1,0},
                        {0,-1}
                };

                case 3 -> offsets = new int[][]{
                        {0,0},
                        {0,1},
                        {0,-1},
                        {1,0}
                };

                default -> {
                    offsets = new int[2][2];
                }

            }
            for(int i=0;i<4;i++){
                int column = x+ offsets[i][0];
                int row = y+ offsets[i][1];

                if (column < 0 || column >= columns || row < 0 || row >= rows) {
                    return false;
                }

                if (grid[row][column] != 0) {
                    return false;
                }

                if (x + offsets[i][0] >= columns)
                    return false;
            }
            return  true;
        }

        public void rotate(int[][] grid, int rows, int columns, int cellSize){
            if (canRotate(grid,grid.length,grid[0].length)){
                rotation = (rotation+1)%4;
                updatePosition(cellSize);
            }

        }

        public void updatePosition(int cellSize) {
            switch (rotation){
                case 0:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x-1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x+1) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x) * cellSize);
                    blocks[3].setY((y+1) * cellSize);
                    return;
                case 1:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y-1) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y+1) * cellSize);
                    blocks[3].setX((x-1) * cellSize);
                    blocks[3].setY((y) * cellSize);
                    return;
                case 2:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x+1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x-1) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x) * cellSize);
                    blocks[3].setY((y-1) * cellSize);
                    return;
                case 3:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y+1) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y-1) * cellSize);
                    blocks[3].setX((x+1) * cellSize);
                    blocks[3].setY((y) * cellSize);
                    return;
                default:
            }

        }

    }

    static class SPiece extends Tetromino{
        private int rotation=0;
        public SPiece(Group gridGroup, int cellSize){
            blocks = new Rectangle[4];
            x=4;
            y=0;
            this.type="S";

            for (int i = 0; i < 4; i++) {
                blocks[i] = new Rectangle(cellSize, cellSize);
                blocks[i].setFill(Color.RED);
                blocks[i].setStroke(Color.GRAY);
                gridGroup.getChildren().add(blocks[i]);
            }
            updatePosition(cellSize);
        }

        @Override

        boolean canRotate(int[][] grid, int rows, int columns){

            int nextRotation=(rotation +1)%4;
            int[][]offsets;

            switch (nextRotation){
                case 0-> offsets = new int[][]{
                        {0,0},
                        {1,0},
                        {0,1},
                        {-1,1}
                };

                case 1 -> offsets = new int[][]{
                        {0,0},
                        {0,1},
                        {-1,0},
                        {-1,-1}
                };

                case 2 -> offsets = new int[][]{
                        {0,0},
                        {-1,0},
                        {0,-1},
                        {1,-1}
                };

                case 3 -> offsets = new int[][]{
                        {0,0},
                        {0,-1},
                        {1,0},
                        {1,1}
                };

                default -> {
                    offsets = new int[2][2];
                }

            }
            for(int i=0;i<4;i++){
                int column = x+ offsets[i][0];
                int row = y+ offsets[i][1];

                if (column < 0 || column >= columns || row < 0 || row >= rows) {
                    return false;
                }

                if (grid[row][column] != 0) {
                    return false;
                }

                if (x + offsets[i][0] >= columns)
                    return false;

            }
            return  true;
        }

        public void rotate(int[][] grid, int rows, int columns, int cellSize){
            if (canRotate(grid,20,10)){
                rotation = (rotation+1)%4;
                updatePosition(cellSize);
            }

        }

        public void updatePosition(int cellSize) {
            switch (rotation){
                case 0:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x+1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y+1) * cellSize);
                    blocks[3].setX((x-1) * cellSize);
                    blocks[3].setY((y+1) * cellSize);
                    return;
                case 1:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y+1) * cellSize);
                    blocks[2].setX((x-1) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x-1) * cellSize);
                    blocks[3].setY((y-1) * cellSize);
                    return;
                case 2:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x-1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y-1) * cellSize);
                    blocks[3].setX((x+1) * cellSize);
                    blocks[3].setY((y-1) * cellSize);
                    return;
                case 3:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y-1) * cellSize);
                    blocks[2].setX((x+1) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x+1) * cellSize);
                    blocks[3].setY((y+1) * cellSize);
                    return;
                default:
            }

        }

    }

    static class ZPiece extends Tetromino{
        private int rotation=0;
        public ZPiece(Group gridGroup, int cellSize){
            blocks = new Rectangle[4];
            x=4;
            y=0;
            this.type="Z";

            for (int i = 0; i < 4; i++) {
                blocks[i] = new Rectangle(cellSize, cellSize);
                blocks[i].setFill(Color.YELLOW);
                blocks[i].setStroke(Color.GRAY);
                gridGroup.getChildren().add(blocks[i]);
            }
            updatePosition(cellSize);
        }

        @Override

        boolean canRotate(int[][] grid, int rows, int columns){

            int nextRotation=(rotation +1)%4;
            int[][]offsets;

            switch (nextRotation){
                case 0-> offsets = new int[][]{
                        {0,0},
                        {-1,0},
                        {0,1},
                        {1,1}
                };

                case 1 -> offsets = new int[][]{
                        {0,0},
                        {0,-1},
                        {-1,0},
                        {-1,1}
                };

                case 2 -> offsets = new int[][]{
                        {0,0},
                        {1,0},
                        {0,-1},
                        {-1,-1}
                };

                case 3 -> offsets = new int[][]{
                        {0,0},
                        {0,1},
                        {1,0},
                        {1,-1}
                };

                default -> {
                    offsets = new int[2][2];
                }

            }
            for(int i=0;i<4;i++){
                int column = x+ offsets[i][0];
                int row = y+ offsets[i][1];

                if (column < 0 || column >= columns || row < 0 || row >= rows) {
                    return false;
                }

                if (grid[row][column] != 0) {
                    return false;
                }

                if (x + offsets[i][0] >= columns)
                    return false;
            }
            return  true;
        }

        public void rotate(int[][] grid, int rows, int columns, int cellSize){
            if (canRotate(grid,20,10)){
                rotation = (rotation+1)%4;
                updatePosition(cellSize);
            }

        }

        public void updatePosition(int cellSize) {
            switch (rotation){
                case 0:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x-1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y+1) * cellSize);
                    blocks[3].setX((x+1) * cellSize);
                    blocks[3].setY((y+1) * cellSize);
                    return;
                case 1:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y-1) * cellSize);
                    blocks[2].setX((x-1) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x-1) * cellSize);
                    blocks[3].setY((y+1) * cellSize);
                    return;
                case 2:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x+1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y-1) * cellSize);
                    blocks[3].setX((x-1) * cellSize);
                    blocks[3].setY((y-1) * cellSize);
                    return;
                case 3:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y+1) * cellSize);
                    blocks[2].setX((x+1) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x+1) * cellSize);
                    blocks[3].setY((y-1) * cellSize);
                    return;
                default:
            }
        }



    }

    static class LPiece extends Tetromino{
        private int rotation=0;
        public LPiece(Group gridGroup, int cellSize){
            blocks = new Rectangle[4];
            x=4;
            y=0;
            this.type="L";

            for (int i = 0; i < 4; i++) {
                blocks[i] = new Rectangle(cellSize, cellSize);
                blocks[i].setFill(Color.BLUE);
                blocks[i].setStroke(Color.GRAY);
                gridGroup.getChildren().add(blocks[i]);
            }
            updatePosition(cellSize);
        }

        @Override

        boolean canRotate(int[][] grid, int rows, int columns){

            int nextRotation=(rotation +1)%4;
            int[][]offsets;

            switch (nextRotation){
                case 0-> offsets = new int[][]{
                        {0,0},
                        {0,1},
                        {0,2},
                        {1,2}
                };

                case 1 -> offsets = new int[][]{
                        {0,0},
                        {-1,0},
                        {-2,0},
                        {-2,1}
                };

                case 2 -> offsets = new int[][]{
                        {0,0},
                        {0,-1},
                        {0,-2},
                        {-1,-2}
                };

                case 3 -> offsets = new int[][]{
                        {0,0},
                        {1,0},
                        {2,0},
                        {2,-1}
                };

                default -> {
                    offsets = new int[2][2];
                }

            }
            for(int i=0;i<4;i++){
                int column = x+ offsets[i][0];
                int row = y+ offsets[i][1];

                if (column < 0 || column >= columns || row < 0 || row >= rows) {
                    return false;
                }

                if (grid[row][column] != 0) {
                    return false;
                }

                if (x + offsets[i][0] >= columns)
                    return false;
            }
            return  true;
        }

        public void rotate(int[][] grid, int rows, int columns, int cellSize){
            if (canRotate(grid,20,10)){
                rotation = (rotation+1)%4;
                updatePosition(cellSize);
            }

        }

        public void updatePosition(int cellSize) {
            switch (rotation){
                case 0:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y+1) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y+2) * cellSize);
                    blocks[3].setX((x+1) * cellSize);
                    blocks[3].setY((y+2) * cellSize);
                    return;
                case 1:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x-1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x-2) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x-2) * cellSize);
                    blocks[3].setY((y+1) * cellSize);
                    return;
                case 2:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y-1) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y-2) * cellSize);
                    blocks[3].setX((x-1) * cellSize);
                    blocks[3].setY((y-2) * cellSize);
                    return;
                case 3:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x+1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x+2) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x+2) * cellSize);
                    blocks[3].setY((y-1) * cellSize);
                    return;
                default:
            }
        }


    }

    static class JPiece extends Tetromino{
        private int rotation=0;
        public JPiece(Group gridGroup, int cellSize){
            blocks = new Rectangle[4];
            x=4;
            y=0;
            this.type="J";

            for (int i = 0; i < 4; i++) {
                blocks[i] = new Rectangle(cellSize, cellSize);
                blocks[i].setFill(Color.ORANGE);
                blocks[i].setStroke(Color.GRAY);
                gridGroup.getChildren().add(blocks[i]);
            }
            updatePosition(cellSize);
        }

        @Override

        boolean canRotate(int[][] grid, int rows, int columns){

            int nextRotation=(rotation +1)%4;
            int[][]offsets;

            switch (nextRotation){
                case 0-> offsets = new int[][]{
                        {0,0},
                        {0,1},
                        {0,2},
                        {-1,2}
                };

                case 1 -> offsets = new int[][]{
                        {0,0},
                        {-1,0},
                        {-2,0},
                        {-2,-1}
                };

                case 2 -> offsets = new int[][]{
                        {0,0},
                        {1,-1},
                        {0,-2},
                        {1,-2}
                };

                case 3 -> offsets = new int[][]{
                        {0,0},
                        {1,0},
                        {2,0},
                        {2,1}
                };

                default -> {
                    offsets = new int[2][2];
                }

            }
            for(int i=0;i<4;i++){
                int column = x+ offsets[i][0];
                int row = y+ offsets[i][1];

                if (column < 0 || column >= columns || row < 0 || row >= rows) {
                    return false;
                }

                if (grid[row][column] != 0) {
                    return false;
                }

                if (x + offsets[i][0] >= columns)
                    return false;
            }
            return  true;
        }

        public void rotate(int[][] grid, int rows, int columns, int cellSize){
            if (canRotate(grid,20,10)){
                rotation = (rotation+1)%4;
                updatePosition(cellSize);
            }

        }

        public void updatePosition(int cellSize) {
            switch (rotation){
                case 0:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y+1) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y+2) * cellSize);
                    blocks[3].setX((x-1) * cellSize);
                    blocks[3].setY((y+2) * cellSize);
                    return;
                case 1:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x-1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x-2) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x-2) * cellSize);
                    blocks[3].setY((y-1) * cellSize);
                    return;
                case 2:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x) * cellSize);
                    blocks[1].setY((y-1) * cellSize);
                    blocks[2].setX((x) * cellSize);
                    blocks[2].setY((y-2) * cellSize);
                    blocks[3].setX((x+1) * cellSize);
                    blocks[3].setY((y-2) * cellSize);
                    return;
                case 3:
                    blocks[0].setX((x) * cellSize);
                    blocks[0].setY((y) * cellSize);
                    blocks[1].setX((x+1) * cellSize);
                    blocks[1].setY((y) * cellSize);
                    blocks[2].setX((x+2) * cellSize);
                    blocks[2].setY((y) * cellSize);
                    blocks[3].setX((x+2) * cellSize);
                    blocks[3].setY((y+1) * cellSize);
                    return;
                default:
            }
        }

    }

}