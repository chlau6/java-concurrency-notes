package listing5p13;

public class UncheckedExceptionWrapper {
    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException) {
            return (RuntimeException) t;
        } else if (t instanceof Error) {
            throw (Error) t;
        } else {
            throw new IllegalStateException("Not unchecked", t);
        }
    }
}

/*
When get throws an ExecutionException in Preloader, the cause will fall into one of three categories:
a checked exception thrown by the Callable, a RuntimeException, or an Error.

We must handle each of these cases separately,
but we will use the launderThrowable utility method in Listing 5.13 to
encapsulate some of the messier exception-handling logic.

Before calling launderThrowable, Preloader tests for the known checked exceptions and rethrows them.
That leaves only unchecked exceptions, which Preloader handles by calling launderThrowable and throwing the result.
If the Throwable passed to launderThrowable is an Error, launderThrowable rethrows it directly;
if it is not a RuntimeException, it throws an IllegalStateException to indicate a logic error.
That leaves only RuntimeException, which launderThrowable returns to its caller,
and which the caller generally rethrows.
 */
