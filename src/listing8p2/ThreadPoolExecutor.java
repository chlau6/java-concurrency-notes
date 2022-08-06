package listing8p2;

import java.util.concurrent.*;

public class ThreadPoolExecutor {
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {

    }
}

/*
If the default execution policy does not meet your needs,
you can instantiate a ThreadPoolExecutor through its constructor and customize it as you see fit;
you can consult the source code for Executors to see the execution policies for the default configurations and
use them as a starting point.
ThreadPoolExecutor has several constructors, the most general of which is shown in Listing 8.2.
 */
