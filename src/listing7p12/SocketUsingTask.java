package listing7p12;

import annotation.GuardedBy;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public abstract class SocketUsingTask<T> implements CancellableTask<T> {
    @GuardedBy("this")
    private Socket socket;

    protected synchronized void setSocket(Socket s) {
        socket = s;
    }

    public synchronized void cancel() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {

        }
    }

    public RunnableFuture<T> newTask() {
        return new FutureTask<T>(this) {
            public boolean cancel(boolean mayInterruptIfRunning) {
                try {
                    SocketUsingTask.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }
}

/*
SocketUsingTask implements CancellableTask and defines Future.cancel to close the socket as well as call super.cancel.
If a SocketUsingTask is cancelled through its Future, the socket is closed and the executing thread is interrupted.
This increases the task’s responsiveness to cancellation:
not only can it safely call interruptible blocking methods while remaining responsive to cancellation,
but it can also call blocking socket I/O methods.
 */