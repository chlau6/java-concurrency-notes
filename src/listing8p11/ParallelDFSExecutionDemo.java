package listing8p11;

import dummy.Element;
import dummy.Node;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

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
}

/*
Loop parallelization can also be applied to some recursive designs;
there are often sequential loops within the recursive algorithm that
can be parallelized in the same manner as Listing 8.10.
The easier case is when each iteration does not require the results of the recursive iterations it invokes.
For example, sequentialRecursive in Listing 8.11 does a depth-first traversal of a tree,
performing a calculation on each node and placing the result in a collection.
The transformed version, parallelRecursive, also does a depth-first traversal,
but instead of computing the result as each node is visited, it submits a task to compute the node result.
 */