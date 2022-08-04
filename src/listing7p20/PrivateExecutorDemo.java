package listing7p20;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrivateExecutorDemo {
    boolean checkMail(Set<String> hosts, long timeout, TimeUnit unit) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        final AtomicBoolean hasNewMail = new AtomicBoolean(false);
        try {
            for (final String host : hosts) {
                exec.execute(new Runnable() {
                    public void run() {
                        if (checkMail(host)) {
                            hasNewMail.set(true);
                        }
                    }
                });
            }
        } finally {
            exec.shutdown();
            exec.awaitTermination(timeout, unit);
        }

        return hasNewMail.get();
    }

    /* Dummy Implementation */
    private boolean checkMail(String host) {
        return true;
    }
}

/*
If a method needs to process a batch of tasks and does not return until all the tasks are finished,
it can simplify service lifecycle management by using a private Executor whose lifetime is bounded by that method.
(The invokeAll and invokeAny methods can often be useful in such situations.)

The checkMail method in Listing 7.20 checks for new mail in parallel on a number of hosts.
It creates a private executor and submits a task for each host:
it then shuts down the executor and waits for termination, which occurs when all the mail-checking tasks have completed.
 */