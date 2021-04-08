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
     * @param playerID  Turn Number
     * @param pbs       Current Board State
     * @return
     */
    public static PentagoMove calcBestEarlyGameMove(int playerID, PentagoBoardState pbs){
        //Check to see if there are any pieces in these coordinates
        Piece q0 = pbs.getPieceAt(1,1);
        Piece q1 = pbs.getPieceAt(4,1);
        Piece q2 = pbs.getPieceAt(4,4);
        Piece q3 = pbs.getPieceAt(1,4);

        ArrayList<int[]> availability = new ArrayList<>();
        //Check to see if the best positions are already taken
        if(q0.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {1,1}); }
        if(q1.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {4,1}); }
        if(q2.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {4,4}); }
        if(q3.compareTo(Piece.EMPTY) == 0){ availability.add(new int[] {1,4}); }

        //Store the best positions in an arrayList to be picked
        return createValidMove(playerID, availability);
    }

    /**
     * This function will be called right after EarlyGame Stage (3rd Move) if the player is white. It will put a white piece next to the best spots from Early Game.
     * It will analyze Black's first move to see if they picked a best starting move and place the piece avoiding the direction of that piece.
     * This placed piece will add pressure and force the opponent to think of a defense.
     * @param playerID  Turn number
     * @param pbs       Board state
     * @return          Best move
     */
    public static PentagoMove calcEarlyWhiteAtk(int playerID, PentagoBoardState pbs){
        //Find the best move piece that we placed in Early game
        boolean q0 = (pbs.getPieceAt(1,1).compareTo(Piece.WHITE) == 0);
        boolean q1 = (pbs.getPieceAt(4,1).compareTo(Piece.WHITE) == 0);
        boolean q2 = (pbs.getPieceAt(4,4).compareTo(Piece.WHITE) == 0);
        boolean q3 = (pbs.getPieceAt(1,4).compareTo(Piece.WHITE) == 0);

        ArrayList<int[]> availability = new ArrayList<>();

        //Find the available moves around our white pieces
        //We can efficiently separate the board into the given quadrants and search withing the quadrants that have our pieces
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if (q0) { if (pbs.getPieceAt(i, j).compareTo(Piece.EMPTY) == 0){ availability.add(new int[]{i, j}); } }
                if (q1) { if (pbs.getPieceAt(i + 3, j).compareTo(Piece.EMPTY) == 0) { availability.add(new int[]{i + 3, j}); } }
                if (q2) { if (pbs.getPieceAt(i, j + 3).compareTo(Piece.EMPTY) == 0) { availability.add(new int[]{i + 3, j + 3}); } }
                if (q3) { if (pbs.getPieceAt(i + 3, j + 3).compareTo(Piece.EMPTY) == 0) { availability.add(new int[]{i, j + 3}); } }
            }
        }

        return createValidMove(playerID, availability);
    }

}
