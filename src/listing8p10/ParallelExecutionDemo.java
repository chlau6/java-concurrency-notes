package listing8p10;

import dummy.Element;

import java.util.List;
import java.util.concurrent.Executor;

public class ParallelExecutionDemo {
    void processSequentially(List<Element> elements) {
        for (Element e : elements)
            process(e);
    }

    void processInParallel(Executor exec, List<Element> elements) {
        for (final Element e : elements)
            exec.execute(new Runnable() {
                public void run() {
                    process(e);
                }
            });
    }

    /* Dummy Implementation */
    private void process(Element e) {
    }
}

/*
TimingThreadPool in Listing 8.9 shows a custom thread pool that uses beforeExecute, afterExecute,
and terminated to add logging and statistics gathering.
To measure a task's runtime, beforeExecute must record the start time and store it somewhere afterExecute can find it.
Because execution hooks are called in the thread that executes the task,
a value placed in a ThreadLocal by beforeExecute can be retrieved by afterExecute.
TimingThreadPool uses a pair of AtomicLongs to keep track of the total number of tasks processed and
the total processing time, and uses the terminated hook to print a log message showing the average task time.

A call to processInParallel returns more quickly than a call to processSequentially because
it returns as soon as all the tasks are queued to the Executor, rather than waiting for them all to complete.
If you want to submit a set of tasks and wait for them all to complete, you can use ExecutorService.invokeAll;
to retrieve the results as they become available, you can use a CompletionService, as in Renderer.
 */