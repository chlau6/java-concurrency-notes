package listing3p8;

import dummy.Event;
import dummy.EventListener;
import dummy.EventSource;

public class SafeListener {
    private final EventListener listener;
    private SafeListener() {
        listener = new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        };
    }

    /*
    Dummy Implementation
     */
    private void doSomething(Event e) {

    }

    public static SafeListener newInstance(EventSource source) {
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }
}

/*
A common mistake that can let the this reference escape during construction is to start a thread from a constructor.

When an object creates a thread from its constructor, it almost always shares its this reference with the new thread,
either explicitly (by passing it to the constructor) or
implicitly (because the Thread or Runnable is an inner class of the owning object).

The new thread might then be able to see the owning object before it is fully constructed.
There’s nothing wrong with creating a thread in a constructor, but it is best not to start the thread immediately.
Instead, expose a start or initialize method that starts the owned thread.

Calling an overridable instance method (one that is neither private nor final) from the constructor
can also allow the this reference to escape.

If you are tempted to register an event listener or start a thread from a constructor,
you can avoid the improper construction by using a private constructor and a public factory method.
 */
