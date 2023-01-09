import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class threadFileHelper implements Runnable {
    private final String fileName;
    public int lineCounter = 0;

    public threadFileHelper(String fileName) {
        this.fileName = fileName;

    }


    public int getLineCounter() {
        return lineCounter;
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
        addToLineCounter(Counter);
    }


    public synchronized void addToLineCounter(int count) {
        lineCounter += count;
    }

}
