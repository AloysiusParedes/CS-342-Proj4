package TilesPuzzle;

import java.util.*;
import java.io.*;
import java.awt.*;

/**
 * Nodes are what are stored in the SearchTree.
 * Each node should have a Board as well as elements needed to connect Nodes to each other, as needed.
 */
//accepts a single line string and to be used as a hash key
public class Node implements Comparable{
    private Board board;
    private String hashKey;
    private Node parentNode;
    private int heuristic;

    //constructor for the parent node
    public Node(Board otherBoard, String key){
        this.board = otherBoard;
        this.hashKey = key;
        this.parentNode = null;
        this.heuristic = this.board.getHeuristicValue();
    }//end parent node constructor

    //constructor for the child(ren) node
    public Node(Board otherBoard, String key, Node parent){
        this.board = otherBoard;
        this.hashKey = key;
        this.parentNode = parent;
        this.heuristic = this.board.getHeuristicValue();
    }//end child node constructor

    //function to return the node's board
    public Board getBoard(){
        return this.board;
    }//end getBoard()

    //function to return the node's hash key value
    public String getHashKey(){
        return this.hashKey;
    }//end getHashKey()

    //function to return the node's parent
    public Node getParent(){
        return this.parentNode;
    }//end getParent()

    //function to return the node's heuristic
    public int getHeuristic(){
        return this.heuristic;
    }//end getParent()

    //function to check if a node is the same as another
    public boolean isMatch(Node other){
        if(this.heuristic == other.getHeuristic() || this.board.isMatch(other.getBoard()) == true)
            return true;
        return false;
    }//end isMatch(...)

    //function that compares nodes
    //return: negative if the current node is better, positive if the other node is better, 0 if they are the same
    public int compareTo(Object other){
        Node otherNode = (Node) other;
        if(this.heuristic < otherNode.getHeuristic())
            return -1;
        else if(this.heuristic > otherNode.getHeuristic())
            return 1;
        else
            return 0;


        //return this.heuristic - otherNode.getHeuristic();
    }//end compareTo(...)

    //function to print the solvable path in order
    public void printPathInOrder(int i){
        if(this.parentNode.getParent() != null) {
            this.parentNode.printPathInOrder(--i);
        }

        this.parentNode.getBoard().printBoard(i);

    }//end printPathInOrder(...)



}//end Node class
