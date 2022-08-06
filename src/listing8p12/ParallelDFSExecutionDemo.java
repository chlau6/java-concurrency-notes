package listing8p12;

import dummy.Node;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public class ParallelDFSExecutionDemo {
    public<T> void sequentialRecursive(List<Node<T>> nodes, Collection<T> results) {
        for (Node<T> n : nodes) {
            results.add(n.compute());
            sequentialRecursive(n.getChildren(), results);
        }
    }

    public<T> void parallelRecursive(final Executor exec, List<Node<T>> nodes, final Collection<T> results) {
        for (final Node<T> n : nodes) {
            exec.execute(new Runnable() {
                public void run() {
                    results.add(n.compute());
                }
            });

            parallelRecursive(exec, n.getChildren(), results);
        }
    }

    public<T> Collection<T> getParallelResults(List<Node<T>> nodes) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Queue<T> resultQueue = new ConcurrentLinkedQueue<T>();
        parallelRecursive(exec, nodes, resultQueue);
        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        return resultQueue;
    }
}

/*
When parallelRecursive returns, each node in the tree has been visited (the traversal is still sequential:
only the calls to compute are executed in parallel) and the computation for each node has been queued to the Executor.
Callers of parallelRecursive can wait for all the results by creating an Executor specific to the traversal and
using shutdown and awaitTermination, as shown in Listing 8.12.
 */