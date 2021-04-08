package student_player;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("XXXXXXX");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {

        // Is random the best you can do?
        System.out.println(boardState.getAllLegalMoves().size());
        System.out.println(Min_max.t_table.size());
        Move myMove = Min_max.min_max_helper(boardState,1);

        if (myMove == null){
            myMove = Monte_Carlo.monte_carlo_helper(boardState,10000);
        }

        // Return your move to be processed by the server.
        return myMove;
    }
}