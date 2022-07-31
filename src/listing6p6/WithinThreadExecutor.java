package listing6p6;

import java.util.concurrent.Executor;

public class WithinThreadExecutor implements Executor {
    public void execute(Runnable r) {
        r.run();
    };
}

/*
Similarly, it is also easy to write an Executor that
would make TaskExecutionWebServer behave like the single-threaded version,
executing each task synchronously before returning from execute,
as shown in WithinThreadExecutor in Listing 6.6.
 */
