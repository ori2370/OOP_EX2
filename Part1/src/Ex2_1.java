import java.io.*;

import java.util.Random;
import java.util.concurrent.*;


public class Ex2_1 {
    public static String[] createTextFiles(int n, int seed, int bound) {
        String[] fileNames = new String[n];
        Random rand = new Random(seed);

        for (int i = 0; i < n; i++) {
            File file = new File("file_" + (i + 1) + ".txt");
            fileNames[i] = "file_" + (i + 1) + ".txt";
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                int randomInt = rand.nextInt(bound);
                String line = "Threads Assignment";
                for (int j = 0; j < randomInt; j++) {
                    bw.write(line);
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileNames;
    }


    public static int getNumOfLines(String[] fileNames) {
        int lineCounter = 0;
        for (String fileName : fileNames) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line = br.readLine();
                while (line != null) {
                    lineCounter++;
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lineCounter;
    }

    public int getNumOfLinesThreads(String[] fileNames) {
        int lineCounter = 0;
        threadFileHelper[] threads = new threadFileHelper[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            threads[i] = new threadFileHelper(fileNames[i]);
            threads[i].start();
        }
        for (int i = 0; i < fileNames.length; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lineCounter += threads[i].getLineCounter();
        }
        return lineCounter;
    }

    public int getNumOfLinesThreadPool(String[] fileNames) {
        int lineCounter = 0;
        ExecutorService executor = Executors.newFixedThreadPool(fileNames.length);
        Future<Integer>[] results = new Future[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            Callable<Integer> callable = new MyCallable(fileNames[i]);
            results[i] = executor.submit(callable);
        }
        for (Future<Integer> result : results) {
            try {
                lineCounter += result.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

        return lineCounter;
    }


    public static void main(String[] args) {
        String[] texts = createTextFiles(3000, 1, 10000);
        long startTime1 = System.currentTimeMillis();
        int result1 = getNumOfLines(texts);
        long endTime1 = System.currentTimeMillis();
        long duration1 = endTime1 - startTime1;
        long startTime2 = System.currentTimeMillis();
        Ex2_1 ex2_1 = new Ex2_1();
        int result2 = ex2_1.getNumOfLinesThreads(texts);
        long endTime2 = System.currentTimeMillis();
        long duration2 = endTime2 - startTime2;
        long startTime3 = System.currentTimeMillis();
        int result3 = ex2_1.getNumOfLinesThreadPool(texts);
        long endTime3 = System.currentTimeMillis();
        long duration3 = endTime3 - startTime3;
        System.out.printf("Duration1:%d ms,result: %d\n", duration1, result1);
        System.out.printf("Duration2:%d ms,result: %d\n", duration2, result2);
        System.out.printf("Duration2:%d ms,result: %d\n", duration3, result3);

    }
}


