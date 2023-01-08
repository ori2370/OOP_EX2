import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class threadFileHelper implements Runnable {
    private String fileName;
    private int threadNumber;
    public int lineCounter = 0;

    public threadFileHelper(String fileName, int threadNumber) {
        this.fileName = fileName;
        this.threadNumber = threadNumber;
    }

    public threadFileHelper() {
    }

    public int getLineCounter() {
        return lineCounter;
    }

    public void setLineCounter(int lineCounter) {
        this.lineCounter = lineCounter;
    }

    @Override
    public void run() {
        int Counter = 0;
        File file = new File(fileName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null) {
                Counter++;
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lineCounter = Counter;
    }
}

