package pentago_twist;

import boardgame.Board;
import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class Monte_Carlo {
    static Hashtable<Long, Float> t_table = new Hashtable<>();

    public Monte_Carlo() {

    }

    public static Move monte_carlo_helper(PentagoBoardState game, int iterations) {
        StateNode root = new StateNode(game);
        ArrayList<PentagoMove> moves = root.game.getAllLegalMoves();
        ArrayList<StateNode> childNodes = new ArrayList<>();
        for (PentagoMove move : moves) {
            PentagoBoardState cloned = (PentagoBoardState) root.game.clone();
            cloned.processMove(move);
            childNodes.add(new StateNode(cloned));
        }
        root.children = childNodes;

        int player = root.game.getTurnPlayer();
        int tkn;
        if (player == 0){
            tkn = -1;
        }else{
            tkn = 1;
        }
        for (int i = 0; i < iterations; i++) {
            monte_carlo(root, tkn);
        }

        float max_val = -1000;
        float child_score;

        // Get the move with the best score
        PentagoMove best_move = moves.get(0);
        for (int i = 0; i < moves.size(); i++) {
            child_score = childNodes.get(i).score;
            if (child_score > max_val) {
                max_val = child_score;
                best_move = moves.get(i);
            }
            System.out.print(" Move:" + moves.get(i).toString() + " Value: " + Float.toString(child_score));
        }
        System.out.println("");
        System.out.println(max_val);

        return best_move;
    }

    private static float monte_carlo(StateNode node, int token) {
        Random rand = new Random();
        float node_val = 0;
        // not a leaf node
        if (node.children.size() != 0) {

            // Getting the uct values for each child
            List<Double> child_values = ucb1(node.children, node);

            // finding the max value
            double max_val = -10000;
            for (int i = 0; i <child_values.size(); i++) {
                max_val = Math.max(child_values.get(i), max_val);
            }

            // getting all the nodes with maximal values
            List<StateNode> max_nodes = new ArrayList<>();
            for(int i =0; i < child_values.size(); i++){
                if(child_values.get(i) == max_val){
                    max_nodes.add(node.children.get(i));
                }
            }

            StateNode max_child;
            max_child = max_nodes.get(rand.nextInt(max_nodes.size()));
            node_val = monte_carlo(max_child, token);

        }// Terminal leaf node
        else if (node.game.gameOver()) {
            int winner = node.game.getWinner();
            if (node.game.getWinner() == Board.DRAW) {
                return 0;
            } else if (winner == 0) {
                node_val = -1000 * token;
            } else if (winner == 1) {
                node_val = 1000 * token;
            }

        }// unvisited leaf node
        else if (node.total_visits == 0) {
            int val = 0;
            int num_rollouts = 3;
            for (int i = 0; i < num_rollouts; i++) {
                val += rollout(node.game, token);
            }
            node_val = val;

        } // visited leaf node
        else {
            ArrayList<StateNode> childNodes = new ArrayList<>();
            for (PentagoMove move : node.game.getAllLegalMoves()) {
                PentagoBoardState cloned = (PentagoBoardState) node.game.clone();
                cloned.processMove(move);
                childNodes.add(new StateNode(cloned));
            }
            node.children = childNodes;
            StateNode child = childNodes.get(rand.nextInt(childNodes.size()));
            node_val = monte_carlo(child, token);
        }

        node.score += node_val;
        node.total_visits += 1;
        return node_val;
    }

    public static int rollout(PentagoBoardState game, int token) {
        PentagoBoardState clone = (PentagoBoardState) game.clone();
        Random rand = new Random();
        Move random_move;
        while (true) {
            int winner = clone.getWinner();
            if (clone.getWinner() == Board.DRAW) {
                return 0;
            } else if (winner == 0) {
                return -1 * token;
            } else if (winner == 1) {
                return 1 * token;
            } else {
                ArrayList<PentagoMove> moves = clone.getAllLegalMoves();
                random_move = moves.get(rand.nextInt(moves.size()));
                clone.processMove((PentagoMove) random_move);
            }
        }
    }

    private static List<Double> ucb1(List<StateNode> children, StateNode parent) {
        List<Double> values = new ArrayList<>();
        double exploration_weight = 0.5;

        for (StateNode child : children) {
            if (child.total_visits == 0) {
                values.add(Double.MAX_VALUE);
            } else {
                values.add(((double) child.score) + 400 * Math.sqrt(Math.log(((double) parent.total_visits) / child.total_visits)));
            }
        }
        return values;
    }
}

