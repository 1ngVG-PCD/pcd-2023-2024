package part1.src.logic;

@FunctionalInterface
public interface OutputUpdater {
    void update(int totalFilesAnalyzed, int pdfFilesFound, int pdfFilesWithWord);
}