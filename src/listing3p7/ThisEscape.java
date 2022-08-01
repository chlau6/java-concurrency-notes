package listing3p7;

import dummy.Event;
import dummy.EventListener;
import dummy.EventSource;

public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        });
    }

    /* Dummy Implementation */
    private void doSomething(Event e) {

    }
}

/*
A final mechanism by which an object or its internal state can be published is to publish an inner class instance,
as shown in ThisEscape in Listing 3.7.

When ThisEscape publishes the EventListener, it implicitly publishes the enclosing ThisEscape instance as well,
because inner class instances contain a hidden reference to the enclosing instance.
 */