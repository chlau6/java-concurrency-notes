package listing8p15;

import listing8p13.Puzzle;
import listing8p14.Node;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SequentialPuzzleSolver<P, M> {
    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<P>();

    public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    public List<M> solve() {
        P pos = puzzle.initialPosition();

        return search(new Node<P, M>(pos, null, null));
    }

    private List<M> search(Node<P, M> node) {
        if (!seen.contains(node.pos)) {
            seen.add(node.pos);

            if (puzzle.isGoal(node.pos)) {
                return node.asMoveList();
            }

            for (M move : puzzle.legalMoves(node.pos)) {
                P pos = puzzle.move(node.pos, move);
                Node<P, M> child = new Node<P, M>(pos, move, node);
                List<M> result = search(child);
                if (result != null)
                    return result;
            }
        }

        return null;
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