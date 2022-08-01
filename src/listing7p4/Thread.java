package listing7p4;

public class Thread {
    /* Dummy Implementation */
    public void interrupt() {

    }

    /* Dummy Implementation */
    public boolean isInterrupted() {
        return true;
    }

    /* Dummy Implementation */
    public static boolean interrupted() {
        return true;
    }
}

/*
Each thread has a boolean interrupted status; interrupting a thread sets its interrupted status to true.
Thread contains methods for interrupting a thread and querying the interrupted status of a thread,
as shown in Listing 7.4.
The interrupt method interrupts the target thread,
and isInterrupted returns the interrupted status of the target thread.
The poorly named static interrupted method clears the interrupted status of the current thread and
returns its previous value; this is the only way to clear the interrupted status.
 */
