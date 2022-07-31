package listing6p12;

import java.util.List;
import java.util.concurrent.*;

public class ExecutorService extends AbstractExecutorService {
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> task) {
        return new FutureTask<T>(task);
    }

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

    @Override
    public void execute(Runnable command) {

    }
}

/*
There are several ways to create a Future to describe a task.
The submit methods in ExecutorService all return a Future,
so that you can submit a Runnable or a Callable to an executor and
get back a Future that can be used to retrieve the result or cancel the task.
You can also explicitly instantiate a FutureTask for a given Runnable or Callable.
(Because FutureTask implements Runnable,
it can be submitted to an Executor for execution or executed directly by calling its run method.)

As of Java 6, ExecutorService implementations can override newTaskFor in AbstractExecutorService to
control instantiation of the Future corresponding to a submitted Callable or Runnable.
The default implementation just creates a new FutureTask, as shown in Listing 6.12.
 */
