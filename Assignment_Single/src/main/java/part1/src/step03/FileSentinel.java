package part1.src.step03;

import java.io.File;

/**
 * La classe {@code FileSentinel} è un oggetto speciale che indica la fine della scansione della directory.
 */
public class FileSentinel extends File {
    private static final FileSentinel sentinel = new FileSentinel();

    private FileSentinel() {
        super("");
    }

    public static FileSentinel getSentinel() {
        return sentinel;
    }
}
