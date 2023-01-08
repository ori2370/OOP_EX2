import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;

public class threadPoolFileHelper implements Callable<Integer> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    private String fileName;

    public threadPoolFileHelper(String fileName) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lineCounter;
    }
}
