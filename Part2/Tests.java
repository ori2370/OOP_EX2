import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class Tests {
    private static Logger LoggerFactory;
    public static final Logger logger = LoggerFactory.getLogger(String.valueOf(Tests.class));

    @Test
    public void partialTest() {
        CustomExecutor customExecutor = new CustomExecutor();


        var task = Task.createTask(() -> {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.OTHER);
        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());

        var priceTask = customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());

        Task<Double> PowTask = Task.createTask(() ->{
            return 1000 * Math.pow(3, 5);
                },TaskType.COMPUTATIONAL);
        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());

        var sumTask = customExecutor.submit(task);
        final int sum;
        try {
            sum = sumTask.get(1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());
        logger.info(() -> "Sum of 1 through 10 = " + sum);
        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };

        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = priceTask.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        logger.info(() -> "Current maximum priority = " + customExecutor.getCurrentMax());
        System.out.println(customExecutor);
        System.out.println(customExecutor.getQueue());

        customExecutor.gracefullyTerminate();

    }
}



