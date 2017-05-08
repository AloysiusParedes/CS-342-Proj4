package TilesPuzzle;

/**
 * TilesController.java
 * Holds all the the references to the GUI's objects (buttons, lables, etc.)
 * Contains functions to handle events for each button press
 * Contains functions for making calculations, modifying tiles, etc.
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class TilesController implements Initializable{
    @FXML
    private Button topLeftButton;
    @FXML
    private Button topMiddleButton;
    @FXML
    private Button topRightButton;
    @FXML
    private Button middleLeftButton;
    @FXML
    private Button middleMiddleButton;
    @FXML
    private Button middleRightButton;
    @FXML
    private Button bottomLeftButton;
    @FXML
    private Button bottomMiddleButton;
    @FXML
    private Button bottomRightButton;
    @FXML
    private Label instructionsLabel;
    @FXML
    private Button solveBoardButton;
    @FXML
    private Label heuristicLabel;
    @FXML
    private Label moveNumberLabel;

    private Button[][] buttonArray;
    private int[][] finalBoard = new int[3][3];
    private int i = 0, j = 0, counter = 0, moveNumber = 0, heuristicValue = 0, solutionIndex = 0;
    private boolean boardReady = false;
    private List<String> solution = new ArrayList<>();

    //override the initialize function, and set the text of all the buttons as random ints from 1-8
    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.println("Board Randomized...");
        initializeFinalBoard();
        initializeButtonArray();

        List<Integer> randomList = new ArrayList<>();
        for(int i = 0; i < 9; i++)
            randomList.add(i);

        Collections.shuffle(randomList);

        //set buttons' text as random values from the random list
        int index = 0;
        for(i = 0; i < 3; i++){
            for(j = 0; j < 3; j++){
                //if it's '0', then place a space on the button
                if(randomList.get(index).toString().equals("0"))
                    buttonArray[i][j].setText(" ");

                else
                    buttonArray[i][j].setText(randomList.get(index).toString());
                index++;
            }
        }

        System.out.println("Board Ready...");
        boardReady = true;
        updateGameLabels();
        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");

        }
    }//end initialize(...)

    //function for set board to let the user enter a custom board
    public void setBoardButtonAction(ActionEvent event){
        System.out.println("Setting Board...");

        moveNumber = 0;
        enableAll();

        //clear all the buttons
        clearButtonTexts();

        List<Integer> buttonsList = new ArrayList<>();
        for(i = 0; i < 9; i++)
            buttonsList.add(i);

        boardReady = false;
        counter = 0;

        return;
    }//end setBoardButtonAction(...)

    //function to clear the texts of all the buttons
    private void clearButtonTexts(){
        for(i = 0; i < 3; i++){
            for(j = 0; j < 3; j++){
                buttonArray[i][j].setText(" ");
            }
        }
        return;
    }//end clearButtonTexts()

    //function to solve the puzzle
    public void solveButtonAction(ActionEvent event){
        System.out.println("Solving Board...");
        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");
        }
        else {
            //System.out.println("INCOMPLETE: TO BE IMPLEMENTED");
            initializeButtonArray();
            String chars = new String();
            //put the button's texts into a char arr
            for(i = 0; i < 3; i++){
                for(j = 0; j < 3; j++){
                    if(buttonArray[i][j].getText().equals(" "))
                        chars += "0";

                    else
                        chars += buttonArray[i][j].getText();
                }
            }
            //System.out.println("Current board: " + chars);
            Board parentBoard = new Board(chars.toCharArray());
            //parentBoard.printBoard(0);

            SearchTree stateSpaceSearch = new SearchTree(parentBoard);

            solution = stateSpaceSearch.algorithm();

            if(solution.size() > 1) {
                Timeline solver = new Timeline(new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        solutionIndex++;
                        if(solutionIndex != solution.size())
                            stringToButtons(solution.get(solutionIndex));
                        updateGameLabels();
                    }
                }));
                solver.setCycleCount(solution.size());
                solver.play();
                if(checkFinished()){
                    disableAll();
                    instructionsLabel.setText("Puzzle Complete\n\n" +
                            "Click \"Set Board\" to create a new custom board\n\n" +
                            "Click \"Exit\" to close the program");
                }
            }
            else {
                stringToButtons(solution.get(solution.size() - 1));
                instructionsLabel.setText("Puzzle Unsolvable: Best Solution Displayed\n\n" +
                        "Click \"Set Board\" to create a new custom board\n\n" +
                        "Click \"Exit\" to close the program");
                updateGameLabels();
                disableAll();
            }

            if(checkFinished()){
                disableAll();
                instructionsLabel.setText("Puzzle Complete\n\n" +
                        "Click \"Set Board\" to create a new custom board\n\n" +
                        "Click \"Exit\" to close the program");
            }
        }

        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");
        }

        return;
    }//end solveButtonAction(...)

    //converts string to text of buttons
    public void stringToButtons(String str){
        char[] chars = str.toCharArray();
        for(i = 0; i < str.length(); i++){
            if(chars[i] == '0')
                chars[i] = ' ';
        }

        clearButtonTexts();
        topLeftButton.setText(Character.toString(chars[0]));
        topMiddleButton.setText(Character.toString(chars[1]));
        topRightButton.setText(Character.toString(chars[2]));
        middleLeftButton.setText(Character.toString(chars[3]));
        middleMiddleButton.setText(Character.toString(chars[4]));
        middleRightButton.setText(Character.toString(chars[5]));
        bottomLeftButton.setText(Character.toString(chars[6]));
        bottomMiddleButton.setText(Character.toString(chars[7]));
        bottomRightButton.setText(Character.toString(chars[8]));
    }//end stringToButtons(...)

    //function to initialize array of buttons from the form
    private void initializeButtonArray(){
        buttonArray = new Button[3][3];
        buttonArray[0][0] = topLeftButton;
        buttonArray[0][1] = topMiddleButton;
        buttonArray[0][2] = topRightButton;
        buttonArray[1][0] = middleLeftButton;
        buttonArray[1][1] = middleMiddleButton;
        buttonArray[1][2] = middleRightButton;
        buttonArray[2][0] = bottomLeftButton;
        buttonArray[2][1] = bottomMiddleButton;
        buttonArray[2][2] = bottomRightButton;
    }//end initializeButtonArray()

    //function for exit button to close the program
    public void exitButtonAction(ActionEvent event) {
        System.out.println("Exiting Program...");
        System.exit(1);
    }//end exitButtonAction(...)

    //event handler for the top left button
    public void topLeftPressed(ActionEvent event){
        if(boardReady == false && topLeftButton.getText().equals(" ")){
            String s = new String();
            if(counter == 0)
                s = " ";
            else
                s = Integer.toString(counter);
            topLeftButton.setText(s);
            counter++;
        }

        if(counter == 9){
            System.out.println("Board Ready...");
            boardReady = true;
            counter = 0;
        }

        if(boardReady == true){
            String temp = topLeftButton.getText();
            //check right
            if(topMiddleButton.getText().equals(" ")){
                topLeftButton.setText(topMiddleButton.getText());
                topMiddleButton.setText(temp);
                updateGameLabels();
            }
            //check below
            else if(middleLeftButton.getText().equals(" ")){
                topLeftButton.setText(middleLeftButton.getText());
                middleLeftButton.setText(temp);
                updateGameLabels();
            }
            else{
                System.out.println("INVALID MOVE...");
            }
            initializeButtonArray();
        }

        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");
        }

        return;
    }//end topLeftPressed(...)

    //event handler for top middle button
    public void topMiddlePressed(ActionEvent event){
        if(boardReady == false && topMiddleButton.getText().equals(" ")){
            String s = new String();
            if(counter == 0)
                s = " ";
            else
                s = Integer.toString(counter);
            topMiddleButton.setText(s);
            counter++;
        }

        if(counter == 9){
            System.out.println("Board Ready...");
            boardReady = true;
        }

        if(boardReady == true){


            String temp = topMiddleButton.getText();
            //check right
            if(topRightButton.getText().equals(" ")){
                topMiddleButton.setText(topRightButton.getText());
                topRightButton.setText(temp);
                updateGameLabels();
            }
            //check below
            else if(middleMiddleButton.getText().equals(" ")){
                topMiddleButton.setText(middleMiddleButton.getText());
                middleMiddleButton.setText(temp);
                updateGameLabels();
            }
            //check left
            else if(topLeftButton.getText().equals(" ")){
                topMiddleButton.setText(topLeftButton.getText());
                topLeftButton.setText(temp);
                updateGameLabels();
            }
            else{
                System.out.println("INVALID MOVE...");
            }
        }

        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");

        }
        return;
    }//end topMiddlePressed(...)

    //event handler for the top right button
    public void topRightPressed(ActionEvent event){
        if(boardReady == false && topRightButton.getText().equals(" ")){
            String s = new String();
            if(counter == 0)
                s = " ";
            else
                s = Integer.toString(counter);
            topRightButton.setText(s);
            counter++;
        }

        if(counter == 9){
            System.out.println("Board Ready...");
            boardReady = true;
        }

        if(boardReady == true){
            String temp = topRightButton.getText();
            //check left
            if(topMiddleButton.getText().equals(" ")){
                topRightButton.setText(topMiddleButton.getText());
                topMiddleButton.setText(temp);
                updateGameLabels();
            }
            //check below
            else if(middleRightButton.getText().equals(" ")){
                topRightButton.setText(middleRightButton.getText());
                middleRightButton.setText(temp);
                updateGameLabels();
            }
            else{
                System.out.println("INVALID MOVE...");
            }
        }
        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");
        }

        return;
    }//end topRightPressed(...)

    //event handler for the middle left button
    public void middleLeftPressed(ActionEvent event){
        if(boardReady == false && middleLeftButton.getText().equals(" ")){
            String s = new String();
            if(counter == 0)
                s = " ";
            else
                s = Integer.toString(counter);
            middleLeftButton.setText(s);
            counter++;
        }

        if(counter == 9){
            System.out.println("Board Ready...");
            boardReady = true;
        }

        if(boardReady == true){
            String temp = middleLeftButton.getText();
            //check right
            if(middleMiddleButton.getText().equals(" ")){
                middleLeftButton.setText(middleMiddleButton.getText());
                middleMiddleButton.setText(temp);
                updateGameLabels();
            }
            //check above
            else if(topLeftButton.getText().equals(" ")){
                middleLeftButton.setText(topLeftButton.getText());
                topLeftButton.setText(temp);
                updateGameLabels();
            }
            //check below
            else if(bottomLeftButton.getText().equals(" ")){
                middleLeftButton.setText(bottomLeftButton.getText());
                bottomLeftButton.setText(temp);
                updateGameLabels();
            }
            else{
                System.out.println("INVALID MOVE...");
            }
        }

        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");

        }

        return;
    }//end middleLeftPressed(...)

    //event handler for the middle middle button
    public void middleMiddlePressed(ActionEvent event){
        if(boardReady == false && middleMiddleButton.getText().equals(" ")){
            String s = new String();
            if(counter == 0)
                s = " ";
            else
                s = Integer.toString(counter);
            middleMiddleButton.setText(s);
            counter++;
        }

        if(counter == 9){
            System.out.println("Board Ready...");
            boardReady = true;
        }

        if(boardReady == true){
            String temp = middleMiddleButton.getText();
            //check right
            if(middleRightButton.getText().equals(" ")){
                middleMiddleButton.setText(middleRightButton.getText());
                middleRightButton.setText(temp);
                updateGameLabels();
            }
            //check below
            else if(bottomMiddleButton.getText().equals(" ")){
                middleMiddleButton.setText(bottomMiddleButton.getText());
                bottomMiddleButton.setText(temp);
                updateGameLabels();
            }
            //check above
            else if(topMiddleButton.getText().equals(" ")){
                middleMiddleButton.setText(topMiddleButton.getText());
                topMiddleButton.setText(temp);
                updateGameLabels();
            }
            //check left
            else if(middleLeftButton.getText().equals(" ")){
                middleMiddleButton.setText(middleLeftButton.getText());
                middleLeftButton.setText(temp);
                updateGameLabels();
            }
            else{
                System.out.println("INVALID MOVE...");
            }
        }

        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");

        }

        return;
    }//end middleMiddlePressed(...)

    //event handler for the middle right button
    public void middleRightPressed(ActionEvent event){
        if(boardReady == false && middleRightButton.getText().equals(" ")){
            String s = new String();
            if(counter == 0)
                s = " ";
            else
                s = Integer.toString(counter);
            middleRightButton.setText(s);
            counter++;
        }

        if(counter == 9){
            System.out.println("Board Ready...");
            boardReady = true;
        }

        if(boardReady == true){


            String temp = middleRightButton.getText();
            //check above
            if(topRightButton.getText().equals(" ")){
                middleRightButton.setText(topRightButton.getText());
                topRightButton.setText(temp);
                updateGameLabels();
            }
            //check below
            else if(bottomRightButton.getText().equals(" ")){
                middleRightButton.setText(bottomRightButton.getText());
                bottomRightButton.setText(temp);
                updateGameLabels();
            }
            //check left
            else if(middleMiddleButton.getText().equals(" ")){
                middleRightButton.setText(middleMiddleButton.getText());
                middleMiddleButton.setText(temp);
                updateGameLabels();
            }
            else{
                System.out.println("INVALID MOVE...");
            }
        }

        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");

        }

        return;
    }//end middleRightPressed(...)

    //event handler for bottom left button
    public void bottomLeftPressed(ActionEvent event){
        if(boardReady == false && bottomLeftButton.getText().equals(" ")){
            String s = new String();
            if(counter == 0)
                s = " ";
            else
                s = Integer.toString(counter);
            bottomLeftButton.setText(s);
            counter++;
        }

        if(counter == 9){
            System.out.println("Board Ready...");
            boardReady = true;
        }

        if(boardReady == true){
            String temp = bottomLeftButton.getText();
            //check right
            if(bottomMiddleButton.getText().equals(" ")){
                bottomLeftButton.setText(bottomMiddleButton.getText());
                bottomMiddleButton.setText(temp);
                updateGameLabels();
            }
            //check above
            else if(middleLeftButton.getText().equals(" ")){
                bottomLeftButton.setText(middleLeftButton.getText());
                middleLeftButton.setText(temp);
                updateGameLabels();
            }
            else{
                System.out.println("INVALID MOVE...");
            }
        }

        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");

        }

        return;
    }//end bottomLeftPressed(...)

    //event handler for bottom middle button
    public void bottomMiddlePressed(ActionEvent event){
        if(boardReady == false && bottomMiddleButton.getText().equals(" ")){
            String s = new String();
            if(counter == 0)
                s = " ";
            else
                s = Integer.toString(counter);
            bottomMiddleButton.setText(s);
            counter++;
        }

        if(counter == 9){
            System.out.println("Board Ready...");
            boardReady = true;
        }

        if(boardReady == true){
            String temp = bottomMiddleButton.getText();
            //check right
            if(bottomRightButton.getText().equals(" ")){
                bottomMiddleButton.setText(bottomRightButton.getText());
                bottomRightButton.setText(temp);
                updateGameLabels();
            }
            //check above
            else if(middleMiddleButton.getText().equals(" ")){
                bottomMiddleButton.setText(middleMiddleButton.getText());
                middleMiddleButton.setText(temp);
                updateGameLabels();
            }
            //check left
            else if(bottomLeftButton.getText().equals(" ")){
                bottomMiddleButton.setText(bottomLeftButton.getText());
                bottomLeftButton.setText(temp);
                updateGameLabels();
            }
            else{
                System.out.println("INVALID MOVE...");
            }
        }
        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");

        }

        return;
    }//end bottomMiddlePressed(...)

    //event handler for bottom right button
    public void bottomRightPressed(ActionEvent event){
        if(boardReady == false && bottomRightButton.getText().equals(" ")){
            String s = new String();
            if(counter == 0)
                s = " ";
            else
                s = Integer.toString(counter);
            bottomRightButton.setText(s);
            counter++;
        }

        if(counter == 9){
            System.out.println("Board Ready...");
            boardReady = true;
        }

        if(boardReady == true){
            String temp = bottomRightButton.getText();
            //check left
            if(bottomMiddleButton.getText().equals(" ")){
                bottomRightButton.setText(bottomMiddleButton.getText());
                bottomMiddleButton.setText(temp);
                updateGameLabels();
            }
            //check below
            else if(middleRightButton.getText().equals(" ")){
                bottomRightButton.setText(middleRightButton.getText());
                middleRightButton.setText(temp);
                updateGameLabels();
            }
            else{
                System.out.println("INVALID MOVE...");
            }
        }
        if(checkFinished()){
            disableAll();
            instructionsLabel.setText("Puzzle Complete\n\n" +
                    "Click \"Set Board\" to create a new custom board\n\n" +
                    "Click \"Exit\" to close the program");

        }

        return;
    }//end bottomRightPressed(...)

    //checks all the buttons to see if the game is finished
    private boolean checkFinished(){
        if(!topLeftButton.getText().equals("1"))
            return false;
        if(!topMiddleButton.getText().equals("2"))
            return false;
        if(!topRightButton.getText().equals("3"))
            return false;
        if(!middleLeftButton.getText().equals("4"))
            return false;
        if(!middleMiddleButton.getText().equals("5"))
            return false;
        if(!middleRightButton.getText().equals("6"))
            return false;
        if(!bottomLeftButton.getText().equals("7"))
            return false;
        if(!bottomMiddleButton.getText().equals("8"))
            return false;
        if(!bottomRightButton.getText().equals(" "))
            return false;

        System.out.println("Board Finished...");
        return true;
    }//end checkFinished()

    //disables all the buttons except for exit and set board
    private void disableAll(){
        topLeftButton.setDisable(true);
        topMiddleButton.setDisable(true);
        topRightButton.setDisable(true);
        middleLeftButton.setDisable(true);
        middleMiddleButton.setDisable(true);
        middleRightButton.setDisable(true);
        bottomLeftButton.setDisable(true);
        bottomMiddleButton.setDisable(true);
        bottomRightButton.setDisable(true);
        solveBoardButton.setDisable(true);
    }//end disableAll()

    //enables all the buttons that were disabled
    private void enableAll(){
        topLeftButton.setDisable(false);
        topMiddleButton.setDisable(false);
        topRightButton.setDisable(false);
        middleLeftButton.setDisable(false);
        middleMiddleButton.setDisable(false);
        middleRightButton.setDisable(false);
        bottomLeftButton.setDisable(false);
        bottomMiddleButton.setDisable(false);
        bottomRightButton.setDisable(false);
        solveBoardButton.setDisable(false);
    }//end enableAll()

    //helper function for calculateHeuristic to calculate the distance for a given piece to it's final destination
    private int calculateDistance(int piece, int pieceX, int pieceY){
        int xDistance, yDistance, totalDistance;
        int xFinal = 0, yFinal = 0;

        int foundFinal = 0;

        //find the final position for that piece
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(finalBoard[i][j] == piece){
                    xFinal = i;
                    yFinal = j;
                    foundFinal = 1;
                }

                if(foundFinal == 1)
                    break;
            }

            if(foundFinal == 1)
                break;
        }

        xDistance = Math.abs(pieceX - xFinal);
        yDistance = Math.abs(pieceY - yFinal);
        totalDistance = xDistance + yDistance;

        return totalDistance;
    }//end calculateDistance(...)

    //calculates the heuristic for the current board
    private int calculateHeuristic(){
        int heuristic = 0;

        //for each piece
        for(int currentPiece = 0; currentPiece < 9; currentPiece++) {
            //find x position
            for (int xPos = 0; xPos < 3; xPos++) {
                //find y position
                for (int yPos = 0; yPos < 3; yPos++) {
                    if(buttonArray[xPos][yPos].getText().equals(" ")){
                        if(currentPiece == 0)
                            heuristic += calculateDistance(currentPiece, xPos, yPos);
                    }
                    //calculate the distance for that piece after the position has been found
                    else if(currentPiece == Integer.parseInt(buttonArray[xPos][yPos].getText())) {
                        heuristic += calculateDistance(currentPiece, xPos, yPos);
                    }
                    //otherwise keep searching for the currentPiece's position
                }
            }
        }

        return heuristic;
    }//end calculateHeuristic

    //updates the move number and heuristic value labels
    private void updateGameLabels(){
        moveNumber++;
        moveNumberLabel.setText("Move Number: " + moveNumber);
        heuristicValue = calculateHeuristic();
        heuristicLabel.setText("Heuristic Value: " + heuristicValue);
    }//end updateGameLabels()

    //initializes the final board
    private void initializeFinalBoard(){
        finalBoard[0][0] = 1;
        finalBoard[0][1] = 2;
        finalBoard[0][2] = 3;
        finalBoard[1][0] = 4;
        finalBoard[1][1] = 5;
        finalBoard[1][2] = 6;
        finalBoard[2][0] = 7;
        finalBoard[2][1] = 8;
        finalBoard[2][2] = 0;
    }//end initializeFinalBoard()

}
