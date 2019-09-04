package javaconcurrencye2.synchronizers;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

public class HorseRace {
    volatile static int winner = 0;

    static void race(int numberOfHourses) {
        final CountDownLatch startLatch = new CountDownLatch(numberOfHourses);
        final CountDownLatch endLatch = new CountDownLatch(numberOfHourses);

        Random r = new Random();
        try {
            for (int i = 1; i <= numberOfHourses; i++) {
                int timeToGetReady = r.nextInt(1000);
                int horseNo = i;

                sleep(timeToGetReady);
                System.out.println("After " + timeToGetReady + "s. Horse no " + horseNo + " is ready.");

                // This is a prepared horse!
                new Thread(() -> {
                    try {
                        startLatch.await();

                        System.out.println("Horse no " + horseNo + " is running. Start time: " + LocalDateTime.now().toString());
                        sleep(r.nextInt(5000));

                        if (winner == 0) {
                            winner = horseNo;
                        }
                        System.out.println("Horse no " + horseNo + " crossed the line!");

                        endLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();

                startLatch.countDown(); // 1 more horse ready, count down
            }

            endLatch.await();
            System.out.println("\n\n" +
                    "Race finished! Winner is " + winner + ".\n\n" +
                    "Oh man, what a game!\n" +
                    "It's over. Race is not gonna happen again today. Let's go home...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        race(10);
    }
}
