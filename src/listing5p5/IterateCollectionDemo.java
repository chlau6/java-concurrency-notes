package listing5p5;

import dummy.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IterateCollectionDemo {
    List<Widget> widgetList = Collections.synchronizedList(new ArrayList<Widget>());

    public void iterate() {
        // May throw ConcurrentModificationException
        for (Widget w : widgetList) {
            doSomething(w);
        }
    }

    /* Dummy Implementation */
    private void doSomething(Widget w) {

    }
}

/*
Listing 5.5 illustrates iterating a collection with the for-each loop syntax.
Internally, javac generates code that uses an Iterator, repeatedly calling hasNext and next to iterate the List.

Just as with iterating the Vector,
the way to prevent ConcurrentModificationException is to hold the collection lock for the duration of the iteration.
 */
