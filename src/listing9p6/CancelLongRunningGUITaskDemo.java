package listing9p6;

import listing9p2.GuiExecutor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CancelLongRunningGUITaskDemo {
    ExecutorService backgroundExec = Executors.newCachedThreadPool();
    final JButton startButton = new JButton();
    final JButton cancelButton = new JButton();
    Future<?> runningTask = null; // thread-confined

    public void change() {
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (runningTask != null) {
                    runningTask = backgroundExec.submit(new Runnable() {
                        public void run() {
                            while (moreWork()) {
                                if (Thread.currentThread().isInterrupted()) {
                                    cleanUpPartialWork();
                                    break;
                                }
                                doSomeWork();
                            }
                        }

                        /* Dummy Implementation */
                        private boolean moreWork() {
                            return true;
                        }

                        /* Dummy Implementation */
                        private void cleanUpPartialWork() {
                        }

                        /* Dummy Implementation */
                        private void doSomeWork() {
                        }
                    });
                };
            }});

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (runningTask != null)
                    runningTask.cancel(true);
            }});
    }
}

/*
Any task that takes long enough to run in another thread probably also takes long enough that
the user might want to cancel it. You could implement cancellation directly using thread interruption,
but it is much easier to use Future, which was designed to manage cancellable tasks.
When you call cancel on a Future with mayInterruptIfRunning set to true,
the Future implementation interrupts the thread that is executing the task if it is currently running.
If your task is written to be responsive to interruption, it can return early if it is cancelled.
Listing 9.6 illustrates a task that polls the thread's interrupted status and returns early on interruption.

Because runningTask is confined to the event thread, no synchronization is required when setting or checking it,
and the start button listener ensures that only one background task is running at a time.
However, it would be better to be notified when the task completes so that, for example,
the cancel button could be disabled. We address this in the next section.
 */