package part1;

import org.junit.jupiter.api.Test;
import part1.src.step01.ConcurrentSearch;
import part1.src.step02.VirtualThreadSearch;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static part1.src.services.ScanDirectory.getPdfFiles;

public class TestVirtualThreadSearch {

    File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet");
    List<File> pdfs = getPdfFiles(directoryPath);
    VirtualThreadSearch search = new VirtualThreadSearch();

    @Test
    void TestAllFileHaveWord() throws InterruptedException {
        String word = "!";
        assertEquals(301, search.run(pdfs, word));
    }

    @Test
    void TestNone() throws InterruptedException {
        String word = "wow";
        assertEquals(0, search.run(pdfs, word));
    }

    @Test
    void TestMixedDirectory() throws InterruptedException {
        String word = "parola";
        assertEquals(151, search.run(pdfs, word));
    }
}
