package javaconcurrencye2.synchronizers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

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
