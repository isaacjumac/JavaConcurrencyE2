package javaconcurrencye2.concurrentcontainers;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.Thread.sleep;

/**
 * <pre>
 * <b>theCandyAddict</b>, as his name indicates, loves candy and eat them very fast at a mind-blowing speed: 300ms/candy!
 * <b>theGoodGuy</b> doesn't have candy addiction and eats at a slower rate of 600ms/candy.
 * Each of them have a candy box, and they will always eat the candy that's put in the box first (assume they have that kind of OCD).
 * <b>theMom</b> will put a candy in each candy box every other while. Mom is fair, so she will distribute her 40 candies evenly.
 * <b>theCandyAddict</b> will steal the last-put candy (so that he will not be noticed) from <b>theGoodGuy</b>'s candy box when he finds his own candy box is empty! <b>theGoodGuy</b> won't do that.
 * </pre>
 *
 * @author Zhu Zhaohua (Isaac)
 */
public class NaughtyCandyAddict {
    static BlockingDeque[] candyBoxes;
    static CountDownLatch startCountDown = new CountDownLatch(1);
    static CountDownLatch candyConsumptionCountDown = new CountDownLatch(20 * 2);

    static {
        BlockingDeque<Integer> candyAddictsBox = new LinkedBlockingDeque(10);
        BlockingDeque<Integer> goodGuysBox = new LinkedBlockingDeque<>(10);
        candyBoxes = new BlockingDeque[] { candyAddictsBox, goodGuysBox };
    }

    public static void main(String[] args) throws InterruptedException {
        Random r = new Random();
        Thread theMom = new Thread(() -> {
            int candyNo = 1;
            while (true) {
                try {
                    startCountDown.await();
                    sleep(r.nextInt(1000));
                    for (BlockingDeque<Integer> candyBox : candyBoxes) {
                        candyBox.put(candyNo); // from the tail
                    }
                    candyNo++;
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        Thread theCandyAddict = new Thread(() -> {
            while (true) {
                int candyToEat = 0;
                try {
                    startCountDown.await();

                    sleep(300);
                    BlockingDeque<Integer> ownCandybox = candyBoxes[0];
                    if (!ownCandybox.isEmpty()) {
                        candyToEat = ownCandybox.take(); // from head
                        System.out.println("[Candy Addict] is eating his own candy: " + candyToEat);
                    } else {
                        BlockingDeque<Integer> goodguysBox = candyBoxes[1];
                        candyToEat = goodguysBox.takeLast(); // from tail
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

        Thread theGoodGuy = new Thread(() -> {
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

        theMom.start();
        theCandyAddict.start();
        theGoodGuy.start();
        startCountDown.countDown();
        candyConsumptionCountDown.await();
        theMom.interrupt();
        theCandyAddict.interrupt();
        theGoodGuy.interrupt();
    }
}
