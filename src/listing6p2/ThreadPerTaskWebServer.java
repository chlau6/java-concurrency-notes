package listing6p2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ThreadPerTaskWebServer {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    handleRequest(connection);
                }
            };
            new Thread(task).start();
        }
    }

    /* Dummy Implementation */
    private static void handleRequest(Socket connection) {
    }
}

/*
ThreadPerTaskWebServer is similar in structure to the single-threaded version-
the main thread still alternates between accepting an incoming connection and dispatching the request.

The difference is that for each connection,
the main loop creates a new thread to  process the request instead of processing it within the main thread.
This has three main consequences:

Task processing is offloaded from the main thread,
enabling the main loop to resume waiting for the next incoming connection more quickly.
This enables new connections to be accepted before previous requests complete, improving responsiveness.

Tasks can be processed in parallel, enabling multiple requests to be serviced simultaneously.
This may improve throughput if there are multiple processors,
or if tasks need to block for any reason such as I/O completion, lock acquisition, or resource availability.

Task-handling code must be thread-safe, because it may be invoked concurrently for multiple tasks.

Under light to moderate load, the thread-per-task approach is an improvement over sequential execution.
As long as the request arrival rate does not exceed the server's capacity to handle requests,
this approach offers better responsiveness and throughput.

For production use, however, the thread-per-task approach has some practical drawbacks,
especially when a large number of threads may be created:

Thread lifecycle overhead.
Thread creation and teardown are not free. The actual overhead varies across platforms, but thread creation takes time,
introducing latency into request processing, and requires some processing activity by the JVM and OS.
If requests are frequent and lightweight, as in most server applications,
creating a new thread for each request can consume significant computing resources.

Resource consumption.
Active threads consume system resources, especially memory.
When there are more runnable threads than available processors, threads sit idle.
Having many idle threads can tie up a lot of memory, putting pressure on the garbage collector,
and having many threads competing for the CPUs can impose other performance costs as well.
If you have enough threads to keep all the CPUs busy, creating more threads won't help and may even hurt.

Stability.
There is a limit on how many threads can be created.
The limit varies by platform and is affected by factors including JVM invocation parameters,
the requested stack size in the Thread constructor,
and limits on threads placed by the underlying operating system.
When you hit this limit, the most likely result is an OutOfMemoryError.
Trying to recover from such an error is very risky;
it is far easier to structure your program to avoid hitting this limit.
 */
