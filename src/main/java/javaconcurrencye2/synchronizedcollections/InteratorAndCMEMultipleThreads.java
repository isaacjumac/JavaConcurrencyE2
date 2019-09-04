package javaconcurrencye2.synchronizedcollections;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;

public class InteratorAndCMEMultipleThreads {
    public static void main(String... args) {
        List<String> strings = new ArrayList<>(Arrays.asList("a", "b", "c"));

        // Thread 1 try to add an element to the list every 30ms
        new Thread(() -> {
            while (true) {
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                }
                strings.add(LocalDateTime.now().toString());
            }
        }).start();

        // Thread 2 tries to print out the list every 40ms
        new Thread(() -> {
            while (true) {
                try {
                    sleep(40);
                } catch (InterruptedException e) {
                }
                System.out.println(strings.toString()); // .toString() uses iterator underneath, which is hidden to us!
            }
        }).start();
    }
}
