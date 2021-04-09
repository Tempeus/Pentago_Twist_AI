package student_player;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {
    private int maxTime = 1970;

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260862327");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {
        long startTime = System.currentTimeMillis();

        // Is random the best you can do?
        System.out.println(boardState.getAllLegalMoves().size());
        System.out.println(Min_max.t_table.size());
        Move myMove;
        PentagoBoardState pbs;

        //Make a copy of the board for analysis
        pbs = MyTools.getLayoutCurrentBoardState(boardState);
        int playerId = pbs.getTurnPlayer();

        //Default Depth
        MyTools.DEPTH = 1;
        //Change Depth depending if its LateGame or EndGame since there are less available moves left
        if(MyTools.getCurrentGameRound(pbs) > MyTools.LATEGAME && MyTools.getCurrentGameRound(pbs) < MyTools.ENDGAME){
            System.out.println("ENTERING LATEGAME MODE");
            MyTools.DEPTH = 2;
        }
        else if(MyTools.getCurrentGameRound(pbs) > MyTools.ENDGAME){
            System.out.println("ENTERING ENDGAME MODE");
            MyTools.DEPTH = 3;
        }

        System.out.println("GAME ROUND: " + MyTools.getCurrentGameRound(pbs));

        //EarlyGame
        if(MyTools.getCurrentGameRound(pbs) < 3){
            System.out.println("EarlyGame Moves");
            myMove = MoveSelect.calcBestEarlyGameMove(playerId, pbs);
        }

        //White Move Attack Advantage
        else if(playerId == 0 && MyTools.getCurrentGameRound(pbs) == 3){
            System.out.println("White Advantage ATK");
            myMove = MoveSelect.calcEarlyWhiteAtk(playerId, pbs);
        }

        //No longer EarlyGame
        else{
            myMove = Min_max.min_max_helper(boardState,MyTools.DEPTH);
            if (myMove == null){
                myMove = Monte_Carlo.monte_carlo_helper(boardState,10000);
            }
        }

        // Return your move to be processed by the server.
        return myMove;
    }
}