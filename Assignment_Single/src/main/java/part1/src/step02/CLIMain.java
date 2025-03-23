package part1.src.step02;

import part1.src.logic.OutputUpdater;

import java.io.File;
import java.util.Scanner;

public class CLIMain implements OutputUpdater {

    public static void main(String[] args) throws InterruptedException {
        CLIMain cliMain = new CLIMain();
        cliMain.start();
    }

    public void start() throws InterruptedException {

        Scanner scanner = new Scanner(System.in);

        // Chiedi il percorso della directory
        System.out.print("Inserisci il percorso della directory: ");
        File directoryPath = new File(scanner.nextLine());

        // Chiedi la parola da cercare
        System.out.print("Inserisci la parola da cercare: ");
        String searchWord = scanner.nextLine();
        VirtualThreadSearch search = new VirtualThreadSearch();
        int result = 0;
        result = search.run(directoryPath, searchWord, this);

        System.out.println("Elaborazione completata. La parola è stata trovata in " + result + " file.");
    }

    @Override
    public void update(int totalFilesAnalyzed, int pdfFilesFound, int pdfFilesWithWord){
        System.out.println("File analizzati: " + totalFilesAnalyzed +
                ", File PDF trovati: " + pdfFilesFound +
                ", File PDF contenenti la parola: " + pdfFilesWithWord);
    }
}
