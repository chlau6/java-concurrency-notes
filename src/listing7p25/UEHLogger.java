package listing7p25;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UEHLogger implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread t, Throwable e) {
        Logger logger = Logger.getAnonymousLogger();
        logger.log(Level.SEVERE, "Thread terminated with exception: " + t.getName(), e);
    }
}

/*
What the handler should do with an uncaught exception depends on your quality-of-service requirements.
The most common response is to write an error message and stack trace to the application log, as shown in Listing 7.25.
Handlers can also take more direct action, such as trying to restart the thread, shutting down the application,
paging an operator, or other corrective or diagnostic action.
 */
