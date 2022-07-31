package listing6p9;

import java.util.Timer;
import java.util.TimerTask;

import static java.util.concurrent.TimeUnit.SECONDS;

public class OutOfTime {
    public static void main(String[] args) throws Exception {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1);
        SECONDS.sleep(1);
        timer.schedule(new ThrowTask(), 1);
        SECONDS.sleep(5);
    }
    static class ThrowTask extends TimerTask {
        public void run() {
            throw new RuntimeException();
        }
    }
}

/*
OutOfTime in Listing 6.9 illustrates how a Timer can become confused in this manner and, as confusion loves company,
how the Timer shares its confusion with the next hapless caller that tries to submit a TimerTask.

You might expect the program to run for six seconds and exit,
but what actually happens is that it terminates after one second with an IllegalStateException whose
message text is "Timer already cancelled".

ScheduledThreadPoolExecutor deals properly with ill-behaved tasks;
there is little reason to use Timer in Java 5.0 or later.
 */
