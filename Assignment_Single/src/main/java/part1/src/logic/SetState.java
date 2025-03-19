package part1.src.logic;

import java.util.Scanner;

public class SetState extends Thread {
    private final ProgramStateManager stateManager;

    public SetState() {
        this.stateManager = ProgramStateManager.getInstance(); // Ottieni l'istanza singleton
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "start":
                    stateManager.setState(ProgramState.START);
                    System.out.println("Programma avviato/ripreso.");
                    break;
                case "pause":
                    stateManager.setState(ProgramState.PAUSE);
                    System.out.println("Programma in pausa.");
                    break;
                case "stop":
                    stateManager.setState(ProgramState.STOP);
                    System.out.println("Programma terminato.");
                    return; // Termina il thread
                default:
                    System.out.println("Comando non riconosciuto. Usa 'start', 'pause' o 'stop'.");
            }
        }
    }
}
