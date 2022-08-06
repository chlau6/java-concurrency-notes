package listing9p7;

import listing9p2.GuiExecutor;

import java.util.concurrent.*;

public abstract class BackgroundTask<V> implements Runnable, Future<V> {
    private final FutureTask<V> computation = new Computation();

    private class Computation extends FutureTask<V> {
        public Computation() {
            super(new Callable<V>() {
                public V call() throws Exception {
                    return BackgroundTask.this.compute();
                }
            });
        }

        protected final void done() {
            GuiExecutor.instance().execute(new Runnable() {
                public void run() {
                    V value = null;
                    Throwable thrown = null;
                    boolean cancelled = false;
                    try {
                        value = get();
                    } catch (ExecutionException e) {
                        thrown = e.getCause();
                    } catch (CancellationException e) {
                        cancelled = true;
                    } catch (InterruptedException consumed) {
                    } finally {
                        onCompletion(value, thrown, cancelled);
                    }
                };
            });
        }
    }

    protected void setProgress(final int current, final int max) {
        GuiExecutor.instance().execute(new Runnable() {
            public void run() {
                onProgress(current, max);
            }
        });
    }

    // Called in the background thread
    protected abstract V compute() throws Exception;

    // Called in the event thread
    protected void onCompletion(V result, Throwable exception, boolean cancelled) {

    }

    protected void onProgress(int current, int max) {

    }
    // Other Future methods forwarded to computation
}

/*
Using a Future to represent a long-running task greatly simplified implementing cancellation.
FutureTask also has a done hook that similarly facilitates completion notification.
After the background Callable completes, done is called. By having done trigger a completion task in the event thread,
we can construct a BackgroundTask class providing an onCompletion hook that is called in the event thread,
as shown in Listing 9.7.

BackgroundTask also supports progress indication. The compute method can call setProgress,
indicating progress in numerical terms. This causes onProgress to be called from the event thread,
which can update the user interface to indicate progress visually.

To implement a BackgroundTask you need only implement compute, which is called in the background thread.
You also have the option of overriding onCompletion and onProgress, which are invoked in the event thread.
 */