import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String... args) {
        try {
            Arguments.load(args);
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        String input;

        try {
            input = Files.readString(Paths.get( Arguments.inputFile() ));
        } catch (IOException ex) {
            System.err.println("Couldn't read file: " + ex.getMessage());
            return;
        }

        Thread scheduler = new Thread(new Scheduler(input, Arguments.workerCount()));
        scheduler.start();

        try {
            scheduler.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
