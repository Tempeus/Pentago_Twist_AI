package pentago_twist;

import boardgame.Board;
import boardgame.BoardState;
import pentago_twist.PentagoBoardState;

import java.util.ArrayList;
import java.util.List;

public class StateNode {
    public int total_visits;
    public float score;
    public PentagoBoardState game;
    List<StateNode> children;
    public StateNode(PentagoBoardState game){
        this.game = game;
        this.total_visits = 0;
        this.score = 0;
        this.children = new ArrayList<StateNode>();
    }
}
