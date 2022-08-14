package dummy;

import java.math.BigInteger;

public class DollarAmount implements Comparable<DollarAmount> {
    private Integer amount;

    public DollarAmount(int amount) {
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public int compareTo(DollarAmount o) {
        return 0;
    }
}
