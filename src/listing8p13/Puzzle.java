package listing8p13;

import dummy.Node;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

public interface Puzzle<P, M> {
    P initialPosition();

    boolean isGoal(P position);

    Set<M> legalMoves(P position);

    P move(P position, M move);
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