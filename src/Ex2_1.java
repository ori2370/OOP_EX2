import java.io.*;
import java.util.Random;

public class Ex2_1 {
    public static String[] createTextFiles(int n, int seed, int bound) {
        String[] fileNames = new String[n];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            File file = new File("file_" + (i + 1) + ".txt");
            fileNames[i] = "file_" + (i + 1) + ".txt";
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                int randomInt = rand.nextInt(bound);
                String line = "hara al reshatot";
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
        for (int i = 0; i < fileNames.length; i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileNames[i]))) {
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

    public static int getNumOfLinesThreads(String[] fileNames) {
        int lineCounter = 0;
        threadFileHelper[] threads = new threadFileHelper[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            threads[i] = new threadFileHelper(fileNames[i], i);
            threads[i].start();
         }    
         for (int i = 0; i < fileNames.length; i++) {
            try {
            threads[i].join();
            lineCounter += threads[i].getLineCounter();
                 } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }

             return lineCounter;
        }


















    public static void main(String[] args) {
        String[] texts = createTextFiles(3,1,10);
        long startTime1 = System.nanoTime();
        int result1 = getNumOfLines(texts);
        long endTime1 = System.nanoTime();
        long duration1 = endTime1 - startTime1;
        long startTime2 = System.nanoTime();
        int result2 = getNumOfLinesThreads(texts);
        long endTime2 = System.nanoTime();
        long duration2 = endTime2 - startTime2;
        System.out.println("Duration1: " + duration1 + " nanoseconds" + result1);
        System.out.println("Duration2: " + duration2 + " nanoseconds" + result2);

    }
}


