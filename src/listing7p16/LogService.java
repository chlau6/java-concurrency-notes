package listing7p16;

import dummy.WriteTask;

import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

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
