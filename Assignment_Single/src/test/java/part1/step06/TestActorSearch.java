package part1.step06;

import org.junit.jupiter.api.Test;
import part1.src.step06.ActorSearch;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestActorSearch {

    File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet");
    ActorSearch search = new ActorSearch();
    MockOutputUpdater outputUpdater = new MockOutputUpdater(); // Mock per OutputUpdater

    @Test
    void TestNone() throws InterruptedException {
        String word = "wow";
        int result = search.run(directoryPath, word, outputUpdater); // Passa tutti i parametri
        assertEquals(0, result); // Verifica che la parola non sia stata trovata in nessun file
    }

    @Test
    void TestMixedDirectory() throws InterruptedException {
        String word = "parola";
        int result = search.run(directoryPath, word, outputUpdater); // Passa tutti i parametri
        assertEquals(151, result); // Verifica che la parola sia stata trovata in 151 file
    }
}