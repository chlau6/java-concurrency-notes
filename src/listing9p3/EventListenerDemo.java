package listing9p3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class EventListenerDemo {
    final Random random = new Random();
    final JButton button = new JButton("Change Color");

    void changeColor() {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button.setBackground(new Color(random.nextInt()));
            }
        });
    }
}

/*
In the simple case, confining presentation objects to the event thread is completely natural.
Listing 9.3 creates a button whose color changes randomly when pressed. When the user clicks on the button,
the toolkit delivers an ActionEvent in the event thread to all registered action listeners.
In response, the action listener picks a new color and changes the button's background color.
So the event originates in the GUI toolkit and is delivered to the application,
and the application modifies the GUI in response to the user's action. Control never has to leave the event thread.
 */