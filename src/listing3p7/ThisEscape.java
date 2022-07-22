package listing3p7;

public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {
            public void onEvent(Event e) {
                doSomething(e);
            }
        });
    }

    /*
    Dummy Implementation
     */
    private void doSomething(Event e) {

    }
}

class EventSource {
    public void registerListener(EventListener eventListener) {

    }
}

class EventListener {
    public void onEvent(Event e) {
    }
}

class Event {
}

/*
A final mechanism by which an object or its internal state can be published is to publish an inner class instance.

When ThisEscape publishes the EventListener, it implicitly publishes the enclosing ThisEscape instance as well,
because inner class instances contain a hidden reference to the enclosing instance.
 */