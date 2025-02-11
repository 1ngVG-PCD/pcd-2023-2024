package part1.step01;

import org.junit.jupiter.api.Test;
import part1.src.step01.ConcurrentSearch;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestConcurrentSearch {

    File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet");
    ConcurrentSearch search = new ConcurrentSearch();


    @Test
    void TestNone() throws InterruptedException {
        String word = "wow";
        assertEquals(0, search.run(directoryPath, word));
    }

    @Test
    void TestMixedDirectory() throws InterruptedException {
        String word = "parola";
        assertEquals(151, search.run(directoryPath, word));
    }
}

