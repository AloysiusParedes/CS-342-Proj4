package TilesPuzzle;

import java.util.*;
import java.io.*;
import java.awt.*;

/**
 * If you have constants shared with multiple classes they should be declared in the Constants class.
 */
public final class Constants {

    private Constants(){} //private constructor

    //constants to be used in application
    public static final int BOARD_SIZE = 9;
    public static final int ROWS = (int) Math.sqrt(BOARD_SIZE);
    public static final int COLS = (int) Math.sqrt(BOARD_SIZE);
    public static final Board END_BOARD = new Board ("123456780".toCharArray(), 1);


}//end Constants class