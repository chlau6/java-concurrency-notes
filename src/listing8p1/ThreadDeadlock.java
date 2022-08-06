package listing8p1;

import dummy.LoadFileTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadDeadlock {
    ExecutorService exec = Executors.newSingleThreadExecutor();

    public class RenderPageTask implements Callable<String> {
        public String call() throws Exception {
            Future<String> header, footer;
            header = exec.submit(new LoadFileTask("header.html"));
            footer = exec.submit(new LoadFileTask("footer.html"));
            String page = renderBody();
            // Will deadlock -- task waiting for result of subtask
            return header.get() + page + footer.get();
        }

        /* Dummy Implementation */
        private String renderBody() {
            return "";
        }
    }
}

/*
ThreadDeadlock in Listing 8.1 illustrates thread starvation deadlock.
RenderPageTask submits two additional tasks to the Executor to fetch the page header and footer, renders the page body,
waits for the results of the header and footer tasks, and then combines the header, body,
and footer into the finished page.
With a single-threaded executor, ThreadDeadlock will always deadlock.
Similarly, tasks coordinating amongst themselves with a barrier could also cause thread starvation deadlock if
the pool is not big enough.
 */
