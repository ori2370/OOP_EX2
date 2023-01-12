import java.util.concurrent.FutureTask;

public class ComparableFuture<V> extends FutureTask<V> implements Comparable<V> {
    final int priority;

    public ComparableFuture(Task<V> callable) {
        super(callable);
        this.priority = callable.getPriority();
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(V o) {
        ComparableFuture<?> other = (ComparableFuture<?>) o;
        return Integer.compare(this.getPriority(), other.getPriority());
    }
}
