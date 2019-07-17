package concurrency;
//2018/11/7  20:18

import java.util.concurrent.*;

class WaitTask implements Runnable {
    private CyclicBarrier barrier;
    private int id;
    private int time = 0;

    public WaitTask(CyclicBarrier barrier, int id) {
        this.barrier = barrier;
        this.id = id;
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("#" + id + "..." + time+++".OK");
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}

class BarrierTask implements Runnable {
    private int time = 0;
    public void run() {
        System.out.println("ALL DONE " + (time++) + " times");
    }
}

public class CyclicBarrierTest {
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier c=new CyclicBarrier(5,new BarrierTask());
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i=0;i<5;i++)
            executor.execute(new WaitTask(c,i));
        //TimeUnit.MILLISECONDS.sleep(500);
        //executor.shutdownNow();
    }

}
