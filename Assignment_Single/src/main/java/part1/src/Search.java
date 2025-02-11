package part1.src;

import java.io.File;

public interface Search {
    public Integer run(File directory, String word) throws InterruptedException;
}
