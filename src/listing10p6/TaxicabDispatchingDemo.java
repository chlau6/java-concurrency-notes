package listing10p6;

import annotation.GuardedBy;
import annotation.ThreadSafe;
import dummy.Image;
import dummy.Point;

import java.util.HashSet;
import java.util.Set;

public class TaxicabDispatchingDemo {
    @ThreadSafe
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
            boolean reachedDestination;
            synchronized (this) {
                this.location = location;
                reachedDestination = location.equals(destination);
            }

            if (reachedDestination) {
                dispatcher.notifyAvailable(this);
            }
        }
    }

    @ThreadSafe
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

        public Image getImage() {
            Set<Taxi> copy;

            synchronized (this) {
                copy = new HashSet<Taxi>(taxis);
            }

            Image image = new Image();
            for (Taxi t : copy) {
                image.drawMarker(t.getLocation());
            }

            return image;
        }
    }
}

/*
Taxi and Dispatcher in Listing 10.5 can be easily refactored to use open calls and thus eliminate the deadlock risk.
This involves shrinking the synchronized blocks to guard only operations that involve shared state, as in Listing 10.6.
Very often, the cause of problems like those in Listing 10.5 is the use of synchronized methods instead of
smaller synchronized blocks for reasons of compact syntax or simplicity rather than because
the entire method must be guarded by a lock.
(As a bonus, shrinking the synchronized block may also improve scalability as well.)
*/