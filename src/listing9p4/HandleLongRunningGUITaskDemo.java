package listing9p4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandleLongRunningGUITaskDemo {
    ExecutorService backgroundExec = Executors.newCachedThreadPool();
    final JButton button = new JButton("Change Color");

    public void change() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backgroundExec.execute(new Runnable() {
                    public void run() {
                        doBigComputation();
                    }
                });
            }});
    }

    /* Dummy Implementation */
    private void doBigComputation() {
    }

}

/*
We start with a simple task that does not support cancellation or progress indication and
that does not update the GUI on completion, and then add those features one by one.
Listing 9.4 shows an action listener, bound to a visual component, that submits a long-running task to an Executor.
Despite the two layers of inner classes, having a GUI task initiate a task in this manner is fairly straightforward:
the UI action listener is called in the event thread and submits a Runnable to execute in the thread pool.

This example gets the long-running task out of the event thread in a "fire and forget" manner,
which is probably not very useful. There is usually some sort of visual feedback when a long-running task completes.
But you cannot access presentation objects from the background thread,
so on completion the task must submit another task to run in the event thread to update the user interface.
 */