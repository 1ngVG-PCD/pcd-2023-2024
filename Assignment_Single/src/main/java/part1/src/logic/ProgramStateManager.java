package part1.src.logic;

public class ProgramStateManager {
    // Istanza singleton
    private static final ProgramStateManager INSTANCE = new ProgramStateManager();

    // Stato del programma
    private volatile ProgramState state = ProgramState.START;

    // Costruttore privato per impedire l'istanziazione esterna
    private ProgramStateManager() {}

    // Metodo per ottenere l'istanza singleton
    public static ProgramStateManager getInstance() {
        return INSTANCE;
    }

    // Metodo per ottenere lo stato corrente
    public ProgramState getState() {
        return state;
    }

    // Metodo per impostare lo stato
    public void setState(ProgramState newState) {
        this.state = newState;
    }
}