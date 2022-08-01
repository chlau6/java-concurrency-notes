package listing7p6;

import dummy.Task;

import java.util.concurrent.BlockingQueue;

public class PropagateInterruptedExceptionDemo {
    BlockingQueue<Task> queue;

    public Task getNextTask() throws InterruptedException {
        return queue.take();
    }
}

/*
when you call an interruptible blocking method such as Thread.sleep or BlockingQueue.put,
there are two practical strategies for handling InterruptedException:

Propagate the exception (possibly after some task-specific cleanup),
making your method an interruptible blocking method, too; or

Restore the interruption status so that code higher up on the call stack can deal with it.

Propagating InterruptedException can be as easy as adding InterruptedException to the throws clause,
as shown by getNextTask in Listing 7.6.
 */