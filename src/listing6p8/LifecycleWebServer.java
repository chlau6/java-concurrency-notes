package listing6p8;

import dummy.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

class LifecycleWebServer {
    private static final int NTHREADS = 100;
    private final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);

    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!exec.isShutdown()) {
            try {
                final Socket conn = socket.accept();
                exec.execute(new Runnable() {
                    public void run() {
                        handleRequest(conn);
                    }
                });
            } catch (RejectedExecutionException e) {
                if (!exec.isShutdown()) {
                    log("task submission rejected", e);
                }
            }
        }
    }

    public void stop() {
        exec.shutdown();
    }

    void handleRequest(Socket connection) {
        Request req = readRequest(connection);
        if (isShutdownRequest(req)) {
            stop();
        } else {
            dispatchRequest(req);
        }
    }

    /* Dummy Implementation */
    private void log(String taskSubmissionRejected, RejectedExecutionException e) {
    }

    /* Dummy Implementation */
    private Request readRequest(Socket connection) {
        return new Request();
    }

    /* Dummy Implementation */
    private boolean isShutdownRequest(Request req) {
        return true;
    }

    /* Dummy Implementation */
    private void dispatchRequest(Request req) {
    }
}

/*
LifecycleWebServer in Listing 6.8 extends our web server with lifecycle support.
It can be shut down in two ways:
programmatically by calling stop,
and through a client request by sending the web server a specially formatted HTTP request.
 */
