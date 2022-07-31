package listing5p10;

import dummy.Task;

import java.util.concurrent.BlockingQueue;

public class TaskRunnable implements Runnable {
    BlockingQueue<Task> queue;

    public void run() {
        try {
            processTask(queue.take());
        } catch (InterruptedException e) {
            // restore interrupted status
            Thread.currentThread().interrupt();
        }
    }

    /*
    Dummy Implementation
     */
    private void processTask(Task task) throws InterruptedException {

    }
}

/*
When your code calls a method that throws InterruptedException, then your method is a blocking method too,
and must have a plan for responding to interruption.

For library code, there are basically two choices:

Propagate the InterruptedException.
This is often the most sensible policy if you can get away with it-
just propagate the InterruptedException to your caller.
This could involve not catching InterruptedException,
or catching it and throwing it again after performing some brief activity-specific cleanup.

Restore the interrupt.
Sometimes you cannot throw InterruptedException, for instance when your code is part of a Runnable.
In these situations, you must catch InterruptedException and
restore the interrupted status by calling interrupt on the current thread,
so that code higher up the call stack can see that an interrupt was issued, as demonstrated in Listing 5.10.
 */
