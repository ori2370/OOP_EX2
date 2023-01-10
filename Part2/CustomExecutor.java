import java.util.Comparator;
import java.util.concurrent.*;

public class CustomExecutor {
    // giving argument Comparator.reverseOrder() to the queue because we want to prioritize from the minimal to maximal value
    private final PriorityBlockingQueue queue=new PriorityBlockingQueue<>(10, Comparator.reverseOrder());
    private ThreadPoolExecutor executor;
    private final int poolSize;
    private int maxPriority;

    public CustomExecutor() {
        this.poolSize=Runtime.getRuntime().availableProcessors();
        this.executor=new ThreadPoolExecutor(poolSize/2,poolSize-1,300,TimeUnit.MILLISECONDS,queue );

    }

    public <V>Future<V> submit(Callable<V> call, TaskType type) {

        return submit(new Task<>(call,type));
    }
    public <V>Future<V> submit(Callable<V> call) {
        return submit(new Task<>(call));
    }
    public <V>Future<V> submit(Task<V> task) {
        queue.put(task.getTask());
        maxPriority=Math.max(maxPriority, task.getPriority());
    return executor.submit(task.getTask());
    }

    private void waitForTermination() {
        try {
            if (!executor.awaitTermination(300, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    public void gracefullyTerminate() {
        executor.shutdown();
       waitForTermination();
    }

    public PriorityBlockingQueue getQueue() {
        return queue;
    }

    public int getCurrentMax() {

        return maxPriority;
    }

    @Override
    public String toString() {
        return "CustomExecutor{" +
                "queue=" + queue +
                ", executor=" + executor +
                ", poolSize=" + poolSize +
                '}';
    }
}