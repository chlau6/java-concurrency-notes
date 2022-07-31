package listing5p16;

public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}
