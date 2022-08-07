package listing9p8;

import listing9p7.BackgroundTask;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

public class CancelLongRunningGUITaskDemo {
    ExecutorService backgroundExec = Executors.newCachedThreadPool();
    final JButton startButton = new JButton();
    final JButton cancelButton = new JButton();
    final JLabel label = new JLabel();

    public void runInBackground(final Runnable task) {
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                class CancelListener implements ActionListener {
                    BackgroundTask<?> task;
                    public void actionPerformed(ActionEvent event) {
                        if (task != null) {
                            task.cancel(true);
                        }
                    }
                }

                final CancelListener listener = new CancelListener();
                listener.task = new BackgroundTask<Void>() {
                    public Void compute() {
                        while (moreWork() && !isCancelled()) {
                            doSomeWork();
                        }
                        return null;
                    }

                    public void onCompletion(boolean cancelled, String s, Throwable exception) {
                        cancelButton.removeActionListener(listener);
                        label.setText("done");
                    }

                    /* Dummy Implementation */
                    private boolean moreWork() {
                        return true;
                    }

                    /* Dummy Implementation */
                    private void doSomeWork() {
                    }

                    @Override
                    public boolean cancel(boolean mayInterruptIfRunning) {
                        return false;
                    }

                    @Override
                    public boolean isCancelled() {
                        return false;
                    }

                    @Override
                    public boolean isDone() {
                        return false;
                    }

                    @Override
                    public Void get() throws InterruptedException, ExecutionException {
                        return null;
                    }

                    @Override
                    public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                        return null;
                    }

                    @Override
                    public void run() {

                    }
                };

                cancelButton.addActionListener(listener);
                backgroundExec.execute(task);
            }
        });
    }
}

/*
Basing BackgroundTask on FutureTask also simplifies cancellation.
Rather than having to poll the thread's interrupted status, compute can call Future.isCancelled.
Listing 9.8 recasts the example from Listing 9.6 using BackgroundTask.
 */