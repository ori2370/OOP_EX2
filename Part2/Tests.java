import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.*;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);

    @Test
    public void partialTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        var task = Task.createTask(() -> {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);
        var sumTask = customExecutor.submit(task);
        final int sum;
        try {
            sum = sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Sum of 1 through 10 = " + sum);
        Callable<Double> callable1 = () -> {
            return 1000 * Math.pow(1.02, 5);
        };
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
        // var is used to infer the declared type automatically
        var priceTask = customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = priceTask.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();
    }

    @Test
    public void anotherTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        ArrayList<Future<Integer>> results = new ArrayList<>();
        int count = 10;
        for (int i = 0; i < count; i++) {
            TaskType taskType = TaskType.OTHER;
            if (i % 3 == 1) {
                taskType = TaskType.IO;
            } else if (i % 3 == 0) {
                taskType = TaskType.COMPUTATIONAL;
            }

            final TaskType finalType = taskType;
            final int taskIndex = i;
            logger.info(() -> "Inserting Task #" + taskIndex + " with Type = " + finalType);

            var task = Task.createTask(() -> {
                logger.info(() -> "Starting work of Task #" + taskIndex + " with Type = " + finalType);

                Thread.sleep(5000);
                return taskIndex;
            }, finalType);

            results.add(customExecutor.submit(task));
        }

        try {
            int highestPriority = customExecutor.getCurrentMax();
            while (highestPriority > 0) {
                final int priority = highestPriority;
                logger.info(() -> "Current highest priority in queue is = " + priority);
                Thread.sleep(500);
                highestPriority = customExecutor.getCurrentMax();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}