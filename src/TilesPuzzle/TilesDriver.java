package TilesPuzzle;

 /* ------------------------------------------------
  * 8-Tiles Program with a GUI
  *
  * Class: CS 342, Fall 2016
  * System: Windows 10, IntelliJ IDE, Javafx, Scene Builder
  * Author Code Number: 879P
  * Author Code Name: Gosh
  * Author Name: Aloysius Paredes
  *
  * ToDo: "Solve" button is not complete.
  * -------------------------------------------------
  */

/**
 * TilesDriver.java
 * This is the driver for the rest of the program that contains main().
 * Calls the start to begin the GUI via the TilesFXML.fxml file
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TilesDriver extends Application {

    //override the start method in Applications class to set the parent as the .fxml file set
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("TilesFXML.fxml"));
        primaryStage.setTitle("Tiles Puzzle");
        primaryStage.setScene(new Scene(root, 500, 525));
        primaryStage.show();

    }

    //main function that calls to the Application class
    public static void main(String[] args) {
        launch(args);
    }//end main
}
