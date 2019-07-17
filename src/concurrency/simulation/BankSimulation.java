package concurrency.simulation;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Consumer {
    private final int id;
    private int serviceTime;
    public Consumer(int id, int serviceTime) {
        this.id = id;
        this.serviceTime = serviceTime;
    }
    public String toString() {
        return "Consumer # " + id;
    }
    public int getId() {
        return id;
    }
    public int getServiceTime() {
        return serviceTime;
    }
}

class ConsumerGenerator implements Runnable {
    private ArrayBlockingQueue<Consumer> consumers;
    private Random random = new Random();
    public ConsumerGenerator(ArrayBlockingQueue<Consumer> consumers) {
        this.consumers = consumers;
    }
    public void run() {
        while (!Thread.interrupted()) {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(800));
                consumers.put(new Consumer(random.nextInt(30), random.nextInt(1000)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Teller implements Runnable {
    private static int count = 0;
    private final int tellerID = ++count;
    private int serviceCount = 0;
    private ArrayBlockingQueue<Consumer> consumers;

    public Teller(ArrayBlockingQueue<Consumer> consumers) {
        this.consumers = consumers;
    }
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Consumer c = consumers.take();
                TimeUnit.MILLISECONDS.sleep(c.getServiceTime());
                System.out.println("NO. " + tellerID + " serviced " + (++serviceCount) + " people ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class TellerManager {

}

public class BankSimulation {
    public static void main(String[] args) {
        ArrayBlockingQueue<Consumer> consumers = new ArrayBlockingQueue<Consumer>(10);
        ExecutorService e = Executors.newCachedThreadPool();
        e.execute(new ConsumerGenerator(consumers));
        e.execute(new Teller(consumers));
        e.execute(new Teller(consumers));
        e.execute(new Teller(consumers));
        e.execute(new Teller(consumers));
        e.execute(new Teller(consumers));
    }
}
