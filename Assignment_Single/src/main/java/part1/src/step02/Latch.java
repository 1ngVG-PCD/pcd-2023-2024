package part1.src.step02;

/**
 * La classe {@code Latch} implementa un meccanismo di sincronizzazione personalizzato
 * simile a {@code CountDownLatch}. Permette a uno o più thread di attendere fino a quando
 * un contatore non raggiunge il valore zero.
 */
public class Latch {
    private int count;

    /**
     * Crea un nuovo {@code Latch} con un valore iniziale specificato.
     *
     * @param count Il valore iniziale del contatore. Deve essere maggiore o uguale a zero.
     * @throws IllegalArgumentException se {@code count} è negativo.
     */
    public Latch(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count must be non-negative");
        }
        this.count = count;
    }

    /**
     * Decrementa il contatore di uno. Se il contatore raggiunge il valore zero,
     * risveglia tutti i thread che sono in attesa sul metodo {@code await()}.
     * Questo metodo è thread-safe.
     */
    public synchronized void countDown() {
        if (count > 0) {
            count--;
            if (count == 0) {
                notifyAll(); // Risveglia tutti i thread in attesa
            }
        }
    }

    /**
     * Mette in attesa il thread chiamante finché il contatore non raggiunge il valore zero.
     * Se il contatore è già zero, il thread continua immediatamente.
     *
     * @throws InterruptedException se il thread viene interrotto mentre è in attesa.
     */
    public synchronized void await() throws InterruptedException {
        while (count > 0) {
            wait();
        }
    }

    /**
     * Restituisce il valore attuale del contatore. Questo metodo è thread-safe.
     *
     * @return Il valore attuale del contatore.
     */
    public synchronized int getCount() {
        return count;
    }
}
