package student_player;

import boardgame.Board;
import boardgame.Move;

import pentago_twist.PentagoMove;
import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.*;

import static student_player.MyTools.MONTE_CARLO_ITTERATION;
import static student_player.MyTools.MAXTIME;

/**
 * A player file submitted by a student.
 */
public class StudentPlayer extends PentagoPlayer {
    Move myMove;
    LinkedList<MoveValue> moveset = new LinkedList<MoveValue>();

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260862327");
    }

    /**
     * This class is a PentagoMove with a weighted value associated to it.
     */
    public class MoveValue {
        public double value;
        public PentagoMove move;

        public MoveValue(double valuenum, PentagoMove movenum) {
            value = valuenum;
            move = movenum;
        }
    }

    /**
     * Evaluate function with heuristics and point system. This will evaluate the board and will be used in the Minimax algorithm
     * @param boardState
     * @return
     */
    private double evaluateFunction(PentagoBoardState boardState) {
        //case when we already win
        if (boardState.getWinner() == player_id) {
            return Integer.MAX_VALUE - 1;
        }
        
        //case the opponent win
        else if (boardState.getWinner() == 1 - player_id) {
            return Integer.MIN_VALUE + 1;
        }
        
        //case in a draw
        else if (boardState.getWinner() == Board.DRAW) {
            return 0.0;
        }

        //else we will evaluate the current move
        double[][] totalBoardEvaluation = MyTools.checkState(boardState);
        double[] whiteEval = totalBoardEvaluation[0];
        double[] blackEval = totalBoardEvaluation[1];
        double score_eval = 0.0;

        //if we move first (white player)
        if (player_id == 0) {
            score_eval = whiteEval[5] * 10 + whiteEval[3] * 7 + whiteEval[4] * 5 + whiteEval[2] * 3 + whiteEval[0] * 1 + whiteEval[1] * 2;
            score_eval = score_eval - 0.8 * (blackEval[5] * 10 + blackEval[3] * 7 + blackEval[4] * 5 + blackEval[2] * 3 + blackEval[0] * 1 + blackEval[1] * 2);
        }

        //if we move last (black player)
        else {
            score_eval = blackEval[5] * 10 + blackEval[3] * 7 + blackEval[4] * 5 + blackEval[2] * 3 + blackEval[0] * 1 + blackEval[1] * 2;
            score_eval = score_eval - 2 * (whiteEval[5] * 10 + whiteEval[3] * 7 + whiteEval[4] * 5 + whiteEval[2] * 0.5 + whiteEval[0] * 1 + whiteEval[1] * 1);

        }
        return score_eval;
    }

    /**
     * Mini max algorithm that will recursively call itself and increment the depth until it reaches the max depth.
     *
     * @param depth
     * @param depth_max
     * @param alpha
     * @param beta
     * @param boardState
     * @return
     */
    public MoveValue MiniMax(int depth, int depth_max, double alpha, double beta, PentagoBoardState boardState) {

        if (depth == depth_max) return new MoveValue(evaluateFunction(boardState), null);

        ArrayList<PentagoMove> allLegalMoves = boardState.getAllLegalMoves();
        MoveValue bestMove = null;

        //max player
        if (boardState.getTurnPlayer() == player_id) {    //max player

            for (PentagoMove move : allLegalMoves) {
                //apply the move
                PentagoBoardState cloneBoardState = (PentagoBoardState) boardState.clone();
                cloneBoardState.processMove(move);

                if (cloneBoardState.getWinner() == player_id) {
                    return new MoveValue(Integer.MAX_VALUE - 1, move);
                }

                //if we couldn't get a winner then apply this to minimax recursively
                MoveValue moveResult = MiniMax(depth + 1, depth_max, alpha, beta, cloneBoardState);

                // if the best value so far is null we assign the first return value to it
                if (bestMove == null || (moveResult.value > bestMove.value)) {
                    bestMove = moveResult;
                    bestMove.move = move;
                }

                if (alpha < moveResult.value) {
                    //update the best move
                    alpha = moveResult.value;
                    bestMove = moveResult;
                }

                if (alpha >= beta) {    // pruning
                    bestMove.value = beta;
                    bestMove.move = null;
                    return bestMove;
                }
            }
            return bestMove;
        } else {
            //min player
            for (PentagoMove move : allLegalMoves) {
                //apply this move
                PentagoBoardState cloneState = (PentagoBoardState) boardState.clone();
                cloneState.processMove(move);

                if (cloneState.getWinner() == 1 - player_id) {
                    return new MoveValue(Integer.MIN_VALUE + 1, move);
                }
                MoveValue result = MiniMax(depth + 1, depth_max, alpha, beta, cloneState);

                if (bestMove == null || (result.value < bestMove.value)) {
                    bestMove = result;
                    bestMove.move = move;
                }

                if (beta > result.value) {
                    //update the best move
                    beta = result.value;
                    bestMove = result;
                }

                if (alpha >= beta) {
                    bestMove.value = alpha;
                    bestMove.move = null;
                    return bestMove;
                }

            }
            return bestMove;
        }
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     * <p>
     * TODO: STEP 4 for BLACK exceeds
     */
    public Move chooseMove(PentagoBoardState boardState) {
        long startTime = System.currentTimeMillis();

        // Is random the best you can do?
        PentagoBoardState pbs;

        //Make a copy of the board for analysis
        pbs = MyTools.getLayoutCurrentBoardState(boardState);
        int currentGameRound = MyTools.getCurrentGameRound(pbs);
        int playerId = pbs.getTurnPlayer();
        System.out.println("I am: " + playerId);

        //Default Depth
        MyTools.DEPTH = 2;
        //Change Depth depending if its LateGame or EndGame since there are less available moves left
        if (currentGameRound > MyTools.LATEGAME && currentGameRound < MyTools.ENDGAME) {
            System.out.println("ENTERING LATEGAME MODE");
            MyTools.DEPTH = 3;
        } else if (currentGameRound > MyTools.ENDGAME) {
            System.out.println("ENTERING ENDGAME MODE");
            MyTools.DEPTH = 4;
        }

        System.out.println("GAME ROUND: " + currentGameRound);
        myMove = boardState.getRandomMove();

        //EarlyGame
        if (currentGameRound < 3) {
            System.out.println("EarlyGame Moves");
            myMove = MoveSelect.calcBestEarlyGameMove(playerId, pbs);
        }

        //EarlyGame Attack
        else if (currentGameRound == 3) {
            System.out.println("EarlyGame ATK");
            myMove = MoveSelect.calcEarlyAttack(playerId, pbs);
        } else {
            Callable<Object> MoveDecision = new Callable<Object> () {

                @Override
                public Object call() throws Exception {
                    MoveValue optimal;
                    int depth = MyTools.DEPTH;
                    while(!Thread.currentThread().isInterrupted()){
                        optimal = MiniMax(0, MyTools.DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, boardState);
                        if(depth == 2){
                            moveset.add(optimal);
                            moveset.add(optimal);
                        }
                        else{
                            moveset.set(1, moveset.get(0));
                            moveset.set(0, optimal);
                        }
                        depth++;
                    }
                    return null;
                }
            };

            ExecutorService ex = Executors.newSingleThreadExecutor();

            final Future<Object> futureEvent = ex.submit(MoveDecision);
            try {
                futureEvent.get(MAXTIME, TimeUnit.MILLISECONDS);
            }
            catch (TimeoutException e) {
                //this is when time out
                if (moveset.size() > 0) {
                    myMove = moveset.get(0).move;
                    System.out.println("Value of the final move: " + moveset.get(0).value);
                }
            }
            catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally {
                ex.shutdownNow();
            }
            myMove = moveset.get(0).move;

        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Time Elapsed: " + (float) elapsedTime / 1000);

        // Return your move to be processed by the server.
        return myMove;
    }
}

//TODO Black Early Attack not working
//TODO try to get all early moves, check to see if opponent has lines
//TODO improve minimax
