package student_player;

import pentago_twist.PentagoBoardState;

import static pentago_twist.PentagoBoardState.BOARD_SIZE;

public class MyTools {
    public static int DEPTH = 1;
    //Play around with these settings
    public static int ENDGAME = 12;
    public static int LATEGAME = 9;
    public static int MIDGAME = 5;
    public static int MONTE_CARLO_ITTERATION = 7500;
    public static int MAXTIME = 1985;

    public static double getSomething() {
        return Math.random();
    }

    /**
     * make a copy of the board to analyze the pieces
     * @param boardState
     * @return
     */
    public static PentagoBoardState getLayoutCurrentBoardState(PentagoBoardState boardState){
        return (PentagoBoardState) boardState.clone();
    }

    /**
     * This function is used to keep track of what round it is, this will allow the program to know when it is reaching EndGame
     * @param pbs
     * @return
     */
    public static int getCurrentGameRound(PentagoBoardState pbs){
        int round = 0;
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                if(pbs.getPieceAt(i,j) != PentagoBoardState.Piece.EMPTY){
                    round++;
                }
            }
        }
        return round/2 + 1;
    }
}