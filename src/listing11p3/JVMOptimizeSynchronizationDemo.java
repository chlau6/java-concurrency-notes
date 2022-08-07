package listing11p3;

import java.util.List;
import java.util.Vector;

public class JVMOptimizeSynchronizationDemo {
    public String getStoogeNames() {
        List<String> stooges = new Vector<String>();
        stooges.add("Moe");
        stooges.add("Larry");
        stooges.add("Curly");
        return stooges.toString();
    }
}

/*
More sophisticated JVMs can use escape analysis to identify when
a local object reference is never published to the heap and is therefore thread-local.
In getStoogeNames in Listing 11.3, the only reference to the List is the local variable stooges,
and stack-confined variables are automatically thread-local.
A naive execution of getStoogeNames would acquire and release the lock on the Vector four times,
once for each call to add or toString.
However, a smart runtime compiler can inline these calls and then see that stooges and its internal state never escape,
and therefore that all four lock acquisitions can be eliminated.
*/