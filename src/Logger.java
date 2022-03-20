import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Logger {

    private final PrintWriter writer;

    public Logger(String filename) throws FileNotFoundException {
        this.writer = new PrintWriter(filename);
    }

    public void log(Talk role, String message) {
        writer.printf("%d, %s, %s\n", System.currentTimeMillis(), role, message);
    }

    public void destroy() {
        writer.close();
    }
}
