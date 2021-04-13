package pentago_twist;

import boardgame.Move;
import pentago_twist.Monte_Carlo;

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
        int option = 1;
        Move myMove;

        switch(option){
            case 0:
                myMove = boardState.getRandomMove();
            case 1:
                myMove = Min_max.min_max_helper(boardState,1);

                if (myMove == null){
                    myMove = Monte_Carlo.monte_carlo_helper(boardState,10000);
                }

            case 2:
                ScoreWithMove obj;
                obj = MinMax.minmax(boardState, 2, boardState.getTurnPlayer(), Integer.MIN_VALUE, Integer.MAX_VALUE);
                myMove = obj.getMove();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + option);
        }
        return myMove;
    }

}
