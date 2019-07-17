package concurrency;
//2018/11/7  15:49

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 使用 CountDownLatch
 */

class Work implements Runnable {
    CountDownLatch latch;

    public Work(CountDownLatch latch) {
        this.latch = latch;
    }

    void doWork() {
        try {
            System.out.println(Thread.currentThread().getName() + " WORKING...");
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (!Thread.interrupted()) {
            synchronized (latch) {
                if (latch.getCount() > 0) {
                    doWork();
                    latch.countDown();
                    System.out.println(latch.getCount());
                } else
                    Thread.currentThread().interrupt();
            }
        }
    }
}

class WaitForWork implements Runnable {
    CountDownLatch latch;

    public WaitForWork(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                System.out.println(">>Interrupted<<");
            }
            System.out.println("WORK DONE");
            Thread.currentThread().interrupt();
        }
    }
}

public class LatchTest {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(50);
        new Thread(new Work(latch)).start();
        new Thread(new Work(latch)).start();
        new Thread(new WaitForWork(latch)).start();
        new Thread(new WaitForWork(latch)).start();
    }
}
