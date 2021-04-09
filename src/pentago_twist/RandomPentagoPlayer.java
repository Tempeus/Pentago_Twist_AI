package pentago_twist;

import boardgame.Move;
import student_player.Monte_Carlo;

/**
 * @author mgrenander
 */
public class RandomPentagoPlayer extends PentagoPlayer {
    public RandomPentagoPlayer() {
        super("RandomPlayer");
    }

    public RandomPentagoPlayer(String name) {
        super(name);
    }

    @Override
    public Move chooseMove(PentagoBoardState boardState) {
        //return boardState.getRandomMove();

        // Is random the best you can do?
        Move myMove = Min_max.min_max_helper(boardState,1);

        if (myMove == null){
            myMove = Monte_Carlo.monte_carlo_helper(boardState,7500);
        }

        // Return your move to be processed by the server.
        return myMove;

    }


}
