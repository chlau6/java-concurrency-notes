package listing9p5;

import listing9p2.GuiExecutor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandleLongRunningGUITaskDemo {
    ExecutorService backgroundExec = Executors.newCachedThreadPool();
    final JButton button = new JButton("Change Color");
    final JLabel label = new JLabel();

    public void change() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(false);
                label.setText("busy");

                backgroundExec.execute(new Runnable() {
                    public void run() {
                        try {
                            doBigComputation();
                        } finally {
                            GuiExecutor.instance().execute(new Runnable() {
                                public void run() {
                                    button.setEnabled(true);
                                    label.setText("idle");
                                }
                            });
                        }
                    }

                    /* Dummy Implementation */
                    private void doBigComputation() {
                    }
                });
            }
        });
    }
}

/*
Listing 9.5 illustrates the obvious way to do this, which is starting to get complicated;
we're now up to three layers of inner classes.
The action listener first dims the button and sets a label indicating that a computation is in progress,
then submits a task to the background executor.
When that task finishes, it queues another task to run in the event thread,
which reenables the button and restores the label text.

The task triggered when the button is pressed is composed of three sequential subtasks whose
execution alternates between the event thread and the background thread.
The first subtask updates the user interface to show that a long-running operation has begun and
starts the second subtask in a background thread.
Upon completion, the second subtask queues the third subtask to run again in the event thread,
which updates the user interface to reflect that the operation has completed.
This sort of "thread hopping" is typical of handling long-running tasks in GUI applications.
 */