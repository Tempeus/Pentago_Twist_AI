package student_player;

import pentago_twist.PentagoBoardState;

import static pentago_twist.PentagoBoardState.BOARD_SIZE;

public class MyTools {
    public static int DEPTH = 1;
    //Play arround with these settings
    public static int LATEGAME = 10;
    public static int ENDGAME = 20;

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


    /* ENUMERATION FOR WINNING POSITIONS */
    public enum WinningPositions{

    }
}