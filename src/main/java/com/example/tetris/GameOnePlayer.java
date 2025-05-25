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


public class GameOnePlayer {
    private int num=0;
    public Scene singleplayerScene(Stage primaryStage){

        final int rows= 20;
        final int columns= 10;

        int [][] grid= new int[rows][columns];
        Rectangle[][] grids=new Rectangle[rows][columns];

        final int cellSize= 30;

        Group gridGroup =new Group();
        Rectangle pieceHolder=new Rectangle(200,200);
        Rectangle pointHolder=new Rectangle(200,100);
        Rectangle timeHolder=new Rectangle(200,100);

        for (int row = 0; row < 20; row++) {
            for (int column = 0; column < 10; column++) {
                Rectangle cell = new Rectangle(cellSize, cellSize);
                cell.setFill(Color.BLACK);
                cell.setStroke(Color.GRAY);
                cell.setX(column * cellSize);
                cell.setY(row * cellSize);
                gridGroup.getChildren().add(cell);
                grids[row][column]= cell;
                grid[row][column]=0;

            }
        }

        gridGroup.setLayoutX(610);
        gridGroup.setLayoutY(100);

        pieceHolder.setFill(Color.GRAY);
        pieceHolder.setStroke(Color.BLACK);
        pieceHolder.setLayoutX(205);
        pieceHolder.setLayoutY(100);

        pointHolder.setFill(Color.GRAY);
        pointHolder.setStroke(Color.BLACK);
        pointHolder.setLayoutX(205);
        pointHolder.setLayoutY(350);

        timeHolder.setFill(Color.GRAY);
        timeHolder.setStroke(Color.BLACK);
        timeHolder.setLayoutX(205);
        timeHolder.setLayoutY(500);

        Pane stacker= new Pane();
        Tetromino[] nextPiece= new Tetromino[1];
        nextPiece[0]= newPiece(gridGroup);
        Group nextGroup= new Group();
        for (Rectangle block: nextPiece[0].blocks){
            nextGroup.getChildren().add(block);
        }
        nextGroup.setLayoutX(165);
        nextGroup.setLayoutY(150);

        Text score= new Text("Score: "+num);
        AtomicInteger count= new AtomicInteger();

        score.setLayoutX(210);
        score.setLayoutY(410);
        score.setFill(Color.BLACK);
        score.setFont(Font.font(24));

        AtomicInteger time= new AtomicInteger();
        Text timer= new Text("Time: "+time);

        timer.setLayoutX(210);
        timer.setLayoutY(560);
        timer.setFill(Color.BLACK);
        timer.setFont(Font.font(24));

        stacker.getChildren().clear();
        stacker.getChildren().addAll(pieceHolder, nextGroup, pointHolder,score,timeHolder,timer);

        Tetromino[] currentPiece = new Tetromino[1];
        currentPiece[0]= newPiece(gridGroup);

        Tetris mainReturn=new Tetris();
        Pane whenOver=new Pane();

        Timeline timeline = new Timeline();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.5), e -> {
            if (currentPiece[0].canMoveDown(grid,20,10)){
            currentPiece[0].moveDown(grid,20,10,30);
            }else{
                currentPiece[0].lockToGrid(grid, grids);

                for(int row=0; row<grid.length;row++){
                    if(rowControl(grid,row)){
                        clearRow(grid,grids,row);
                        shiftRowsDown(grid,grids,row);
                        count.getAndIncrement();
                        row--;
                    }
                }
                scoreCount(score, count);
                count.set(0);

                currentPiece[0].removeFromGroup(gridGroup);
                currentPiece[0] = newNextPiece(gridGroup,nextPiece[0].getType());
                nextPiece[0]=newPiece(gridGroup);

                if (grid[0][4]==1||grid[1][4]==1){
                    timeline.stop();
                    VBox overHolder = new VBox();
                    overHolder.setAlignment(Pos.CENTER);
                    Text gameOver= new Text("GAME OVER");
                    gameOver.setFont(Font.font(150));
                    gameOver.setFill(Color.DARKRED);
                    LocalScores scoreSave=new LocalScores();
                    String username= SessionProfile.username;
                    scoreSave.localSaving(username,num,time);
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
                    overHolder.setLayoutY(300);
                    whenOver.getChildren().add(overHolder);
                    whenOver.toFront();
                }

                nextGroup.getChildren().clear();
                for (Rectangle block: nextPiece[0].blocks){
                    nextGroup.getChildren().add(block);
                }

                stacker.getChildren().clear();
                stacker.getChildren().addAll(pieceHolder, nextGroup, pointHolder,score,timeHolder,timer);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Timeline countTimer=new Timeline();
        countTimer.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e->{
            time.addAndGet(1);
            timer.setText("Time: "+time+"s");

            if (grid[0][4]==1||grid[1][4]==1){
                countTimer.stop();
            }
        }));
        countTimer.setCycleCount(Animation.INDEFINITE);
        countTimer.play();

        Button pause= new Button("PAUSE");
        pause.setLayoutX(950);
        pause.setLayoutY(100);
        AtomicInteger buttonCount = new AtomicInteger();

        Button returnMain= new Button("RETURN TO MAIN MENU");
        returnMain.setLayoutX(950);
        returnMain.setLayoutY(150);

        Pane rootGame = new Pane();
        rootGame.getChildren().addAll(gridGroup,stacker,pause,returnMain,whenOver);
        rootGame.requestFocus();

        Scene scene= new Scene(rootGame,1520,800);

        rootGame.requestFocus();

        scene.setOnKeyPressed(e->{
            switch (e.getCode()){
                case LEFT -> currentPiece[0].moveLeft(grid,20,10,30);
                case RIGHT -> currentPiece[0].moveRight(grid,20,10,30);
                case UP -> {
                    if (currentPiece[0].canRotate(grid,20,10)){
                        currentPiece[0].rotate(grid,20,10,30);
                    }
                }
                case DOWN -> {
                    if (currentPiece[0].canMoveDown(grid,20,10)){
                        currentPiece[0].moveDown(grid,20,10,30);
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

    public void scoreCount(Text score,AtomicInteger count){
        switch (count.get()){
            case 1:
                num += 100;
                score.setText("Score: "+num);
                return;
            case 2:
                num += 300;
                score.setText("Score: "+num);
                return;
            case 3:
                num += 600;
                score.setText("Score: "+num);
                return;
            case 4:
                num += 1200;
                score.setText("Score: "+num);
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