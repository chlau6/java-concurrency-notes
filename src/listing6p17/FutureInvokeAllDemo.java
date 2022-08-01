package listing6p17;

import dummy.Ad;
import dummy.TravelCompany;
import dummy.TravelInfo;
import dummy.TravelQuote;

import java.util.*;
import java.util.concurrent.*;

public class FutureInvokeAllDemo {
    private static final int NTHREADS = 100;
    private final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);

    private class QuoteTask implements Callable<TravelQuote> {
        private final TravelCompany company;
        private final TravelInfo travelInfo;

        public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
            this.company = company;
            this.travelInfo = travelInfo;
        }

        public TravelQuote call() throws Exception {
            return company.solicitQuote(travelInfo);
        }

        /*
        Dummy Implementation
         */
        public TravelQuote getFailureQuote(Throwable cause) {
            return new TravelQuote();
        }

        /*
        Dummy Implementation
         */
        public TravelQuote getTimeoutQuote(CancellationException e) {
            return new TravelQuote();
        }
    }
    public List<TravelQuote> getRankedTravelQuotes(TravelInfo travelInfo, Set<TravelCompany> companies,
            Comparator<TravelQuote> ranking, long time, TimeUnit unit)
            throws InterruptedException {

        List<QuoteTask> tasks = new ArrayList<QuoteTask>();

        for (TravelCompany company : companies) {
            tasks.add(new QuoteTask(company, travelInfo));
        }

        List<Future<TravelQuote>> futures = exec.invokeAll(tasks, time, unit);
        List<TravelQuote> quotes = new ArrayList<TravelQuote>(tasks.size());
        Iterator<QuoteTask> taskIter = tasks.iterator();

        for (Future<TravelQuote> f : futures) {
            QuoteTask task = taskIter.next();
            try {
                quotes.add(f.get());
            } catch (ExecutionException e) {
                quotes.add(task.getFailureQuote(e.getCause()));
            } catch (CancellationException e) {
                quotes.add(task.getTimeoutQuote(e));
            }
        }

        Collections.sort(quotes, ranking);

        return quotes;
    }
}

/*
The time-budgeting approach in the previous section can be easily generalized to an arbitrary number of tasks.
Consider a travel reservation portal:
the user enters travel dates and requirements and the portal fetches and displays bids from a number of airlines,
hotels or car rental companies.
Depending on the company, fetching a bid might involve invoking a web service, consulting a database,
performing an EDI transaction, or some other mechanism.
Rather than have the response time for the page be driven by the slowest response,
it may be preferable to present only the information available within a given time budget.
For providers that do not respond in time,
the page could either omit them completely or display a placeholder such as "Did not hear from Air Java in time."

Fetching a bid from one company is independent of fetching bids from another,
so fetching a single bid is a sensible task boundary that allows bid retrieval to proceed concurrently.
It would be easy enough to create n tasks, submit them to a thread pool, retain the Futures,
and use a timed get to fetch each result sequentially via its Future, but there is an even easier way - invokeAll.

Listing 6.17 uses the timed version of invokeAll to submit multiple tasks to
an ExecutorService and retrieve the results.
The invokeAll method takes a collection of tasks and returns a collection of Futures.
The two collections have identical structures;
invokeAll adds the Futures to the returned collection in the order imposed by the task collection's iterator,
thus allowing the caller to associate a Future with the Callable it represents.
The timed version of invokeAll will return when all the tasks have completed, the calling thread is interrupted,
or the timeout expires.
Any tasks that are not complete when the timeout expires are cancelled.
On return from invokeAll, each task will have either completed normally or been cancelled;
the client code can call get or isCancelled to find out which.
 */
