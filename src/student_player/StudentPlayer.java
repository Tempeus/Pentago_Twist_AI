package student_player;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;

import static student_player.MyTools.MONTE_CARLO_ITTERATION;
import static student_player.MyTools.MAXTIME;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {
    Move myMove;

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
     *
     * TODO: STEP 4 for BLACK exceeds
     */
    public Move chooseMove(PentagoBoardState boardState) {
        long startTime = System.currentTimeMillis();

        // Is random the best you can do?
        System.out.println(boardState.getAllLegalMoves().size());
        System.out.println(Min_max.t_table.size());
        PentagoBoardState pbs;

        //Make a copy of the board for analysis
        pbs = MyTools.getLayoutCurrentBoardState(boardState);
        int playerId = pbs.getTurnPlayer();
        System.out.println("I am: "+ playerId);

        //Default Depth
        MyTools.DEPTH = 1;
        //Change Depth depending if its LateGame or EndGame since there are less available moves left
        if(MyTools.getCurrentGameRound(pbs) > MyTools.MIDGAME && MyTools.getCurrentGameRound(pbs) < MyTools.LATEGAME){
            System.out.println("ENTERING LATEGAME MODE");
            MONTE_CARLO_ITTERATION = 20000;
        }
        else if(MyTools.getCurrentGameRound(pbs) > MyTools.LATEGAME && MyTools.getCurrentGameRound(pbs) < MyTools.ENDGAME){
            System.out.println("ENTERING LATEGAME MODE");
            MONTE_CARLO_ITTERATION = 30000;
        }
        else if(MyTools.getCurrentGameRound(pbs) > MyTools.ENDGAME){
            System.out.println("ENTERING ENDGAME MODE");
            MyTools.DEPTH = 2;
        }

        System.out.println("GAME ROUND: " + MyTools.getCurrentGameRound(pbs));
        myMove = boardState.getRandomMove();

        DecisionMove dmove = new DecisionMove(pbs, boardState, playerId);
        Thread t = new Thread(dmove);
        t.start();
        try{
            t.join(MAXTIME);
        } catch (InterruptedException e) {
            t.interrupt();
        }
        if(t.isAlive()){
            t.interrupt();
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time Elapsed: " + (float) elapsedTime / 1000);

        // Return your move to be processed by the server.
        return myMove;
    }

    public class DecisionMove implements  Runnable {
        PentagoBoardState pbs_copy;
        PentagoBoardState boardState;
        int playerId;

        public DecisionMove(PentagoBoardState b, PentagoBoardState state, int p){
            pbs_copy = b;
            boardState = state;
            playerId = p;
        }

        @Override
        public void run(){
            try{
                //EarlyGame
                if(MyTools.getCurrentGameRound(pbs_copy) < 3){
                    System.out.println("EarlyGame Moves");
                    myMove = MoveSelect.calcBestEarlyGameMove(playerId, pbs_copy);
                }

                //EarlyGame Attack
                else if(MyTools.getCurrentGameRound(pbs_copy) == 3){
                    System.out.println("EarlyGame ATK");
                    myMove = MoveSelect.calcEarlyAttack(playerId, pbs_copy);
                }

                //No longer EarlyGame
                else{
                    myMove = Min_max.min_max_helper(boardState,MyTools.DEPTH);
                    if (myMove == null){
                        myMove = Monte_Carlo.monte_carlo_helper(boardState,MONTE_CARLO_ITTERATION);
                    }
                }
            } catch (Exception e) {
                System.out.println("TIMED OUT");
            }
        }
    }
}

//check if player has three or more, else pick prime position
//take the middle