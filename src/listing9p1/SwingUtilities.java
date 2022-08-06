package listing9p1;

import listing8p13.Puzzle;
import listing8p14.Node;
import listing8p16.ConcurrentPuzzleSolver;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SwingUtilities {
    private static final ExecutorService exec = Executors.newSingleThreadExecutor(new SwingThreadFactory());
    private static volatile Thread swingThread;

    private static class SwingThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            swingThread = new Thread(r);
            return swingThread;
        }
    }

    public static boolean isEventDispatchThread() {
        return Thread.currentThread() == swingThread;
    }

    public static void invokeLater(Runnable task) {
        exec.execute(task);
    }

    public static void invokeAndWait(Runnable task) throws InterruptedException, InvocationTargetException {
        Future f = exec.submit(task);
        try {
            f.get();
        } catch (ExecutionException e) {
            throw new InvocationTargetException(e);
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