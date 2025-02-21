package part1.src.logic;

import java.io.File;

@FunctionalInterface
public interface Search {
    Integer run(File directory, String word, OutputUpdater outputUpdater) throws InterruptedException;
}
