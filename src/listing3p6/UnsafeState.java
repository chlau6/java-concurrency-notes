package listing3p6;

public class UnsafeState {
    private String[] states = new String[] {"AK", "AL"};

    public String[] getStates() {
        return states;
    }
}

/*
Similarly, returning a reference from a nonprivate method also publishes the returned object.
UnsafeStates in Listing 3.6 publishes the supposedly private array of state abbreviations.
 */
