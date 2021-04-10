package pentago_twist;
import java.util.*;
import java.util.function.UnaryOperator;

import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoCoord;
import pentago_twist.PentagoMove;

class ScoreWithMove {

    private PentagoMove move;
    private int score;

    public ScoreWithMove(PentagoMove move, int score) {
        this.move = move;
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }
    public Move getMove() {
        return this.move;
    }
}

public class MinMax {

    public static final int BOARD_SIZE = 6;
    private static final UnaryOperator<PentagoCoord> getNextHorizontal = c -> new PentagoCoord(c.getX(), c.getY()+1);
    private static final UnaryOperator<PentagoCoord> getNextVertical = c -> new PentagoCoord(c.getX()+1, c.getY());
    private static final UnaryOperator<PentagoCoord> getNextDiagRight = c -> new PentagoCoord(c.getX()+1, c.getY()+1);
    private static final UnaryOperator<PentagoCoord> getNextDiagLeft = c -> new PentagoCoord(c.getX()+1, c.getY()-1);

    public static int[] checkVertTwo(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0, 5, 0, BOARD_SIZE, getNextVertical, 2);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, 5, 0, BOARD_SIZE, getNextVertical, 2);
//        counter[0] = checkWinRange(boardState, TurnPlayer, 0, 2, 0, BOARD_SIZE, getNextVertical, 2)+checkWinRange(boardState, TurnPlayer, 3, 5, 0, BOARD_SIZE, getNextVertical, 2);
//        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, 2, 0, BOARD_SIZE, getNextVertical, 2)+checkWinRange(boardState, 1-TurnPlayer, 3, 5, 0, BOARD_SIZE, getNextVertical, 2);
        return counter;
    }

    private static int[] checkVertThree(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0, 4, 0, BOARD_SIZE, getNextVertical, 3);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, 4, 0, BOARD_SIZE, getNextVertical, 3);
        return counter;
    }
    private static int[] checkVertFour(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0, 3, 0, BOARD_SIZE, getNextVertical, 4);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, 3, 0, BOARD_SIZE, getNextVertical, 4);
        return counter;
    }

    private static int[] checkHorTwo(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0, BOARD_SIZE, 0, 5, getNextHorizontal, 2);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, BOARD_SIZE, 0, 5, getNextHorizontal, 2);
        return counter;
    }

    public static int[] checkHorThree(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0, BOARD_SIZE, 0, 4, getNextHorizontal, 3);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, BOARD_SIZE, 0, 4, getNextHorizontal, 3);
        return counter;
    }
    private static int[] checkHorFour(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0, BOARD_SIZE, 0, 3, getNextHorizontal, 4);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, BOARD_SIZE, 0, 3, getNextHorizontal, 4);
        return counter;
    }

    private static int[] checkDiagRTwo(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0, 5, 0, 5, getNextDiagRight, 2);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, 5, 0, 5, getNextDiagRight, 2);
        return counter;
    }

    private static int[] checkDiagRThree(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0, 4, 0, 4, getNextDiagRight, 3);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, 4, 0, 4, getNextDiagRight, 3);
        return counter;
    }
    private static int[] checkDiagRFour(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0, 3, 0, 3, getNextDiagRight, 4);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0, 3, 0, 3, getNextDiagRight, 4);
        return counter;
    }

    private static int[] checkDiagLTwo(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0 ,5, 1, BOARD_SIZE, getNextDiagLeft, 2) ;
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0 ,5, 1, BOARD_SIZE, getNextDiagLeft, 2);
        return counter;
    }

    private static int[] checkDiagLThree(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0 ,4, 2, BOARD_SIZE, getNextDiagLeft, 3);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0 ,4, 2, BOARD_SIZE, getNextDiagLeft, 3);
        return counter;
    }

    private static int[] checkDiagLFour(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[2];
        counter[0] = checkWinRange(boardState, TurnPlayer, 0 ,3, 3, BOARD_SIZE, getNextDiagLeft, 4);
        counter[1] = checkWinRange(boardState, 1-TurnPlayer, 0 ,3, 3, BOARD_SIZE, getNextDiagLeft, 4);
        return counter;
    }

    public static int[] checkBlock(PentagoBoardState boardState, int TurnPlayer) {
        int[] counter  = new int[8];
        //Ver
        counter[0] = checkBlockRange(boardState, TurnPlayer, 0, 5, 0, BOARD_SIZE, getNextVertical, 2);
        //Hor
        counter[1] = checkBlockRange(boardState, TurnPlayer, 0, BOARD_SIZE, 0, 5, getNextHorizontal, 2);
        //DiagR
        counter[2] = checkBlockRange(boardState, TurnPlayer, 0, 5, 0, 5, getNextDiagRight, 2);
        //DiagL
        counter[3] = checkBlockRange(boardState, TurnPlayer, 0 ,5, 1, BOARD_SIZE, getNextDiagLeft, 2);

        //Ver
        counter[4] = checkBlockRange(boardState, TurnPlayer, 0, 4, 0, BOARD_SIZE, getNextVertical, 3);
        //Hor
        counter[5] = checkBlockRange(boardState, TurnPlayer, 0, BOARD_SIZE, 0, 4, getNextHorizontal, 3);
        //DiagR
        counter[6] = checkBlockRange(boardState, TurnPlayer, 0, 4, 0, 4, getNextDiagRight, 3);
        //DiagL
        counter[7] = checkBlockRange(boardState, TurnPlayer, 0 ,4, 2, BOARD_SIZE, getNextDiagLeft, 3);

        return counter;

    }

