package student_player;

import boardgame.Board;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

import java.util.*;

public class Min_max {
    public static Hashtable<Long, Float> t_table = new Hashtable<Long, Float>();

    public static PentagoMove min_max_helper(PentagoBoardState game, int depth) {
        Random rand = new Random();
        ArrayList<PentagoMove> moves = game.getAllLegalMoves();

        int player = game.getTurnPlayer();
        int tkn;
        if (player == 0) {
            tkn = -1;
        } else {
            tkn = 1;
        }
        ArrayList<Float> scores = new ArrayList<>();
        for (PentagoMove move : moves) {
            PentagoBoardState cloned = (PentagoBoardState) game.clone();
            cloned.processMove(move);
            scores.add(min_max(depth, false, cloned, tkn, -100000, 100000));
        }

        float max_val = Collections.max(scores);
        System.out.println(scores.toString());

        List<PentagoMove> candidate_moves = new ArrayList<>();

        // Get the move with the best score
        for (int i = 0; i < moves.size(); i++) {
            if (scores.get(i) == max_val) {
                candidate_moves.add(moves.get(i));
            }
        }

        if (candidate_moves.size() == moves.size()) {
            return null;
        }

        PentagoMove best_move = candidate_moves.get(rand.nextInt(candidate_moves.size()));
        return best_move;
    }

    public static float min_max(int depth, boolean isMax, PentagoBoardState game, int tkn, float alpha, float beta) {
        long hashed_game = game_hash(game);
        float position_score = 0;
//        if(t_table.containsKey(hashed_game)){
////            System.out.println("Found match");
//            return t_table.get(hashed_game)*tkn;
//        }

        if ((game.getWinner() != Board.NOBODY)) {
            int winner = game.getWinner();
            if (game.getWinner() == Board.DRAW) {
                position_score = 0;
            } else if (winner == 0) {
                position_score = -(depth + 10) * tkn;
            } else if (winner == 1) {
                position_score = (depth + 10) * tkn;
            }
        } else if (depth == 0) { // If we reach the depth limit, we are pessimistic about the board position
            return -1;

        } else {
            List<PentagoMove> valid_moves = game.getAllLegalMoves();
            // maximizing agent
            if (isMax) {
                float maxScore = -100000;
                float score;
                for (PentagoMove move : valid_moves) {
                    PentagoBoardState cloned = (PentagoBoardState) game.clone();
                    cloned.processMove(move);
                    score = min_max(depth - 1, false, cloned, tkn, alpha, beta);
                    maxScore = Math.max(score, maxScore);
                    alpha = Math.max(alpha, score);
                    if (alpha > beta) {
                        break;
                    }
                }
                position_score = maxScore;
            }
            // minimizing agent
            else {
                float minScore = 100000;
                float score;
                for (PentagoMove move : valid_moves) {
                    PentagoBoardState cloned = (PentagoBoardState) game.clone();
                    cloned.processMove(move);
                    score = min_max(depth - 1, true, cloned, tkn, alpha, beta);
                    minScore = Math.min(score, minScore);
                    beta = Math.min(beta, score);
                    if (beta < alpha) {
                        break;
                    }
                }
                position_score = minScore;
            }
        }
        if (position_score != -1.0){
            t_table.put(hashed_game,tkn*position_score);
        }
        return position_score;
    }

    public static Long game_hash(PentagoBoardState game) {
        long hash = 0;
        int exponent;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                exponent = 6 * i + j;
                hash += Math.pow(game.getPieceAt(i, j).ordinal(), exponent);
            }
        }
        return hash;
    }
}
