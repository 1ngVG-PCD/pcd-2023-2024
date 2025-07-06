package part1.src.step06;

import java.io.File;

public class Messages {
    public record StartSearch(File directory, String word) {
    }
    public record ScanDirectory(File directory, String word) {
    }

    public record FileFound(File file) {
    }

    public record ResultOfSearch(boolean containsWord) {
    }
}
