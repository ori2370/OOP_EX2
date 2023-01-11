import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class TaskWrapper <V> extends FutureTask<V> implements  Comparable<TaskWrapper<V>>{
    private final int priority;
    private  Callable<V> task;

    public TaskWrapper(Callable<V> task, int priority) {
        super(task);
        this.priority = priority;
        this.task = new Task<>(task);
    }

    public int getPriority() {
        return priority;
    }

    public Callable<V> getTask() {
        return task;
    }

    @Override
    public int compareTo(TaskWrapper<V> call) {
        return Integer.compare(this.priority,call.priority);
    }
}
