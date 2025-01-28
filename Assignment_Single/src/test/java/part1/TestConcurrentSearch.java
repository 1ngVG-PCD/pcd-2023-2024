package part1;

import org.junit.jupiter.api.Test;
import part1.src.step00.SeqSearch;
import part1.src.step01.ConcurrentSearch;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static part1.src.services.ScanDirectory.getPdfFiles;

public class TestConcurrentSearch {

    File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet");
    List<File> pdfs = getPdfFiles(directoryPath);
    ConcurrentSearch concurrentSearch = new ConcurrentSearch();

    @Test
    void TestAllFileHaveWord() {
        String word = "!";
        assertEquals(301, concurrentSearch.run(pdfs, word));
    }

    @Test
    void TestNone() {
        String word = "wow";
        assertEquals(0, concurrentSearch.run(pdfs, word));
    }

    @Test
    void TestMixedDirectory() {
        String word = "parola";
        assertEquals(151, concurrentSearch.run(pdfs, word));
    }
}

