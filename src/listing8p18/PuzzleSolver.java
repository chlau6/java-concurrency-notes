package listing8p18;

import listing8p13.Puzzle;
import listing8p14.Node;
import listing8p15.SequentialPuzzleSolver;
import listing8p16.ConcurrentPuzzleSolver;
import listing8p17.ValueLatch;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class PuzzleSolver<P,M> extends ConcurrentPuzzleSolver<P,M> {
    private final AtomicInteger taskCount = new AtomicInteger(0);

    public PuzzleSolver(Puzzle<P, M> puzzle, ExecutorService exec, ConcurrentMap<P, Boolean> seen) {
        super(puzzle, exec, seen);
    }

    protected Runnable newTask(P p, M m, Node<P,M> n) {
        return new CountingSolverTask(p, m, n);
    }

    class CountingSolverTask extends SolverTask {
        CountingSolverTask(P pos, M move, Node<P, M> prev) {
            super(pos, move, prev);
            taskCount.incrementAndGet();
        }

        public void run() {
            try {
                super.run();
            } finally {
                if (taskCount.decrementAndGet() == 0)
                    solution.setValue(null);
            }
        }
    }
}

/*
ConcurrentPuzzleSolver does not deal well with the case where there is no solution:
if all possible moves and positions have been evaluated and no solution has been found,
solve waits forever in the call to getSolution.
The sequential version terminated when it had exhausted the search space,
but getting concurrent programs to terminate can sometimes be more difficult.
One possible solution is to keep a count of active solver tasks and
set the solution to null when the count drops to zero, as in Listing 8.18.
 */