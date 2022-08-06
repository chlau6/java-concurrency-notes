package listing8p4;

import annotation.ThreadSafe;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;

@ThreadSafe
public class BoundedExecutor {
    private final Executor exec;
    private final Semaphore semaphore;

    public BoundedExecutor(Executor exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    public void submitTask(final Runnable command) throws InterruptedException {
        semaphore.acquire();

        try {
            exec.execute(new Runnable() {
                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
        }
    }
}

/*
There is no predefined saturation policy to make execute block when the work queue is full.
However, the same effect can be accomplished by using a Semaphore to bound the task injection rate,
as shown in BoundedExecutor in Listing 8.4.
In such an approach, use an unbounded queue (there's no reason to bound both the queue size and the injection rate) and
set the bound on the semaphore to be equal to the pool size plus the number of queued tasks you want to allow,
since the semaphore is bounding the number of tasks both currently executing and awaiting execution.
*/
