package student_player;

import boardgame.Board;
import boardgame.Move;

import pentago_twist.PentagoMove;
import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState;

import java.util.ArrayList;

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

	public class MoveValue {
		public double value;
		public PentagoMove move;
		public MoveValue(double valuenum, PentagoMove movenum) {
			value = valuenum;
			move = movenum;
		}
	}

	private double evaluateFunction(PentagoBoardState boardState){
		//case when we already win
		if(boardState.getWinner() == player_id){
			return Integer.MAX_VALUE-1;
		}
		//case the opponent win
		else if(boardState.getWinner() == 1-player_id){
			return Integer.MIN_VALUE+1;
		}
		//case in a draw
		else if(boardState.getWinner() == Board.DRAW){
			return 0.0;
		}

		//else we will evaluate the current move
		double[][] totalResult = MyTools.checkState(boardState);
		double[] whiteResult = totalResult[0];
		double[] blackResult = totalResult[1];
		double evaluatedscore =0.0;

		//if we move first
		if(player_id== 0){
			evaluatedscore = whiteResult[5]*10 + whiteResult[3]*7 + whiteResult[4]*5 + whiteResult[2]*3 + whiteResult[0]*1 + whiteResult[1]*2;
			evaluatedscore = evaluatedscore - 0.8*(blackResult[5]*10+ blackResult[3]*7 + blackResult[4]*5 + blackResult[2]*3 + blackResult[0]*1 + blackResult[1]*2);
		}

		//if we move last
		else{
			evaluatedscore = blackResult[5]*10 + blackResult[3]*7 + blackResult[4]*5 + blackResult[2]*3 + blackResult[0]*1 + blackResult[1]*2;
			evaluatedscore = evaluatedscore - 2*(whiteResult[5]*10 + whiteResult[3]*7 + whiteResult[4]*5 + whiteResult[2]*0.5 + whiteResult[0]*1 + whiteResult[1]*1);

		}
		return evaluatedscore;
	}

	public MoveValue MiniMax(int depth, int maxDepth, double alpha, double beta, PentagoBoardState boardState) {

		System.out.println("Doing MINIMAX");
		if (depth == maxDepth) return new MoveValue(evaluateFunction(boardState), null);

		ArrayList<PentagoMove> moveoptions = boardState.getAllLegalMoves();
		MoveValue bestMove = null;
		//max player
		if (boardState.getTurnPlayer() == player_id) {    //max player

			for (PentagoMove move : moveoptions) {
				//apply the move
				PentagoBoardState cloneState = (PentagoBoardState) boardState.clone();
				cloneState.processMove(move);

				if (cloneState.getWinner() == player_id) {
					return new MoveValue(Integer.MAX_VALUE - 1, move);
				}

				//if we couldn't get a winner then apply this to minimax recursively
				MoveValue resultMove = MiniMax(depth + 1, maxDepth, alpha, beta, cloneState);

				// if the best value so far is null we assign the first return value to it
				if (bestMove == null || (resultMove.value > bestMove.value)) {
					bestMove = resultMove;
					bestMove.move = move;
				}


				if (alpha < resultMove.value) {
					//update the best move
					alpha = resultMove.value;
					bestMove = resultMove;
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
			for (PentagoMove move : moveoptions) {
				//apply this move
				PentagoBoardState cloneState = (PentagoBoardState) boardState.clone();
				cloneState.processMove(move);

				if (cloneState.getWinner() == 1 - player_id) {
					return new MoveValue(Integer.MIN_VALUE + 1, move);
				}
				MoveValue result = MiniMax(depth + 1, maxDepth, alpha, beta, cloneState);

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
	 *
	 * TODO: STEP 4 for BLACK exceeds
	 */
	public Move chooseMove(PentagoBoardState boardState) {
		long startTime = System.currentTimeMillis();

		// Is random the best you can do?
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
				MoveValue optimal;

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
					optimal = MiniMax(0, MyTools.DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, boardState);
					if (optimal == null){
						myMove = Monte_Carlo.monte_carlo_helper(boardState,MONTE_CARLO_ITTERATION);
					}
					else{
						myMove = optimal.move;
					}
				}
			} catch (Exception e) {
				System.out.println("TIMED OUT");
			}
		}
	}
}

//TODO Black Early Attack not working
//try to get all early moves, check to see if opponent has lines

