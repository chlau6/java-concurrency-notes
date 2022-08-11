package listing10p5;

import annotation.GuardedBy;
import dummy.Account;
import dummy.DollarAmount;
import dummy.Image;
import dummy.Point;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TaxicabDispatchingDemo {
    class Taxi {
        @GuardedBy("this")
        private Point location, destination;

        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setLocation(Point location) {
            this.location = location;

            if (location.equals(destination)) {
                dispatcher.notifyAvailable(this);
            }
        }
    }

    class Dispatcher {
        @GuardedBy("this")
        private final Set<Taxi> taxis;

        @GuardedBy("this")
        private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public synchronized Image getImage() {
            Image image = new Image();
            for (Taxi t : taxis) {
                image.drawMarker(t.getLocation());
            }

            return image;
        }
    }
}

/*
Multiple lock acquisition is not always as obvious as in LeftRightDeadlock or transferMoney;
the two locks need not be acquired by the same method.
Consider the cooperating classes in Listing 10.5, which might be used in a taxicab dispatching application.
Taxi represents an individual taxi with a location and a destination; Dispatcher represents a fleet of taxis.

While no method explicitly acquires two locks, callers of setLocation and getImage can acquire two locks just the same.
If a thread calls setLocation in response to an update from a GPS receiver,
it first updates the taxi's location and then checks to see if it has reached its destination.
If it has, it informs the dispatcher that it needs a new destination.
Since both setLocation and notifyAvailable are synchronized,
the thread calling setLocation acquires the Taxi lock and then the Dispatcher lock.
Similarly, a thread calling getImage acquires the Dispatcher lock and then each Taxi lock (one at at time).
Just as in LeftRightDeadlock, two locks are acquired by two threads in different orders, risking deadlock.

It was easy to spot the deadlock possibility in LeftRightDeadlock or transferMoney by
looking for methods that acquire two locks.
Spotting the deadlock possibility in Taxi and Dispatcher is a little harder:
the warning sign is that an alien method is being called while holding a lock.
 */