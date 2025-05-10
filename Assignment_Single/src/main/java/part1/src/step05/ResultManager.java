package part1.src.step05;

import part1.src.logic.OutputUpdater;
import part1.src.logic.SearchResult;

import java.util.concurrent.atomic.AtomicReference;

public class ResultManager {
    private final AtomicReference<SearchResult> result = new AtomicReference<>(new SearchResult(0, 0, 0));
    private final OutputUpdater outputUpdater;

    public ResultManager(OutputUpdater outputUpdater) {
        this.outputUpdater = outputUpdater;
    }

    public void incrementFilesFound() {
        result.updateAndGet(current -> {
            SearchResult newResult = new SearchResult(
                    current.totalFilesAnalyzed(),
                    current.pdfFilesFound() + 1,
                    current.pdfFilesWithWord()
            );
            outputUpdater.update(newResult.totalFilesAnalyzed(), newResult.pdfFilesFound(), newResult.pdfFilesWithWord());
            return newResult;
        });
    }

    public void incrementFilesAnalyzed() {
        result.updateAndGet(current -> {
            SearchResult newResult = new SearchResult(
                    current.totalFilesAnalyzed() + 1,
                    current.pdfFilesFound(),
                    current.pdfFilesWithWord()
            );
            outputUpdater.update(newResult.totalFilesAnalyzed(), newResult.pdfFilesFound(), newResult.pdfFilesWithWord());
            return newResult;
        });
    }

    public void incrementFilesWord() {
        result.updateAndGet(current -> {
            SearchResult newResult = new SearchResult(
                    current.totalFilesAnalyzed(),
                    current.pdfFilesFound(),
                    current.pdfFilesWithWord() + 1
            );
            outputUpdater.update(newResult.totalFilesAnalyzed(), newResult.pdfFilesFound(), newResult.pdfFilesWithWord());
            return newResult;
        });
    }

    public int getResult() {return result.get().pdfFilesWithWord();}

    public int getAnalyzed() {return result.get().totalFilesAnalyzed();}

    public int getFilesFound() {return result.get().pdfFilesFound();}
}