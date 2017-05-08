package TilesPuzzle;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.util.List;

/**
 * This implements the A* state-space search algorithm which uses a heuristic as an approximation
 * of the goodness of each board configuration.  If the chosen heuristic is overly optimistic, then
 * that implies less search, but at the possible extent of an optimal solution.  A heuristic is admissible
 * if it underestimates the goodness of a position.  This implies extra search, but that search then encompasses
 * an optimal solution.
 *
 * The search tree should include some sort of storage for:
 *      1. The list of all unique Nodes
 a*         This can be any sort of data structure, but efficient Node lookup is important,
 *         since this is the data structure you will use to process and store only new unique
 *         Nodes that are not already on this list, out of the total possible ~181,442 moves.
 *         You could maintain this as an ordered list by hash number and use binary search, or you could use a HashMap.
 *      2. The ordered list of leaf Nodes
 *         This could be stored as an ordered linked list or as a Priority Queue.
 *         Here the ordering is done by heuristic value, with a lower heuristic value being preferable.
 *         The heuristic is the sum of the city-blocks distances of each piece from its desired destination.
 */
public class SearchTree {
    private HashMap<String, Node> uniqueNodesList;
    private PriorityQueue<Node> leafNodesList;

    private Node goalNode;
    public Node startNode;

    private List<String> solutionPathList;

    //constructor
    public SearchTree(Board startBoard){
        uniqueNodesList = new HashMap<>();          //open
        leafNodesList = new PriorityQueue<Node>();  //closed
        goalNode = new Node(new Board("123456780".toCharArray(), 1), "123456780");
        startNode = new Node(startBoard, startBoard.toStringValue());
        solutionPathList = new ArrayList<>();
    }//end constructor

    //function to find the path using A* algorithm
    public List<String> algorithm(){
        List<String> inOrder = new ArrayList<>();
        boolean solutionFound = false;

        //put start node into the queue
        leafNodesList.add(startNode);
        uniqueNodesList.put(startNode.getHashKey(), startNode);
        Node temp = null;

        //board to hold the smallest heuristic
        Board smallestHeuristic = new Board(startNode.getBoard().toStringValue().toCharArray());

        while(leafNodesList.isEmpty() == false){
            //retrieve and remove the head of the priority queue
            temp = leafNodesList.poll();

            //check to see if temp is the goal node/solution
            if(temp.isMatch(goalNode) == true){
                solutionFound = true;
                break;
            }

            //check valid moves of temp node
            int[] validMoves = temp.getBoard().getPossibleMoves();

            //create boards for each valid move
            Board[] directions = new Board[4];
            for(int i = 0; i < 4; i++){
                if(validMoves[i] == 1) {
                    directions[i] = new Board(temp.getBoard(), i);
                }
                else{
                    directions[i] = null;
                }
            }

            //for each direction
            for(int i = 0; i < 4; i++){
                //if the board is valid
                if(directions[i] != null){
                    //check to see if it's not in the hashmap
                    if(uniqueNodesList.containsKey(directions[i].toStringValue()) == false){
                        //add it to the priority queue
                        Node childNode = new Node(directions[i], directions[i].toStringValue(), temp);
                        leafNodesList.add(childNode);
                        uniqueNodesList.put(childNode.getHashKey(), childNode);

                        //check to see if a smallest heuristic value was found
                        if(smallestHeuristic.getHeuristicValue() > childNode.getBoard().getHeuristicValue())
                            smallestHeuristic = new Board(childNode.getBoard().toStringValue().toCharArray());
                    }
                }
            }

            //uniqueNodesList.put(temp.getHashKey(), temp);
        }//end while(...)

        if(solutionFound) {
            Node curr = temp.getParent();
            int i = 1;
            solutionPathList.add(temp.getBoard().returnStringPrint(i));
            while(curr.getParent() != null) {
                solutionPathList.add(curr.getBoard().returnStringPrint(i));
                curr = curr.getParent();
                i++;
            }

            temp.printPathInOrder(i);
            temp.getBoard().printBoard(i);

            int sol = 1;



            for(int j = solutionPathList.size() - 1; j >= 0; j--){
                inOrder.add(solutionPathList.get(j));
            }
            for(String obj: inOrder){
                System.out.println(sol + ": " + obj);
                sol++;
            }

        }
        else {
            System.out.println("\nAll moves have been tried. \n" +
                    "That puzzle is impossible to solve.  Best board found was: ");

            smallestHeuristic.printBoard(1);
            inOrder.add(smallestHeuristic.toStringValue());
            int sol = 1;
            for(String obj: solutionPathList){
                System.out.println(sol + ": " + obj);
                sol++;
            }



        }

        return inOrder;
    }//end algorithm()




}//end SearchTree class