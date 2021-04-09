package student_player;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoBoardState.Piece;
import pentago_twist.PentagoMove;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MoveSelect {

    /**
     * This function is used to create a valid Move for the game. It will take an pool of moves that were deemed to be the best. i
     * It will afterwards randomly pick a quadrant to twist.
     * @param playerID      turn Number
     * @param availability  ArrayList of available moves to be picked randomly.
     * @return
     */
    private static PentagoMove createValidMove(int playerID, ArrayList<int []> availability){
        Random rand = new Random();

        //We will get a random coordinate in the pool of best moves
        int[] getRandomCoord = availability.get(rand.nextInt(availability.size()));

        //Pick any quadrants to twist (Q0 Q1 Q2 Q3) and then pick either you want to rotate 90 clockwise or flip horizontally.
        int qtwist = rand.nextInt(4);
        int choice = rand.nextInt(2);

        System.out.println("Putting piece at: "+getRandomCoord[0]+", "+getRandomCoord[1]);
        return new PentagoMove(getRandomCoord[0], getRandomCoord[1], qtwist, choice, playerID);
    }

    /**
     *  This function will be called during EarlyGame (1st and 2nd move). It allows us to capture the best spots for our algorithm to build on
     *  The best positions are (1,1), (4,1), (1,4) , (4,4) because not only are they not affected by the twist, but they are great positions
     *  to adapt strong attacks that forces the opponent to go on the defense.
     *
     *  | 0 1 |
     *  | 2 3 |
     *
     *  We will prioritize the quadrant 1 and quadrant 3.
     * @param playerID  Turn Number
     * @param pbs       Current Board State
     * @return
     */
    public static PentagoMove calcBestEarlyGameMove(int playerID, PentagoBoardState pbs){
        //Check to see if there are any pieces in these coordinates
        //Aim for the best diagonal setup which is located at (4,1) and (1,4)
        //Second best diagonal setup is located at (1,1) and (4,4)

        Piece q0 = pbs.getPieceAt(1,1);
        Piece q1 = pbs.getPieceAt(4,1);
        Piece q2 = pbs.getPieceAt(4,4);
        Piece q3 = pbs.getPieceAt(1,4);
        ArrayList<int[]> availability = new ArrayList<>();

        //Attempt to get best diagonal setup (4,1) (1,4)
        //TODO: FIX bug where it goes into the if statement when its not even true
        System.out.println(q1.compareTo(Piece.EMPTY) == 0);
        System.out.println(q1.compareTo(Piece.values()[1-playerID]) == 0);
        System.out.println(q3.compareTo(Piece.EMPTY) == 0);
        System.out.println(q3.compareTo(Piece.values()[1-playerID]) == 0);

        if((q1.compareTo(Piece.EMPTY) == 0 || q1.compareTo(Piece.values()[1-playerID]) == 0) && (q3.compareTo(Piece.EMPTY) == 0) || q3.compareTo(Piece.values()[1-playerID]) == 0){
            System.out.println("Attempting to Perform best Diagonal Setup");
            if(q1.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {4,1}); }
            if(q3.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {1,4}); }
        }
        //Attempt to get Second Diagonal Setup (1,1) (4,4)
        else if((q0.compareTo(Piece.EMPTY) == 0 || q0.compareTo(Piece.values()[playerID]) == 0) && (q2.compareTo(Piece.EMPTY) == 0) || q2.compareTo(Piece.values()[playerID]) == 0){
            System.out.println("Attempting to perform second best diagonal setup");
            if(q0.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {1,1}); }
            if(q2.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {4,4}); }
        }
        else {
            //Check to see if the best positions are already taken
            CheckAllCenters(q0, q1, q2, q3, availability);
        }

        //If the list is empty
        if(availability.size() == 0){
            //Check to see if the best positions are already taken
            CheckAllCenters(q0, q1, q2, q3, availability);
        }

        //Store the best positions in an arrayList to be picked
        return createValidMove(playerID, availability);
    }

    private static void CheckAllCenters(Piece q0, Piece q1, Piece q2, Piece q3, ArrayList<int[]> availability) {
        if(q0.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {1,1}); }
        if(q1.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {4,1}); }
        if(q2.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {4,4}); }
        if(q3.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {1,4}); }
    }

    /**
     * This function will be called right after EarlyGame Stage (3rd Move) if the player is white. It will put a white piece next to the best spots from Early Game.
     * It will analyze Black's first move to see if they picked a best starting move and place the piece avoiding the direction of that piece.
     * This placed piece will add pressure and force the opponent to think of a defense.
     *
     * If the two pieces are diagonal from each other, place a diagonal piece
     * If the two pieces are horizontal from each other, place a horizontal piece
     * If the two pieces are vertical from each other, place a vertical piece
     * @param playerID  Turn number
     * @param pbs       Board state
     * @return          Best move
     */
    public static PentagoMove calcEarlyAttack(int playerID, PentagoBoardState pbs){
        //Find the best move piece that we placed in Early game
        boolean q0 = (pbs.getPieceAt(1,1).compareTo(Piece.values()[1-playerID]) == 0);
        boolean q1 = (pbs.getPieceAt(4,1).compareTo(Piece.values()[1-playerID]) == 0);
        boolean q2 = (pbs.getPieceAt(4,4).compareTo(Piece.values()[1-playerID]) == 0);
        boolean q3 = (pbs.getPieceAt(1,4).compareTo(Piece.values()[1-playerID]) == 0);
        ArrayList<int[]> availability = new ArrayList<>();

        //Check if the EarlyGame pieces are diagonal from each other

        //Best Diagonal Setup - We would either want to pick (5,2) or (0,3)
        if(q1 && q3){
            System.out.println("Best Diagonal Setup - Attack");
            if (pbs.getPieceAt(2, 5).compareTo(Piece.EMPTY) == 0){ availability.add(new int[]{2, 5}); }
            if (pbs.getPieceAt(3, 0).compareTo(Piece.EMPTY) == 0){ availability.add(new int[]{3, 0}); }
        }
        else if(q0 && q2){
            System.out.println("Second Best Diagonal Setup - Attack");
            if (pbs.getPieceAt(5, 2).compareTo(Piece.EMPTY) == 0){ availability.add(new int[]{5, 2}); }
            if (pbs.getPieceAt(0, 3).compareTo(Piece.EMPTY) == 0){ availability.add(new int[]{0, 3}); }
        }
        else{
            //Find the available moves around our white pieces
            //We can efficiently separate the board into the given quadrants and search withing the quadrants that have our pieces
            //TODO: Double check this isn't ruined by the messed up coordinate system (y,x)
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if (q0) { if (pbs.getPieceAt(i, j).compareTo(Piece.EMPTY) == 0){ availability.add(new int[]{i, j}); } }
                    if (q1) { if (pbs.getPieceAt(i + 3, j).compareTo(Piece.EMPTY) == 0) { availability.add(new int[]{i + 3, j}); } }
                    if (q2) { if (pbs.getPieceAt(i, j + 3).compareTo(Piece.EMPTY) == 0) { availability.add(new int[]{i + 3, j + 3}); } }
                    if (q3) { if (pbs.getPieceAt(i + 3, j + 3).compareTo(Piece.EMPTY) == 0) { availability.add(new int[]{i, j + 3}); } }
                }
            }
        }
        return createValidMove(playerID, availability);
    }

}
