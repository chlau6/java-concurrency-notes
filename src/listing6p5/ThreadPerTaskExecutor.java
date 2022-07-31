package listing6p5;

import java.util.concurrent.Executor;

public class ThreadPerTaskExecutor implements Executor {
    public void execute(Runnable r) {
        new Thread(r).start();
    };
}

/*
We can easily modify TaskExecutionWebServer to behave like ThreadPerTaskWebServer by
substituting an Executor that creates a new thread for each request.
Writing such an Executor is trivial, as shown in ThreadPerTaskExecutor in Listing 6.5.
 */
