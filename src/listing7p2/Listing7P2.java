package listing7p2;

import listing7p1.PrimeGenerator;

import java.math.BigInteger;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Listing7P2 {
    List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        new Thread(generator).start();
        try {
            SECONDS.sleep(1);
        } finally {
            generator.cancel();
        }
        return generator.get();
    }
}

/*
Listing 7.2 shows a sample use of this class that lets the prime generator run for one second before cancelling it.
The generator won’t necessarily stop after exactly one second,
since there may be some delay between the time that cancellation is requested and
the time that the run loop next checks for cancellation.

The cancel method is called from a finally block to ensure that the prime generator
is cancelled even if the call to sleep is interrupted.
If cancel were not called, the prime-seeking thread would run forever,
consuming CPU cycles and preventing the JVM from exiting.
 */
