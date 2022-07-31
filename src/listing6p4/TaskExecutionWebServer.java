package listing6p4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class TaskExecutionWebServer {
    private static final int NTHREADS = 100;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }
            };
            exec.execute(task);
        }
    }

    /*
    Dummy Implementation
     */
    private static void handleRequest(Socket connection) {
    }
}

/*
Building a web server with an Executor is easy.
TaskExecutionWebServer in Listing 6.4 replaces the hard-coded thread creation with an Executor.
In this case, we use one of the standard Executor implementations, a fixed-size thread pool with 100 threads.

In TaskExecutionWebServer, submission of the request-handling task is decoupled from its execution using an Executor,
and its behavior can be changed merely by substituting a different Executor implementation.
Changing Executor implementations or configuration is far less invasive than changing the way tasks are submitted;
Executor configuration is generally a one-time event and can easily be exposed for deployment-time configuration,
whereas task submission code tends to be strewn throughout the program and harder to expose.
 */
