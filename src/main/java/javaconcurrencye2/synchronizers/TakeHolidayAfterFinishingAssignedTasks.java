package javaconcurrencye2.synchronizers;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static java.lang.Thread.sleep;

/**
 * Simply me can't wait to take holiday but I have to make sure my assigned tasks are finished (i.e., <code>if (finishTasks.get())</code>)
 *
 * @author Zhu Zhaohua (Isaac)
 */
public class TakeHolidayAfterFinishingAssignedTasks {
    static int taskCount = 10;

    static boolean work() throws InterruptedException {
        Random r = new Random();
        for (int i = 1; i <= taskCount; i++) {
            String status = "Woking on task " + i;
            if (i == 10) {
                status += ". Last 1, last1, last 1!";
            } else if (i >= 8) {
                status += ". Oh god I'm freaking breaking down.";
            } else if (i >= 5) {
                status += ". Reaching my limit.";
            }

            System.out.println(status);
            sleep(r.nextInt(2000));
            System.out.println("Completed task " + i + "\n");
        }
        return true;
    }

    public static void main(String[] args) {
        FutureTask<Boolean> finishTasks = new FutureTask<>(() -> work());
        Thread meAfterWorkingWithoutBreakForManyIterations = new Thread(finishTasks);
        System.out.println("I'm so freaking tired. I need a break. But I still have tasks to complete...");
        meAfterWorkingWithoutBreakForManyIterations.start();

        try {
            if (finishTasks.get()) {
                System.out.println("Hooray! HOLIDAY TIME! I'm FREEEEE!");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
