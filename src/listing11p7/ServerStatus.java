package listing11p7;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.Set;

@ThreadSafe
public class ServerStatus {
    @GuardedBy("users")
    public final Set<String> users;

    @GuardedBy("queries")
    public final Set<String> queries;

    public ServerStatus(Set<String> users, Set<String> queries) {
        this.users = users;
        this.queries = queries;
    }

    public void addUser(String u) {
        synchronized (users) {
            users.add(u);
        }
    }

    public void addQuery(String q) {
        synchronized (queries) {
            queries.add(q);
        }
    }
    // remove methods similarly refactored to use split locks
}

/*
ServerStatus in Listing 11.6 shows a portion of the monitoring interface for a database server that
maintains the set of currently logged-on users and the set of currently executing queries.
As a user logs on or off or query execution begins or ends,
the ServerStatus object is updated by calling the appropriate add or remove method.
The two types of information are completely independent;
ServerStatus could even be split into two separate classes with no loss of functionality.
*/