//    public static int[] checkBlockThree(PentagoBoardState boardState, int TurnPlayer) {
//
//        int[] counter  = new int[4];
//        //Ver
//        counter[0] = checkBlockRange(boardState, TurnPlayer, 0, 4, 0, BOARD_SIZE, getNextVertical, 3);
//        //Hor
//        counter[1] = checkBlockRange(boardState, TurnPlayer, 0, BOARD_SIZE, 0, 4, getNextHorizontal, 3);
//        //DiagR
//        counter[2] = checkBlockRange(boardState, TurnPlayer, 0, 4, 0, 4, getNextDiagRight, 3);
//        //DiagL
//        counter[3] = checkBlockRange(boardState, TurnPlayer, 0 ,4, 2, BOARD_SIZE, getNextDiagLeft, 3);
//        return counter;
//    }

    private static int checkBlockRange(PentagoBoardState boardState, int player, int xStart, int xEnd, int yStart, int yEnd, UnaryOperator<PentagoCoord> direction, int number) {
        int bcounter = 0;
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                bcounter += checkBlockLine(boardState, player, new PentagoCoord(i, j), direction, number);
            }
        }
        return bcounter;
    }

    private static int checkBlockLine(PentagoBoardState boardState, int player, PentagoCoord pentagoCoord, UnaryOperator<PentagoCoord> direction, int number) {
            int lineCounter = 0;
            PentagoBoardState.Piece currColour;
            PentagoBoardState.Piece oppColour;

            if (player == 0) {
                currColour = PentagoBoardState.Piece.WHITE;
                oppColour =  PentagoBoardState.Piece.BLACK;
            } else {
                currColour = PentagoBoardState.Piece.BLACK;
                oppColour =  PentagoBoardState.Piece.WHITE;
            }

            boolean isFrontBlock = oppColour == boardState.getPieceAt(pentagoCoord.getX(), pentagoCoord.getY());
            PentagoCoord current = pentagoCoord;

            while (true) {
                try {
                    if (!isFrontBlock && lineCounter == number - 1 && oppColour == boardState.getPieceAt(current.getX(), current.getY())) {
                        lineCounter++;
                        break;
                    } else if (currColour == boardState.getPieceAt(current.getX(), current.getY()) || (isFrontBlock && lineCounter == 0)) {
                        lineCounter++;
                        current = direction.apply(current);
                    } else {
                        break;
                    }
                } catch (IllegalArgumentException e) { //We have run off the board
                    break;
                }
            }
            return lineCounter == number ? 1 : 0;
    }

    private static int checkWinRange(PentagoBoardState boardState, int player, int xStart, int xEnd, int yStart, int yEnd, UnaryOperator<PentagoCoord> direction, int number) {
        int counter = 0;
        for (int i = xStart; i < xEnd; i++) {
            for (int j = yStart; j < yEnd; j++) {
                counter += checkLine(boardState, player, new PentagoCoord(i, j), direction, number);
            }
        }
        return counter;
    }

    private static int checkLine(PentagoBoardState boardState, int playerID, PentagoCoord start, UnaryOperator<PentagoCoord> direction, int number) {
        int lineCounter = 0;
        PentagoBoardState.Piece currColour = playerID == 0 ? PentagoBoardState.Piece.WHITE : PentagoBoardState.Piece.BLACK;
        PentagoCoord current = start;
        while(true) {
            try {
                if (currColour == boardState.getPieceAt(current.getX(), current.getY())) {
                    lineCounter++;
                    current = direction.apply(current);
                } else {
                    break;
                }
            } catch (IllegalArgumentException e) { //We have run off the board
                break;
            }
        }
        return lineCounter == number ? 1 : 0;
    }

    static int evaluate(PentagoBoardState boardState, int playerID)
    {
        int sum = 0;
//        System.out.println("Turn number: " + boardState.getTurnNumber());
        //being aggressive and try to aggregate pieces together
//        if( boardState.getTurnNumber() <= 2) {
//
//            int[] verTwo = checkVertTwo(boardState, playerID);
//            int[] horTwo = checkHorTwo(boardState, playerID);
//            int[] diagRTwo = checkDiagRTwo(boardState, playerID);
//            int[] diagLTwo = checkDiagLTwo(boardState, playerID);
//
////        System.out.println("Ver 2 Cur: " + verTwo[0] + " opp: "+ verTwo[1]);
//            sum += (verTwo[0] >= 1 || horTwo[0] >= 1) ? (20 + (verTwo[0] + horTwo[0]) * 10) : 0;
//            sum += (diagRTwo[0] >= 1 || diagLTwo[0] >= 1) ? (10 + (verTwo[0] + horTwo[0]) * 10) : 0;
////            System.out.println(playerID+"" + boardState.toString()+ "" + sum);
////
//        }
//        else {

            int[] block = checkBlock(boardState,playerID);

            sum += (block[0] >= 1 || block[1] >= 1)? (25 + ((block[0] +block[1]) * 10) ): 0;

            sum += (block[2] >= 1 || block[3] >= 1)? (15 + ((block[2]+block[3]) * 10)): 0;

            sum += (block[4] >= 1 || block[5] >= 1)? (65 + ((block[4] +block[5]) * 10) ): 0;

            sum += (block[6] >= 1 || block[7] >= 1)? (55 + ((block[6]+block[7]) * 10)): 0;


//            if(sum > 20){
//                System.out.println("Score: " + sum + " " + playerID+" blocked" + boardState.toString());
//            }

//            int[] blockThree = checkBlockThree(boardState,playerID);
//
//            sum += (blockThree[0] >= 1 || blockThree[1] >= 1)? (65 + ((blockThree[0] +blockThree[1]) * 10) ): 0;
//
//            sum += (blockThree[2] >= 1 || blockThree[3] >= 1)? (55 + ((blockThree[2]+blockThree[3]) * 10)): 0;

//            if(sum > 20){
//                System.out.println("Score: " + sum + " " + playerID+" blocked" + boardState.toString());
//            }


//            int[] horizontalThree = checkHorThree(boardState, playerID);
//            int[] horizontalFour = checkHorFour(boardState, playerID);
//            int[] verticalThree = checkVertThree(boardState, playerID);
//            int[] verticalFour = checkVertFour(boardState, playerID);
//            int[] diagRightThree = checkDiagRThree(boardState, playerID);
//            int[] diagRightFour = checkDiagRFour(boardState, playerID);
//            int[] diagLeftThree = checkDiagLThree(boardState, playerID);
//            int[] diagLeftFour = checkDiagLFour(boardState, playerID);
//
//            sum += (horizontalThree[0] >= 1 || verticalThree[0] >= 1) ? 60 + (horizontalThree[0] + verticalThree[0]) * 10 : 0;
//
//            sum += (horizontalFour[0] >= 1 || verticalFour[0] >= 1) ? 120 + (horizontalFour[0] + verticalFour[0]) * 10 : 0;
//
//            sum += (diagLeftThree[0] >= 1 || diagLeftThree[0] >= 1) ? 40 + (diagLeftThree[0] + diagRightThree[0]) * 10 : 0;
//
//            sum += (diagRightFour[0] >= 1 || diagRightFour[0] >= 1) ? 80 + (diagRightFour[0] + diagLeftFour[0]) * 10 : 0;
//
//            sum -= (horizontalThree[1] >= 1 || verticalThree[1] >= 1) ? (60 + (horizontalThree[1] + verticalThree[1]) * 10) : 0;
//
//            sum -= (horizontalFour[1] >= 1 || verticalFour[1] >= 1) ? (120 + (horizontalFour[1] + verticalFour[1]) * 10) : 0;
//
//            sum -= (diagRightThree[1] >= 1 || diagLeftThree[1] >= 1) ? (40 + (diagRightThree[1] + diagLeftThree[1]) * 10) : 0;
//
//            sum -= (diagRightFour[1] >= 1 || diagLeftFour[1] >= 1) ? (80 + (diagRightFour[1] + diagLeftFour[1]) * 10) : 0;
////        }


        if(boardState.getWinner() == playerID){
//            System.out.println("i am about to win");
//            boardState.printBoard();
            sum += 3000;
        }

        else if(boardState.getWinner() == 1-playerID){
//            System.out.println("i am about to lose");
//            boardState.printBoard();
            sum -= 3000;
        }

        // Else if none of them have won then return 0
//        System.out.println("Heuristic" + sum);
        return sum;
    }

    static ScoreWithMove minmax(PentagoBoardState boardState, int depth, int playerID, int alpha, int beta) {

        ArrayList<PentagoMove> moves = boardState.getAllLegalMoves();
//        Collections.shuffle(moves);

        int currPlayerId = boardState.getTurnPlayer();

        if (depth == 0 || moves.size() == 1) {
            return new ScoreWithMove(moves.get(0), evaluate(boardState, playerID));
        }

        ScoreWithMove bestMove;

        if (currPlayerId == playerID) {
            bestMove = new ScoreWithMove(moves.get(0), Integer.MIN_VALUE);
        } else {
            bestMove = new ScoreWithMove(moves.get(0), Integer.MAX_VALUE);
        }


        for (PentagoMove move : moves) {

            if (boardState.getWinner() == playerID && currPlayerId == playerID) {
                return new ScoreWithMove(move, 10000);
            }

            if (boardState.getWinner() == (1-playerID) && currPlayerId == 1-playerID) {
                return new ScoreWithMove(move, -100000);
            }

            PentagoBoardState board = (PentagoBoardState) boardState.clone();
            board.processMove(move);

            ScoreWithMove curScoreWithMove = minmax(board, depth - 1, playerID, alpha, beta);

            int curScore = curScoreWithMove.getScore();   // for minmax
            int bestMoveScore = bestMove.getScore();  // for best

            if (currPlayerId == playerID) {

                if(curScore >  bestMoveScore) {
                    bestMove = new ScoreWithMove(move, curScore);
                }

                if( alpha  < curScore){
                    alpha = curScore;
                }

            } else {

                if(curScore <  bestMoveScore) {
                    bestMove = new ScoreWithMove(move, curScore);
                }
                if( beta  > curScore){
                    beta = curScore;
                }
            }

            // Detects if a branch should be pruned.
            if (alpha >= beta) {
//                System.out.println("pruned");
                break;
            }
        }
        return bestMove;


    }


}
