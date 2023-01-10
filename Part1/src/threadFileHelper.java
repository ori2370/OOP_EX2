import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class threadFileHelper extends Thread {
    private final String fileName;
    private AtomicInteger lineCounter = new AtomicInteger();

    public threadFileHelper(String fileName) {
        this.fileName = fileName;

    }

    public int getLineCounter() {
        return lineCounter.get();
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            while (line != null) {
                lineCounter.incrementAndGet();
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
