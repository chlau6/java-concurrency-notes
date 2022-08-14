package listing14p4;

import dummy.BufferEmptyException;
import listing14p3.GrumpyBoundedBuffer;

public class GrumpyBoundedBufferClientDemo<V> {
    private static final long SLEEP_GRANULARITY = 1000;
    private final GrumpyBoundedBuffer<V> buffer = new GrumpyBoundedBuffer<>(10);

    void take() throws InterruptedException {
        while (true) {
            try {
                V item = buffer.take();
                // use item
                break;
            } catch (BufferEmptyException e) {
                Thread.sleep(SLEEP_GRANULARITY);
            }
        }
    }

}

/*
GrumpyBoundedBuffer in Listing 14.3 is a crude first attempt at implementing a bounded buffer.
The put and take methods are synchronized to ensure exclusive access to the buffer state,
since both employ check-then-act logic in accessing the buffer.

While this approach is easy enough to implement, it is annoying to use.
Exceptions are supposed to be for exceptional conditions.
“Buffer is full” is not an exceptional condition for a bounded buffer
any more than “red” is an exceptional condition for a traffic signal.
The simplification in implementing the buffer (forcing the caller to manage the state dependence)
is more than made up for by the substantial complication in using it,
since now the caller must be prepared to catch exceptions and possibly retry for every buffer operation.
A well-structured call to take is shown in Listing 14.4 - not very pretty,
especially if put and take are called throughout the program.

A variant of this approach is to return an error value when the buffer is in the wrong state.
This is a minor improvement in that it doesn’t abuse the exception mechanism by
throwing an exception that really means “sorry, try again”, but it does not address the fundamental problem:
that callers must deal with precondition failures themselves.

The client code in Listing 14.4 is not the only way to implement the retry logic.
The caller could retry the take immediately, without sleeping—an approach known as busy waiting or spin waiting.
This could consume quite a lot of CPU time if the buffer state does not change for a while.
On the other hand, if the caller decides to sleep so as not to consume so much CPU time,
it could easily “oversleep” if the buffer state changes shortly after the call to sleep.
So the client code is left with the choice between the poor CPU usage of spinning and
the poor responsiveness of sleeping.
(Somewhere between busy waiting and sleeping would be calling Thread.yield in each iteration,
which is a hint to the scheduler that this would be a reasonable time to let another thread run.
If you are waiting for another thread to do something, that something might happen faster if
you yield the processor rather than consuming your full scheduling quantum.)
*/