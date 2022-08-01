package listing6p16;

import dummy.Ad;
import dummy.FetchAdTask;
import dummy.Page;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class TimedFutureDemo {
    private static final int NTHREADS = 100;
    private static final long TIME_BUDGET = 5000;
    private final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);
    private static final Ad DEFAULT_AD = new Ad();

    Page renderPageWithAd() throws InterruptedException {
        long endNanos = System.nanoTime() + TIME_BUDGET;
        FetchAdTask task = new FetchAdTask();

        Future<Ad> f = exec.submit(task);

        // Render the page while waiting for the ad
        Page page = renderPageBody();
        Ad ad;

        try {
            // Only wait for the remaining time budget
            long timeLeft = endNanos - System.nanoTime();
            ad = f.get(timeLeft, NANOSECONDS);
        } catch (ExecutionException e) {
            ad = DEFAULT_AD;
        } catch (TimeoutException e) {
            ad = DEFAULT_AD;
            f.cancel(true);
        }

        page.setAd(ad);
        return page;
    }

    private Page renderPageBody() {
        return new Page();
    }

    public static void main(String[] args) {

    }
}

/*
Listing 6.16 shows a typical application of a timed Future.get.
It generates a composite web page that contains the requested content plus an advertisement fetched from an ad server.
It submits the ad-fetching task to an executor, computes the rest of the page content,
and then waits for the ad until its time budget runs out.
If the get times out, it cancels the ad-fetching task and uses a default advertisement instead.
 */
