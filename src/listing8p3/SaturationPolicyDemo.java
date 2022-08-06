package listing8p3;

import java.util.concurrent.*;

public class SaturationPolicyDemo {
    private static final int N_THREADS = 1000;
    private static final int CAPACITY = 2000;

    public SaturationPolicyDemo() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(N_THREADS, N_THREADS,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(CAPACITY));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    }
}

/*
The caller-runs policy implements a form of throttling that neither discards tasks nor throws an exception,
but instead tries to slow down the flow of new tasks by pushing some of the work back to the caller.
It executes the newly submitted task not in a pool thread, but in the thread that calls execute.
If we modified our WebServer example to use a bounded queue and the caller-runs policy,
after all the pool threads were occupied and
the work queue filled up the next task would be executed in the main thread during the call to execute.
Since this would probably take some time, the main thread cannot submit any more tasks for at least a little while,
giving the worker threads some time to catch up on the backlog.
The main thread would also not be calling accept during this time,
so incoming requests will queue up in the TCP layer instead of in the application.
If the overload persisted, eventually the TCP layer would decide it has queued enough connection requests and
begin discarding connection requests as well.
As the server becomes overloaded, the overload is gradually pushed outward -
from the pool threads to the work queue to the application to the TCP layer,
and eventually to the client - enabling more graceful degradation under load.

Choosing a saturation policy or making other changes to the execution policy can be done when the Executor is created.
Listing 8.3 illustrates creating a fixedsize thread pool with the caller-runs saturation policy.
 */
