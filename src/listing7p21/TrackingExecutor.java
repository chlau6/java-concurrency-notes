package listing7p21;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class TrackingExecutor extends AbstractExecutorService {
    private final ExecutorService exec;
    private final Set<Runnable> tasksCancelledAtShutdown = Collections.synchronizedSet(new HashSet<Runnable>());

    public TrackingExecutor(ExecutorService exec) {
        this.exec = exec;
    }

    public List<Runnable> getCancelledTasks() {
        if (!exec.isTerminated()) throw new IllegalStateException();
        return new ArrayList<Runnable>(tasksCancelledAtShutdown);
    }
    public void execute(final Runnable runnable) {
        exec.execute(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } finally {
                    if (isShutdown() && Thread.currentThread().isInterrupted()) {
                        tasksCancelledAtShutdown.add(runnable);
                    }
                }
            }
        });
    }

    // delegate other ExecutorService methods to exec
    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }
}

/*
TrackingExecutor in Listing 7.21 shows a technique for determining which tasks were in progress at shutdown time.
By encapsulating an ExecutorService and instrumenting execute (and similarly submit, not shown) to remember
which tasks were cancelled after shutdown,
TrackingExecutor can identify which tasks started but did not complete normally.
After the executor terminates, getCancelledTasks returns the list of cancelled tasks.
In order for this technique to work, the tasks must preserve the thread’s interrupted status when they return,
which well behaved tasks will do anyway.
 */