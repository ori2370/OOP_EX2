import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

public class MyCallable implements Callable<Integer> {
    private String fileName;
    public int lineCounter = 0;

    public MyCallable(String fileName) {
        this.fileName = fileName;
    }

    public MyCallable() {
    }


    public int getLineCounter() {
        return lineCounter;
    }

    @Override
        public Integer call() throws Exception {

            int lineCounter = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line = br.readLine();
                while (line != null) {
                    lineCounter++;
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return lineCounter;
        }
}
