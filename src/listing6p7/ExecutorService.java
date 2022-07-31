package listing6p7;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public interface ExecutorService extends Executor {
    void shutdown();

    List<Runnable> shutdownNow();

    boolean isShutdown();

    boolean isTerminated();

    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

    // ... additional convenience methods for task submission
}

/*
To address the issue of execution service lifecycle, the ExecutorService interface extends Executor,
adding a number of methods for lifecycle management (as well as some convenience methods for task submission).
The lifecycle management methods of ExecutorService are shown in Listing 6.7.

The lifecycle implied by ExecutorService has three states-running, shutting down, and terminated.
ExecutorServices are initially created in the running state.
The shutdown method initiates a graceful shutdown:
no new tasks are accepted but previously submitted tasks are allowed to complete-including those that
have not yet begun execution.

The shutdownNow method initiates an abrupt shutdown:
it attempts to cancel outstanding tasks and does not start any tasks that are queued but not begun.
 */