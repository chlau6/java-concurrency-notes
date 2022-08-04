package listing7p26;

import dummy.WriteTask;

import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class LogService {
    private static final long TIMEOUT = 60;
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    private final ExecutorService exec = newSingleThreadExecutor();
    private final PrintWriter writer;

    public LogService(PrintWriter writer) {
        this.writer = writer;
    }

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    LogService.this.stop();
                } catch (InterruptedException ignored) {

                }
            }
        });
    }

    public void stop() throws InterruptedException {
        try {
            exec.shutdown();
            exec.awaitTermination(TIMEOUT, UNIT);
        } finally {
            writer.close();
        }
    }

    public void log(String msg) {
        try {
            exec.execute(new WriteTask(msg));
        } catch (RejectedExecutionException ignored) {

        }
    }

}

/*
Shutdown hooks can be used for service or application cleanup,
such as deleting temporary files or cleaning up resources that are not automatically cleaned up by the OS.
Listing 7.26 shows how LogService in Listing 7.16 could register a shutdown hook from its start method to
ensure the log file is closed on exit.
 */
