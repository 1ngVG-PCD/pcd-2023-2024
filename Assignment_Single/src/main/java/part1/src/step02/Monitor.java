package part1.src.step02;

import java.io.File;

/**
 * La classe {@code Monitor} implementa un buffer limitato per la comunicazione tra
 * produttore (scanner della directory) e consumatori (worker virtual thread).
 */
public class Monitor {
    private final File[] buffer;
    private int first = 0, last = 0, count = 0;
    private final int maxSize;
    private boolean isFinished = false;

    public Monitor(int maxSize) {
        this.maxSize = maxSize;
        this.buffer = new File[maxSize];
    }

    public synchronized void addFile(File file) throws InterruptedException {
        while (count == maxSize) {
            wait();
        }
        buffer[last] = file;
        last = (last + 1) % maxSize;
        count++;
        notifyAll();
    }

    public synchronized File getFile() throws InterruptedException {
        while (count == 0 && !isFinished) {
            wait();
        }
        if (count == 0 && isFinished) {
            return null;
        }
        File file = buffer[first];
        first = (first + 1) % maxSize;
        count--;
        notifyAll();
        return file;
    }

    public synchronized void setFinished() {
        isFinished = true;
        notifyAll();
    }
}
