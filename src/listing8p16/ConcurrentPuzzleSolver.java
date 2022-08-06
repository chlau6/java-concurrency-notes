package listing8p16;

import annotation.Immutable;
import listing8p13.Puzzle;
import listing8p14.Node;
import listing8p17.ValueLatch;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

public class ConcurrentPuzzleSolver<P, M> {
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;
    public final ValueLatch<Node<P, M>> solution = new ValueLatch<Node<P, M>>();

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle, ExecutorService exec, ConcurrentMap<P, Boolean> seen) {
        this.puzzle = puzzle;
        this.exec = exec;
        this.seen = seen;
    }

    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            // block until solution found
            Node<P, M> solnNode = solution.getValue();
            return (solnNode == null) ? null : solnNode.asMoveList();
        } finally {
            exec.shutdown();
        }
    }

    protected Runnable newTask(P p, M m, Node<P,M> n) {
        return new SolverTask(p, m, n);
    }

    public class SolverTask extends Node<P, M> implements Runnable {
        public SolverTask(P pos, M move, Node<P, M> prev) {
            super(pos, move, prev);
        }

        public void run() {
            if (solution.isSet() || seen.putIfAbsent(pos, true) != null) {
                return; // already solved or seen this position
            } else if (puzzle.isGoal(pos)) {
                solution.setValue(this);
            } else {
                for (M m : puzzle.legalMoves(pos)) {
                    exec.execute(newTask(puzzle.move(pos, m), m, this));
                }
            }
        }
    }
}

/*
ConcurrentPuzzleSolver in Listing 8.16 uses an inner SolverTask class that extends Node and implements Runnable.
Most of the work is done in run: evaluating the set of possible next positions, pruning positions already searched,
evaluating whether success has yet been achieved (by this task or by some other task),
and submitting unsearched positions to an Executor.

To avoid infinite loops, the sequential version maintained a Set of previously searched positions;
ConcurrentPuzzleSolver uses a ConcurrentHashMap for this purpose.
This provides thread safety and avoids the race condition inherent in conditionally updating a shared collection by
using putIfAbsent to atomically.

The concurrent approach also trades one form of limitation for another that
might be more suitable to the problem domain.
The sequential version performs a depth-first search, so the search is bounded by the available stack size.
The concurrent version performs a breadth-first search and  is therefore free of the stack size restriction
(but can still run out of memory if
the set of positions to be searched or already searched exceeds the available memory).
 */