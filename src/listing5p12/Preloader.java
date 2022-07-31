package listing5p12;

import dummy.DataLoadException;
import dummy.ProductInfo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static listing5p13.Listing5P13.launderThrowable;

public class Preloader {
    private final FutureTask<ProductInfo> future = new FutureTask<ProductInfo>(
            new Callable<ProductInfo>() {
                public ProductInfo call() throws DataLoadException {
                    return loadProductInfo();
                }
            });

    private final Thread thread = new Thread(future);
    public void start() {
        thread.start();
    }

    public ProductInfo get() throws DataLoadException, InterruptedException {
        try {
            return future.get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof DataLoadException) {
                throw (DataLoadException) cause;
            } else {
                throw launderThrowable(cause);
            }
        }
    }

    /*
    Dummy Implementation
     */
    private ProductInfo loadProductInfo() throws DataLoadException {
        return new ProductInfo();
    }
}

/*
Preloader creates a FutureTask that describes the task of loading product information from a database and
a thread in which the computation will be performed.

It provides a start method to start the thread,
since it is inadvisable to start a thread from a constructor or static initializer.

When the program later needs the ProductInfo, it can call get, which returns the loaded data if it is ready,
or waits for the load to complete if not.

Tasks described by Callable can throw checked and unchecked exceptions, and any code can throw an Error.
Whatever the task code may throw, it is wrapped in an ExecutionException and rethrown from Future.get.
This complicates code that calls get, not only because it must deal with the possibility of
ExecutionException (and the unchecked CancellationException),
but also because the cause of the ExecutionException is returned as a Throwable, which is inconvenient to deal with.
 */
