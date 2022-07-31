package listing6p14;

import java.util.concurrent.*;

public class ExecutorCompletionService<V> implements CompletionService<V> {
    private final Executor executor;
    private final AbstractExecutorService aes;
    private final BlockingQueue<Future<V>> completionQueue;

    public ExecutorCompletionService(Executor executor) {
        if (executor == null)
            throw new NullPointerException();
        this.executor = executor;
        this.aes = (executor instanceof AbstractExecutorService) ? (AbstractExecutorService) executor : null;
        this.completionQueue = new LinkedBlockingQueue<>();
    }

    public ExecutorCompletionService(Executor executor, BlockingQueue<Future<V>> completionQueue) {
        if (executor == null || completionQueue == null)
            throw new NullPointerException();
        this.executor = executor;
        this.aes = (executor instanceof AbstractExecutorService) ? (AbstractExecutorService) executor : null;
        this.completionQueue = completionQueue;
    }
    @Override
    public Future<V> submit(Callable task) {
        return null;
    }

    @Override
    public Future<V> submit(Runnable task, Object result) {
        return null;
    }

    @Override
    public Future<V> take() throws InterruptedException {
        return null;
    }

    @Override
    public Future<V> poll() {
        return null;
    }

    @Override
    public Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    private class QueueingFuture extends FutureTask<Void> {
        private final Future<V> task;

        QueueingFuture(RunnableFuture<V> task) {
            super(task, null);
            this.task = task;
        }

        protected void done() {
            completionQueue.add(task);
        }
    }
}

/*
CompletionService combines the functionality of an Executor and a BlockingQueue.
You can submit Callable tasks to it for execution and use the queuelike methods take and
poll to retrieve completed results, packaged as Futures, as they become available.
ExecutorCompletionService implements CompletionService, delegating the computation to an Executor.

The implementation of ExecutorCompletionService is quite straightforward.
The constructor creates a BlockingQueue to hold the completed results.
FutureTask has a done method that is called when the computation completes.
When a task is submitted, it is wrapped with a QueueingFuture,
a subclass of FutureTask that overrides done to place the result on the BlockingQueue,
as shown in Listing 6.14.
The take and poll methods delegate to the BlockingQueue, blocking if results are not yet available.
 */
