package listing7p24;

public interface UncaughtExceptionHandler {
    void uncaughtException(Thread t, Throwable e);
}

/*
The previous section offered a proactive approach to the problem of unchecked exceptions.
The Thread API also provides the UncaughtExceptionHandler facility,
which lets you detect when a thread dies due to an uncaught exception.

The two approaches are complementary: taken together, they provide defense-in-depth against thread leakage.

When a thread exits due to an uncaught exception,
the JVM reports this event to an application-provided UncaughtExceptionHandler (see Listing 7.24);
if no handler exists, the default behavior is to print the stack trace to System.err.
 */
