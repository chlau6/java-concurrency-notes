package listing14p3;

import annotation.ThreadSafe;
import dummy.BufferEmptyException;
import dummy.BufferFullException;
import listing14p2.BaseBoundedBuffer;

@ThreadSafe
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {
    public GrumpyBoundedBuffer(int size) {
        super(size);
    }

    public synchronized void put(V v) throws BufferFullException {
        if (isFull()) {
            throw new BufferFullException();
        }
        doPut(v);
    }

    public synchronized V take() throws BufferEmptyException {
        if (isEmpty()) {
            throw new BufferEmptyException();
        }
        return doTake();
    }
}

/*
GrumpyBoundedBuffer in Listing 14.3 is a crude first attempt at implementing a bounded buffer.
The put and take methods are synchronized to ensure exclusive access to the buffer state,
since both employ check-then-act logic in accessing the buffer.

While this approach is easy enough to implement, it is annoying to use.
Exceptions are supposed to be for exceptional conditions.
"Buffer is full" is not an exceptional condition for a bounded buffer
any more than "red" is an exceptional condition for a traffic signal.
The simplification in implementing the buffer (forcing the caller to manage the state dependence)
is more than made up for by the substantial complication in using it,
since now the caller must be prepared to catch exceptions and possibly retry for every buffer operation.
A well-structured call to take is shown in Listing 14.4 - not very pretty,
especially if put and take are called throughout the program.
*/