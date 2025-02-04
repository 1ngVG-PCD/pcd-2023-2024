package part1.step04;

import org.junit.jupiter.api.Test;
import part1.src.step04.EventSearch;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEventSearch {

    File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet");
    EventSearch search = new EventSearch();


    @Test
    void TestNone() {
        String word = "wow";
        assertEquals(0, search.run(directoryPath, word));
    }

    @Test
    void TestMixedDirectory() {
        String word = "parola";
        assertEquals(151, search.run(directoryPath, word));
    }
}
