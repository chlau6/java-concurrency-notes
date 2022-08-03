package listing7p14;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogService {
    private boolean shutdownRequested = false;
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public void log(String msg) throws InterruptedException {
        if (!shutdownRequested) {
            queue.put(msg);
        } else {
            throw new IllegalStateException("logger is shut down");
        }
    }
}

/*
Another approach to shutting down LogWriter would be to set a "shutdown requested" flag to
prevent further messages from being submitted, as shown in Listing 7.14.
The consumer could then drain the queue upon being notified that shutdown has been requested,
writing out any pending messages and unblocking any producers blocked in log.
However, this approach has race conditions that make it unreliable.
The implementation of log is a check-then-act sequence:
producers could observe that the service has not yet been shut down but still queue messages after the shutdown,
again with the risk that the producer might get blocked in log and never become unblocked.
There are tricks that reduce the likelihood of this
(like having the consumer wait several seconds before declaring the queue drained),
but these do not change the fundamental problem, merely the likelihood that it will cause a failure.
 */
