package main.java.javaconcurrencye2.synchronizedcollections;

import java.util.Vector;

import static java.lang.Thread.sleep;

public class CompondActionOnSynchronizedCollections {
    public static Vector<Integer> v = new Vector<>();

    // Initialize the vector
    static {
        for (int i = 0; i < 10; i++) {
            v.add(i);
        }
    }

    // Try to get last in the check-then-get style
    static Integer getLast() {
        try {
            int lastIndex = v.size() - 1; // Check
            sleep(1000); // Sleep for 1s to simulate any potential processing time / latency in between
            return v.get(lastIndex); // get failed because the last element deleted alr.
        } catch (InterruptedException e) {
            System.out.println("Interrupted!");
        }
        return -1;
    }

    // Delete the last element
    static void deleteLast() {
        int lastIndex = v.size() - 1;
        v.remove(lastIndex);
    }

    public static void main(String... args) {
        new Thread(() -> getLast()).start(); // thread 1 to try to get last element
        new Thread(() -> deleteLast()).start(); // thread 2 to try to remove last element
    }
}
