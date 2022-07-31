package dummy;

import dummy.Ad;

import java.util.concurrent.Callable;

public class FetchAdTask implements Callable<Ad> {
    public Ad call() throws Exception {
        return new Ad();
    }
}
