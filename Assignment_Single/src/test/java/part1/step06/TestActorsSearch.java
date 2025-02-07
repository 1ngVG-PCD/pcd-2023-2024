//package part1.step06;
//
//import org.junit.jupiter.api.Test;
//import part1.src.step05.ReactiveSearch;
//
//import java.io.File;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class TestActorsSearch {
//
//    File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet");
//    ActorsSearch search = new ActorsSearch();
//
//
//    @Test
//    void TestNone() throws InterruptedException {
//        String word = "wow";
//        assertEquals(0, search.run(directoryPath, word));
//    }
//
//    @Test
//    void TestMixedDirectory() throws InterruptedException {
//        String word = "parola";
//        assertEquals(151, search.run(directoryPath, word));
//    }
//}
