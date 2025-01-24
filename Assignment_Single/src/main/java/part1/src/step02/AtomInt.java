package part1.src.step02;

/**
 * La classe {@code AtomInt} implementa un contatore thread-safe che consente
 * a più thread di leggere e modificare il valore in modo sincronizzato.
 */
public class AtomInt {
    private int value;

    /**
     * Crea un nuovo {@code AtomInt} con un valore iniziale specificato.
     *
     * @param initialValue Il valore iniziale del contatore.
     */
    public AtomInt(int initialValue) {
        this.value = initialValue;
    }

    /**
     * Incrementa il valore del contatore di uno in modo thread-safe.
     */
    public synchronized void incrementAndGet() {
        value++;
    }

    /**
     * Restituisce il valore corrente del contatore in modo thread-safe.
     *
     * @return Il valore corrente del contatore.
     */
    public synchronized int get() {
        return value;
    }

    /**
     * Imposta il valore del contatore a un nuovo valore specificato in modo thread-safe.
     *
     * @param newValue Il nuovo valore da assegnare al contatore.
     */
    public synchronized void set(int newValue) {
        this.value = newValue;
    }
}


