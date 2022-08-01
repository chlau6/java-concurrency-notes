package listing7p3;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CancelProducerThreadDemo {
    class BrokenPrimeProducer extends Thread {
        private final BlockingQueue<BigInteger> queue;
        private volatile boolean cancelled = false;

        BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                BigInteger p = BigInteger.ONE;
                while (!cancelled) {
                    queue.put(p = p.nextProbablePrime());
                }
            } catch (InterruptedException consumed) {

            }
        }

        public void cancel() {
            cancelled = true;
        }
    }
    
    void consumePrimes() throws InterruptedException {
        BlockingQueue<BigInteger> primes = new LinkedBlockingQueue<>();
        BrokenPrimeProducer producer = new BrokenPrimeProducer(primes);

        producer.start();

        try {
            while (needMorePrimes()) {
                consume(primes.take());
            }
        } finally {
            producer.cancel();
        }
    }

    /* Dummy Implementation */
    private void consume(BigInteger take) {
    }

    /* Dummy Implementation */
    private boolean needMorePrimes() {
        return true;
    }
}

/*
The cancellation mechanism in PrimeGenerator will eventually cause the primeseeking task to exit,
but it might take a while.
If, however, a task that uses this approach calls a blocking method such as BlockingQueue.put,
we could have a more serious problem - the task might never check the cancellation flag and
therefore might never terminate.

BrokenPrimeProducer in Listing 7.3 illustrates this problem.
The producer thread generates primes and places them on a blocking queue.
If the producer gets ahead of the consumer, the queue will fill up and put will block.
What happens if the consumer tries to cancel the producer task while it is blocked in put?
It can call cancel which will set the cancelled flag - but the producer will never check the flag because
it will never emerge from the blocking put (because the consumer has stopped retrieving primes from the queue).
 */
