package listing7p12;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

public interface CancellableTask<T> extends Callable<T> {
    void cancel();

    RunnableFuture<T> newTask();
}

/*
CancellableTask in Listing 7.12 defines a CancellableTask interface that extends Callable and
adds a cancel method and a newTask factory method for constructing a RunnableFuture.
 */
