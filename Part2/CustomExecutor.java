import java.util.concurrent.*;

public class CustomExecutor extends ThreadPoolExecutor {
    private int highestPriority;

    public CustomExecutor() {
        super(Runtime.getRuntime().availableProcessors() / 2, Runtime.getRuntime().availableProcessors() - 1, 300, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<>());
    }

    public <V> Future<V> submit(Task<V> task) {
        ComparableFuture<V> future = new ComparableFuture<>(task);
        super.execute(future);
        return future;
    }

    public <V> Future<V> submit(Callable<V> call, TaskType type) {
        return this.submit(new Task<V>(call, type));
    }

    public <V> Future<V> submit(Callable<V> call) {
        return this.submit(new Task<V>(call));
    }

    private void waitForTermination() {
        try {
            if (!super.awaitTermination(300, TimeUnit.MILLISECONDS)) {
                super.shutdownNow();
            }
        } catch (InterruptedException e) {
            super.shutdownNow();
        }
    }

    public void gracefullyTerminate() {
        super.shutdown();
        waitForTermination();
    }

    public int getCurrentMax() {
        return highestPriority;
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        this.highestPriority = ((ComparableFuture<?>) r).getPriority();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (this.getQueue().peek() == null) {
            this.highestPriority = -1;
        }
    }
}