import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.Callable;

public class MyCallable implements Callable<Integer> {
    private final String fileName;

    public MyCallable(String fileName) {
        this.fileName = fileName;
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
        }

        return lineCounter;
    }
}
