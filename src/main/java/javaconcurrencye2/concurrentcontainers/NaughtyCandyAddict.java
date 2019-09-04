package javaconcurrencye2.concurrentcontainers;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.Thread.sleep;

public class NaughtyCandyAddict {
    static BlockingDeque[] candyBoxes;
    static CountDownLatch startCountDown = new CountDownLatch(1);
    static CountDownLatch candyConsumptionCountDown = new CountDownLatch(20*2);

    static {
        BlockingDeque<Integer> candyAddictsBox = new LinkedBlockingDeque(10);
        BlockingDeque<Integer> goodGuysBox = new LinkedBlockingDeque<>(10);
        candyBoxes = new BlockingDeque[] { candyAddictsBox, goodGuysBox };
    }

    public static void main(String[] args) throws InterruptedException {
        Random r = new Random();
        Thread candyDispatcher = new Thread(() -> {
            int candyNo = 1;
            while (true) {
                try {
                    startCountDown.await();
                    sleep(r.nextInt(1000));
                    for (BlockingDeque<Integer> candyBox : candyBoxes) {
                        candyBox.put(candyNo);
                    }
                    candyNo++;
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        Thread candyAddict = new Thread(() -> {
            while (true) {
                int candyToEat = 0;
                try {
                    startCountDown.await();

                    sleep(300);
                    BlockingDeque<Integer> ownCandybox = candyBoxes[0];
                    if (!ownCandybox.isEmpty()) {
                        candyToEat = ownCandybox.take();
                        System.out.println("[Candy Addict] is eating his own candy: " + candyToEat);
                    } else {
                        BlockingDeque<Integer> goodguysBox = candyBoxes[1];
                        candyToEat = goodguysBox.takeLast();
                        System.out.println("[Candy Addict] Oh the naughty Candy Addict stole candy "
                                + candyToEat
                                + " from Good Guy!");
                    }
                    candyConsumptionCountDown.countDown();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        Thread goodGuy = new Thread(() -> {
            while (true) {
                int candyToEat = 0;
                try {
                    startCountDown.await();

                    sleep(600);
                    BlockingDeque<Integer> ownCandybox = candyBoxes[1];
                    candyToEat = ownCandybox.take();
                    System.out.println("[Good Guy] is eating his candy: " + candyToEat);
                    candyConsumptionCountDown.countDown();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        candyDispatcher.start();
        candyAddict.start();
        goodGuy.start();
        startCountDown.countDown();
        candyConsumptionCountDown.await();
        candyDispatcher.interrupt();
        candyAddict.interrupt();
        goodGuy.interrupt();
    }
}
