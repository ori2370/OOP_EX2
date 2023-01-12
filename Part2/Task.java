import java.util.Objects;
import java.util.concurrent.Callable;


public class Task<V> implements Callable<V> {
    //Data
    private final Callable<V> task;
    private final TaskType type;

    // Constructors
    public Task(Callable<V> task, TaskType type) {
        this.task = task;
        this.type = type;
    }

    public Task(Callable<V> task) {
        this.task = task;
        this.type = TaskType.OTHER;
    }

    //methods
    public static <V> Task<V> createTask(Callable<V> task, TaskType type) {
        return new Task<V>(task, type);
    }

    public Callable<V> getTask() {
        return task;
    }

    public TaskType getType() {
        return type;
    }


    @Override
    public V call() throws Exception {
        return task.call();
    }

    public int getPriority() {
        return type.getPriorityValue();
    }

    @Override
    public String toString() {
        return "Task{" +
                "task=" + task +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task<?> task1 = (Task<?>) o;
        return Objects.equals(getTask(), task1.getTask()) && getType() == task1.getType();
    }
}