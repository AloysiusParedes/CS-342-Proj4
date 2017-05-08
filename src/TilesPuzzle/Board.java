package TilesPuzzle;

import com.sun.tools.internal.jxc.ap.Const;

import java.util.*;
import java.util.List;

/**
 * Used to store the board pieces, initialize a board, compute the board heuristic value,
 * find possible next moves from any board position.  If you do use a data structure that needs a hash value,
 * you should define that as well.  Unless the board is initialized by the user, the Board class should create
 * a board where the destination pieces are chosen at random.  Seed your random number generator using:
 *  randomGenerator.setSeed( System.currentTimeMillis());
 * so that you get different boards each time you run the program.
 */

public class Board {
    private int[][] pieces = new int[Constants.ROWS][Constants.COLS];
    private int heuristicValue;
    private int numPossibleMoves;
    private int[] possibleMoves = {0, 0, 0, 0}; //Indices: 0 = UP, 1 = DOWN, 2 = LEFT, 3 = RIGHT

    //constructor to create a random board
    public Board(){
        List<Integer> arrList = new ArrayList<>();
        for(int i = 0; i < Constants.BOARD_SIZE; i++)
            arrList.add(i);

        Collections.shuffle(arrList);
        int index = 0;
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                this.pieces[i][j] = arrList.get(index);
                index++;
            }
        }

        this.heuristicValue = calculateHeuristic(this.pieces);
        this.numPossibleMoves = calculateNumPossibleMoves(this.pieces);
        //printBoard(0);
    }//end random Board constructor

    //constructor to create a custom board
    public Board(char[] userInput){
        int index = 0;
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                this.pieces[i][j] = Character.getNumericValue(userInput[index]);
                index++;
            }
        }

        this.heuristicValue = calculateHeuristic(this.pieces);
        this.numPossibleMoves = calculateNumPossibleMoves(this.pieces);
        //printBoard(0);
    }//end custom Board constructor

    //constructor to create the final board
    public Board(char[] userInput, int num){
        int index = 0;
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                this.pieces[i][j] = Character.getNumericValue(userInput[index]);
                index++;
            }
        }

        this.heuristicValue = 0;
        this.numPossibleMoves = 0;
        //printBoard(0);
    }//end custom Board constructor

    //constructor to create a board from a board and direction
    public Board(Board other, int direction){
        int emptyXPos = 0, emptyYPos = 0;

        //find the coordinates of 0
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                if(other.getPiece(i, j) == 0){
                    emptyXPos = i;
                    emptyYPos = j;
                }
            }
        }

        this.pieces = copyPieces(other.getPieces());

        if(direction == 0){ //swap up
            int tempPiece = this.pieces[emptyXPos - 1][emptyYPos];
            this.pieces[emptyXPos - 1][emptyYPos] = 0;
            pieces[emptyXPos][emptyYPos] = tempPiece;
        }
        else if(direction == 1) { //swap down
            int tempPiece = this.pieces[emptyXPos + 1][emptyYPos];
            this.pieces[emptyXPos + 1][emptyYPos] = 0;
            pieces[emptyXPos][emptyYPos] = tempPiece;
        }
        else if(direction == 2){ //swap left
            int tempPiece = this.pieces[emptyXPos][emptyYPos - 1];
            this.pieces[emptyXPos][emptyYPos - 1] = 0;
            pieces[emptyXPos][emptyYPos] = tempPiece;
        }
        else{ //swap right
            int tempPiece = this.pieces[emptyXPos][emptyYPos + 1];
            this.pieces[emptyXPos][emptyYPos + 1] = 0;
            pieces[emptyXPos][emptyYPos] = tempPiece;
        }

        this.heuristicValue = calculateNumPossibleMoves(this.pieces);
        this.numPossibleMoves = calculateNumPossibleMoves(this.pieces);
    }//end directional Board constructor

    //function to return pieces array
    public int[][] getPieces(){
        return pieces;
    }//end getPieces()

    //function to return the value at position (x,y)
    public int getPiece(int x, int y){
        return this.pieces[x][y];
    }//end getPieces()

    //function to return the heuristic value for the current board
    public int getHeuristicValue(){
        return this.heuristicValue;
    }//end getHeuristicValue()

    //function to return the number of possible moves for the current board
    public int getNumPossibleMoves(){
        return this.numPossibleMoves;
    }//end getNumPossibleMoves()

    //function to return array of possible moves
    public int[] getPossibleMoves(){
        return this.possibleMoves;
    }//end getPossibleMoves()

    //helper function for calculateHeuristic to calculate the distance for a given piece to it's final destination.
    private int calculateDistance(int piece, int pieceX, int pieceY){
        int xDistance, yDistance, totalDistance;
        int xFinal = 0, yFinal = 0;

        int foundFinal = 0;

        //find the final position for that piece
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(Constants.END_BOARD.getPiece(i, j) == piece){
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

    //function to calculate heuristic for the given board (piecesArray)
    private int calculateHeuristic(int[][] piecesArray){
        int heuristic = 0;

        //for each piece
        for(int currentPiece = 0; currentPiece < 9; currentPiece++) {
            //find x position
            for (int xPos = 0; xPos < Constants.ROWS; xPos++) {
                //find y position
                for (int yPos = 0; yPos < Constants.COLS; yPos++) {
                    //calculate the distance for that piece after the position has been found
                    if(currentPiece == piecesArray[xPos][yPos]) {
                        heuristic += calculateDistance(currentPiece, xPos, yPos);
                    }
                    //otherwise keep searching for the currentPiece's position
                }
            }
        }

        return heuristic;
    }//end calculateHeuristic(...)

    //helper function for calculateNumPossibleMoves to check if you can move to some direction.
    private int checkDirection(int direction, int x, int y){
        int canMove = 0;

        if(direction == 0) {          //check if moving up a row will go out of arrays bounds
            if ((x - 1) >= 0)
                canMove = 1;
        }
        else if(direction == 1) {  //check if moving down a row will go out of arrays bounds
            if ((x + 1) <= Constants.ROWS - 1)
                canMove = 1;
        }
        else if(direction == 2) {     //check if moving left a column will go out of arrays bounds
            if ((y - 1) >= 0)
                canMove = 1;
        }

        else {                        //check if moving right a column will go out of arrays bounds
            if ((y + 1) <= Constants.COLS - 1)
                canMove = 1;
        }

        return canMove;
    }//return checkDirection

    //function to return and calculate the number of moves for the current board
    private int calculateNumPossibleMoves(int[][] piecesArray){
        int numPossibleMoves = 0;

        int emptyXPos = 0, emptyYPos = 0;
        //find the coordinates of 0
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                if(piecesArray[i][j] == 0){
                    emptyXPos = i;
                    emptyYPos = j;
                }
            }
        }

        //check to see if you can move in a certain direction
        for(int i = 0; i < 4; i++) {
            this.possibleMoves[i] = checkDirection(i, emptyXPos, emptyYPos);
        }

        //count if the certain direction is available
        for(int i = 0; i < 4; i++)
            if(this.possibleMoves[i] == 1)
                numPossibleMoves++;

        return numPossibleMoves;
    }//end calculateNumPossibleMoves(...)

    //function to print out board. " " is represented by integer 0
    public void printBoard(int num){
        if(num > 0)
            System.out.print("\n" + num + ".");
        System.out.println();
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                if(pieces[i][j] == 0)
                    System.out.print("\t" + " " + " ");
                else
                    System.out.print("\t" + pieces[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Heuristic value: " + this.calculateHeuristic(this.pieces));
        //System.out.println("Number of possible moves: " + this.numPossibleMoves);
        return;
    }//end printBoard()

    public String returnStringPrint(int n){
        String board = new String();

        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                board += Integer.toString(pieces[i][j]);
            }
        }

        return board;
    }

    //function to return the String version of the board
    public String toStringValue(){
        char[] piecesArray = new char[Constants.BOARD_SIZE];
        int index = 0;
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                piecesArray[index] = (char) ('0' + this.pieces[i][j]);
                index++;
            }
        }

        return new String (piecesArray);
    }//end toStringValue()

    //function to check if this board is the same as another
    public boolean isMatch(Board other) {
        for (int i = 0; i < Constants.ROWS; i++)
            for (int j = 0; j < Constants.COLS; j++)
                if (this.pieces[i][j] != other.getPiece(i, j))
                    return false;
        return true;
    }//end isMatch(...)

    //function to copy the pieces from one array to another
    public int[][] copyPieces(int[][] piecesArray){
        int[][] newPieces = new int[Constants.ROWS][Constants.COLS];
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                newPieces[i][j] = piecesArray[i][j];
            }
        }

        return newPieces;
    }//end copyPieces(...)

    //function to find the piece to move that gives the shortest heuristic next board
    public int getShortestHeuristicPiece(){
        int pieceToMove = 0;

        int emptyXPos = 0, emptyYPos = 0;
        //find the coordinates of 0
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                if(this.pieces[i][j] == 0){
                    emptyXPos = i;
                    emptyYPos = j;
                }
            }
        }

        //create boards for each valid move
        Board[] directions = new Board[4];
        for(int i = 0; i < 4; i++){
            if(this.possibleMoves[i] == 1) {
                directions[i] = new Board(this, i);
            }
            else{
                directions[i] = null;
            }
        }

        int smallestHeuristic = Integer.MAX_VALUE;
        int direction = 0;
        for(int i = 0; i < 4; i++){
            if(directions[i] != null) {
                if (directions[i].getHeuristicValue() < smallestHeuristic) {
                    smallestHeuristic = directions[i].getHeuristicValue();
                    direction = i;
                }
            }
        }

        if(direction == 0){
            emptyXPos = emptyXPos - 1;
            emptyYPos = emptyYPos;
        }
        else if(direction == 1){
            emptyXPos = emptyXPos + 1;
            emptyYPos = emptyYPos;
        }
        else if(direction == 2){
            emptyXPos = emptyXPos;
            emptyYPos = emptyYPos - 1;
        }
        else {
            emptyXPos = emptyXPos;
            emptyYPos = emptyYPos + 1;
        }

        pieceToMove = this.getPiece(emptyXPos, emptyYPos);

        return pieceToMove;
    }//end getShortestHeursticPiece()

    //function to return an array list holding the indices of possible adjacent moves from index holding '0'
    public ArrayList findPossibleMovesIndices(){
        //array list holding pieces that can be moved
        ArrayList<Integer> possibleMoves = new ArrayList<>();

        //find the coordinates of 0
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                if(this.pieces[i][j] == 0){
                    if(i == 0 && j == 0){
                        possibleMoves.add(new Integer(getPiece(i, j + 1))); //add number to the right
                        possibleMoves.add(new Integer(getPiece(i + 1, j))); //add number below
                    }
                    else if(i == 0 && j == 1){
                        possibleMoves.add(new Integer(getPiece(i, j + 1))); //add number to the right
                        possibleMoves.add(new Integer(getPiece(i + 1, j))); //add number below
                        possibleMoves.add(new Integer(getPiece(i, j - 1))); //add number to the left
                    }
                    else if(i == 0 && j == 2){
                        possibleMoves.add(new Integer(getPiece(i + 1, j))); //add number below
                        possibleMoves.add(new Integer(getPiece(i, j - 1))); //add number to the left
                    }
                    else if(i == 1 && j == 0){
                        possibleMoves.add(new Integer(getPiece(i, j + 1))); //add number to the right
                        possibleMoves.add(new Integer(getPiece(i + 1, j))); //add number below
                        possibleMoves.add(new Integer(getPiece(i - 1, j))); //add number above
                    }
                    else if(i == 1 && j == 1){
                        possibleMoves.add(new Integer(getPiece(i, j + 1))); //add number to the right
                        possibleMoves.add(new Integer(getPiece(i + 1, j))); //add number below
                        possibleMoves.add(new Integer(getPiece(i, j - 1))); //add number to the left
                        possibleMoves.add(new Integer(getPiece(i - 1, j))); //add number above
                    }
                    else if(i == 1 && j == 2){
                        possibleMoves.add(new Integer(getPiece(i - 1, j))); //add number above
                        possibleMoves.add(new Integer(getPiece(i + 1, j))); //add number below
                        possibleMoves.add(new Integer(getPiece(i, j - 1))); //add number to the left
                    }
                    else if(i == 2 && j == 0){
                        possibleMoves.add(new Integer(getPiece(i, j + 1))); //add number to the right
                        possibleMoves.add(new Integer(getPiece(i - 1, j))); //add number above
                    }
                    else if(i == 2 && j == 1){
                        possibleMoves.add(new Integer(getPiece(i, j + 1))); //add number to the right
                        possibleMoves.add(new Integer(getPiece(i - 1, j))); //add number above
                        possibleMoves.add(new Integer(getPiece(i, j - 1))); //add number to the left
                    }
                    else if(i == 2 && j == 2){
                        possibleMoves.add(new Integer(getPiece(i - 1, j))); //add number above
                        possibleMoves.add(new Integer(getPiece(i, j - 1))); //add number to the left
                    }
                    else{
                        System.out.println("Could not find 0 Position on current board\n\n");
                        System.exit(1);
                    }
                }
            }
        }

        System.out.println("Possible Moves: ");
        //print the possible moves
        for(int i = 0; i < possibleMoves.size(); i++){
            //System.out.print(possibleMoves.get(i));
        }

        return possibleMoves;
    }//end findPossibleMovesIndices()

    //function to make a move/switch the pieces
    public void movePiece(int piece){
        int emptyXPos = 0, emptyYPos = 0, moveXPos = 0, moveYPos = 0;
        //find the coordinates of 0 and the piece to move
        for(int i = 0; i < Constants.ROWS; i++){
            for(int j = 0; j < Constants.COLS; j++){
                if(this.pieces[i][j] == 0){
                    emptyXPos = i;
                    emptyYPos = j;
                }
                if(this.pieces[i][j] == piece){
                    moveXPos = i;
                    moveYPos = j;
                }
            }
        }

        //swap
        this.pieces[emptyXPos][emptyYPos] = piece;
        this.pieces[moveXPos][moveYPos] = 0;

        this.heuristicValue = calculateHeuristic(this.pieces);
        this.numPossibleMoves = calculateNumPossibleMoves(this.pieces);

    }//end movePiece(...)


}//end Board() class
