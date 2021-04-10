package student_player;

import pentago_twist.PentagoBoardState;

import static pentago_twist.PentagoBoardState.BOARD_SIZE;

public class MyTools {
	public static int DEPTH = 2;
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

	public static class State{
		//declare the variables, for example towthree means two pieces with same colour plus three empty pieces
		double twothree = 0;
		double twofour = 0;
		double threetwo = 0;
		double threethree = 0;
		double fourone = 0;
		double fourtwo = 0;
		//getNum function
		public double[] getNum(){
			double[] result = { twothree,twofour, threetwo, threethree, fourone, fourtwo};
			return result;
		}
		//increase function that take the input, avail and value of this
		public void increase(int input, int avail, double value){
			if(input == 2){
				if(avail == 3){
					twothree += value;
				}
				else if(avail == 4){
					twofour += value;
				}
			}
			else if(input == 3){
				if(avail == 2){
					threetwo += value;
				}
				else if(avail == 3){
					threethree += value;
				}
			}
			else if(input == 4){
				if(avail == 2){
					fourtwo += value;
				}
				else if(avail == 1){
					fourone += value;
				}
			}
			else{
				return;
			}
		}
	}

	/*checkState function that take the input as currentBoard and player output the 2D double array which contatins
	 * 2 arrays for both black board and white board score*/
	public static double[][] checkState(PentagoBoardState currentBoard){
		State whiteState = new State();
		State blackState = new State();
		for(int i = 0; i < 6; i++){
			checkHorizontal(i, currentBoard, whiteState, blackState);
			checkVertical(i, currentBoard, whiteState, blackState);
		}
		checkDiagRight(0,0,currentBoard, whiteState, blackState, false);
		checkDiagRight(0,1,currentBoard, whiteState, blackState, true);
		checkDiagRight(1,0,currentBoard, whiteState, blackState, true);
		checkDiagLeft(0,5,currentBoard, whiteState, blackState, false);
		checkDiagLeft(0,4,currentBoard, whiteState, blackState, true);
		checkDiagLeft(1,4,currentBoard, whiteState, blackState, true);
		double[][] result = {whiteState.getNum(), blackState.getNum()};
		return  result;
	}

	/*CheckHorizontal function that will go through 6 rows of the board and increase the score of whiteState and blackState*/
	public static void checkHorizontal(int rowNum, PentagoBoardState currentBoard, State whiteState, State blackState){
		int white = 0;
		int avail = 0;
		int black = 0;
		for(int i = 0; i < 6; i++){
			PentagoBoardState.Piece currentPiece = currentBoard.getPieceAt(rowNum, i);
			if(currentPiece == PentagoBoardState.Piece.WHITE){
				white++;
			}
			else if(currentPiece == PentagoBoardState.Piece.BLACK){
				black++;
			}
			else{
				avail++;
			}
		}
		if(white == 0){
			blackState.increase(black, avail, 1);
			return;
		}
		else if(black == 0){
			whiteState.increase(white, avail, 1);
			return;
		}
		else{
			if(white + avail >= 5){
				whiteState.increase(white, avail, 1);
				return;
			}
			else if(black + avail >= 5){
				blackState.increase(black, avail, 1);
				return;
			}
		}
	}

	/*CheckVertical function that will go through 6 columns of the board and increase the score of whiteState and blackState*/
	public static void checkVertical(int colNum, PentagoBoardState currentBoard, State whiteState, State blackState){
		int white = 0;
		int avail = 0;
		int black = 0;
		for(int i = 0; i < 6; i++){
			PentagoBoardState.Piece currentPiece = currentBoard.getPieceAt(i, colNum);
			if(currentPiece == PentagoBoardState.Piece.WHITE){
				white++;
			}
			else if(currentPiece == PentagoBoardState.Piece.BLACK){
				black++;
			}
			else{
				avail++;
			}
		}
		if(white == 0){
			blackState.increase(black, avail, 1);
			return;
		}
		else if(black == 0){
			whiteState.increase(white, avail, 1);
			return;
		}
		else{
			if(white + avail >= 5){
				whiteState.increase(white, avail, 1);
				return;
			}
			else if(black + avail >= 5){
				blackState.increase(black, avail, 1);
				return;
			}
			else{
			}
		}
	}

	/*checkDiagRight that will start from one piece and go their diagonal right then update the score for both white and black*/
	public static void checkDiagRight(int rowNum, int colNum, PentagoBoardState currentBoard, State whiteState, State blackState,Boolean initial){
		int white = 0;
		int black = 0;
		int avail = 0;
		for(int i= 0; i < 6; i++){
			PentagoBoardState.Piece currentPiece = currentBoard.getPieceAt(rowNum, colNum);
			if(currentPiece == PentagoBoardState.Piece.WHITE){
				white++;
			}
			else if(currentPiece == PentagoBoardState.Piece.BLACK){
				black++;
			}
			else{
				avail++;
			}

			if(rowNum < 5 && colNum < 5){
				rowNum++;
				colNum++;
			}
			else{
				break;
			}
		}
		//This means when we have a row whose total pieces are 5(different from others which are 6)
		if(initial){
			if(white == 0){
				blackState.increase(black, avail, 1.5);
				return;
			}
			else if(black == 0){
				whiteState.increase(white, avail, 1.5);
				return;
			}
			else{
				if(white + avail >= 5){
					whiteState.increase(white, avail, 1.5);
					return;
				}
				else if(black + avail >= 5){
					blackState.increase(black, avail, 1.5);
					return;
				}
				else{
				}
			}
		}
		else{
			if(white == 0){
				blackState.increase(black, avail, 1.5);
				return;
			}
			else if(black == 0){
				whiteState.increase(white, avail, 1.5);
				return;
			}
			else{
				if(white + avail >= 5){
					whiteState.increase(white, avail, 1.5);
					return;
				}
				else if(black + avail >= 5){
					blackState.increase(black, avail, 1.5);
					return;
				}
				else{
				}
			}
		}
	}

	/*checkDiagLeft that will start from one piece and go their diagonal right then update the score for both white and black*/
	public static void checkDiagLeft(int rowNum, int colNum, PentagoBoardState currentBoard, State whiteState, State blackState,Boolean initial){
		int white = 0;
		int black = 0;
		int avail = 0;
		for(int i= 0; i<6; i++){
			PentagoBoardState.Piece currentPiece = currentBoard.getPieceAt(rowNum, colNum);
			if(currentPiece == PentagoBoardState.Piece.WHITE){
				white++;
			}
			else if(currentPiece == PentagoBoardState.Piece.BLACK){
				black++;
			}
			else{
				avail++;
			}
			if(rowNum < 5 && colNum > 0){
				rowNum++;
				colNum--;
			}
			else{
				break;
			}
		}
		if(initial){
			if(white == 0){
				blackState.increase(black, avail, 1.5);
				return;
			}
			else if(black == 0){
				whiteState.increase(white, avail, 1.5);
				return;
			}
			else{
				if(white + avail >= 5){
					whiteState.increase(white, avail, 1.5);
					return;
				}
				else if(black + avail >= 5){
					blackState.increase(black, avail, 1.5);
					return;
				}
				else{
				}
			}
		}
		else{
			if(white == 0){
				blackState.increase(black, avail, 1.5);
				return;
			}
			else if(black == 0){
				whiteState.increase(white, avail, 1.5);
				return;
			}
			else{
				if(white + avail >= 5){
					whiteState.increase(white, avail, 1.5);
					return;
				}
				else if(black + avail >= 5){
					blackState.increase(black, avail, 1.5);
					return;
				}
				else{
				}
			}
		}
	}
}