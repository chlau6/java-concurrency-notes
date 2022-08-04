package listing7p23;

import dummy.Task;

public class StructureWorkerThreadDemo extends Thread {
    public void run() {
        Throwable thrown = null;
        try {
            while (!isInterrupted())
                runTask(getTaskFromWorkQueue());
        } catch (Throwable e) {
            thrown = e;
        } finally {
            threadExited(this, thrown);
        }
    }

    /* Dummy Implementation */
    private void runTask(Task taskFromWorkQueue) {

    }

    /* Dummy Implementation */
    private Task getTaskFromWorkQueue() {
        return new Task();
    }

    /* Dummy Implementation */
    private void threadExited(StructureWorkerThreadDemo structureWorkerThreadDemo, Throwable thrown) {

    }
}

/*
Listing 7.23 illustrates a way to structure a worker thread within a thread pool.
If a task throws an unchecked exception, it allows the thread to die,
but not before notifying the framework that the thread has died.
The framework may then replace the worker thread with a new thread,
or may choose not to because the thread pool is being shut down or
there are already enough worker threads to meet current demand.
ThreadPoolExecutor and Swing use this technique to ensure that a poorly behaved task doesn't
prevent subsequent tasks from executing.
If you are writing a worker thread class that executes submitted tasks,
or calling untrusted external code (such as dynamically loaded plugins),
use one of these approaches to prevent a poorly written task or plugin from
taking down the thread that happens to call it.
 */
