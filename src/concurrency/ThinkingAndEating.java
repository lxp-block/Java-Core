package concurrency;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**哲学家就餐*/

class Chopsticks {
    public int id;
    private boolean taken = false;
    public Chopsticks(int id) {
        this.id = id;
    }
    synchronized public void take() {
        while (taken) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        taken=true;
    }
    synchronized public void drop() {
        taken = false;
        notifyAll();
    }
}

class Philosopher implements Runnable {
    private Chopsticks left;
    private Chopsticks right;
    private int pid;
    private Random random = new Random();

    public Philosopher(Chopsticks left, Chopsticks right, int pid) {
        this.left = left;
        this.right = right;
        this.pid = pid;
    }

    private void thinking() {
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void eating() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while (!Thread.interrupted()) {
            System.out.println("philosopher # " + pid + " is thinking...");
            thinking();
            left.take();
            System.out.println("#"+pid+" taking left "+left.id);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            right.take();
            System.out.println("#"+pid+" taking right "+right.id);
            System.out.println("philosopher # " + pid + " is eating...");
            eating();
            left.drop();
            right.drop();
        }
    }
}

public class ThinkingAndEating{
    public static void main(String[] args) {
        Chopsticks[] chopsticks=new Chopsticks[5];
        Philosopher[] philosophers=new Philosopher[5];
        for (int i=0;i<chopsticks.length;i++)
            chopsticks[i]=new Chopsticks(i);
        ExecutorService executor= Executors.newCachedThreadPool();
        for (int i=0;i<philosophers.length-1;i++) {
            philosophers[i]=new Philosopher(chopsticks[i],chopsticks[(i+1)%chopsticks.length],i);
            executor.execute(philosophers[i]);
        }
        philosophers[4]=new Philosopher(chopsticks[0],chopsticks[4],4);
        executor.execute(philosophers[4]);
        //executor.shutdownNow();
        //System.out.println("ALL STOP");
    }
}