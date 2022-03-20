import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String... args) {
        Arguments arg;
        try {
            arg = new Arguments(args);
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        String input;

        try {
            input = Files.readString(Paths.get( arg.inputFile() ));
        } catch (IOException ex) {
            System.err.println("Couldn't read file: " + ex.getMessage());
            return;
        }

        try {
            Scheduler scheduler = new Scheduler(input, arg);
            scheduler.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
