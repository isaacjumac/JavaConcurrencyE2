package javaconcurrencye2.synchronizers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;


/**
 * This is a demo to simulate some kids trying to play Ping Pong when there's only 1 table:<br>
 * <ul>
 *     <li>There's only 1 table, so every single point of time there can be at most 2 kids playing. Controlled by the {@linkplain Semaphore} <code>s</code></li>
 *     <li>No kid can start playing until both are ready. This is controlled by <code>startBarrier</code>;</li>
 *     <li>A game is only over when both kid finish playing (not the case, but anyway). This is controlled by <code>endBarrier</code>;</li>
 *     <li>After game ends, another 2 kids can play. I.e., <code>startBarrier</code> is reset;</li>
 * </ul>
 *
 * @author Zhu Zhaohua (Isaac)
 */
import static java.lang.Thread.sleep;

public class PlayPingPong {
    static Semaphore s = new Semaphore(2);
    static CyclicBarrier startBarrier = new CyclicBarrier(2);
    static CyclicBarrier endBarrier = new CyclicBarrier(2);
    static List<Integer> kidsPlaying = new ArrayList<>();

    static void play(int numberOfKids){
        for (int i = 0; i < numberOfKids; i++) {
            int kid = i + 1;
            new Thread(() -> {
                try {
                    s.acquire();
                    kidsPlaying.add(kid);
                    startBarrier.await();
                    System.out.println("Kid " + kid + " is playing.");
                    sleep(500);
                } catch (InterruptedException | BrokenBarrierException e) {
                } finally {
                    try {
                        kidsPlaying.remove((Integer)kid);
                        s.release();
                        endBarrier.await();
                        System.out.println();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        play(10);
    }
}
