package listing8p14;

import annotation.Immutable;

import java.util.LinkedList;
import java.util.List;

@Immutable
public class Node<P, M> {
    public final P pos;
    public final M move;
    public final Node<P, M> prev;

    public Node(P pos, M move, Node<P, M> prev) {
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }

    public List<M> asMoveList() {
        List<M> solution = new LinkedList<M>();

        for (Node<P, M> n = this; n.move != null; n = n.prev) {
            solution.add(0, n.move);
        }

        return solution;
    }
}

/*
We define a "puzzle" as a combination of an initial position, a goal position,
and a set of rules that determine valid moves.
The rule set has two parts:
computing the list of legal moves from a given position and computing the result of applying a move to a position.
Puzzle in Listing 8.13 shows our puzzle abstraction;
the type parameters P and M represent the classes for a position and a move.
From this interface, we can write a simple sequential solver that searches the puzzle space until
a solution is found or the puzzle space is exhausted.
 */