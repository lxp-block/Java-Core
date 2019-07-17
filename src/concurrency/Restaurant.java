package concurrency;
//2018/11/6  11:34

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 同步对象（同步控制快）：多个线程引用同一个对象，只有一个线程可以使用该对象访问同步控制快内的代码。
  **/
class Meal {
    private final int orderNum;

    public Meal(int orderNum) {
        this.orderNum = orderNum;
    }

    public String toString() {
        return "Meal " + orderNum;
    }
}

class WaitPerson implements Runnable {
    private Restaurant restaurant;

    public WaitPerson(Restaurant r) {
        restaurant = r;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (restaurant.meal == null){
                        System.out.println("waiting...");
                        wait(); // ... for the chef to produce a meal

                }}
                System.out.println(("Waitperson got " + restaurant.meal));
                synchronized (restaurant.chef) {
                    restaurant.meal = null;
                    System.out.println("notify chef");
                    restaurant.chef.notifyAll(); // Ready for another
                }
            }
        } catch (InterruptedException e) {
            System.out.println(("WaitPerson interrupted"));
        }
    }
}

class Chef implements Runnable {
    private Restaurant restaurant;
    private int count = 0;

    public Chef(Restaurant r) {
        restaurant = r;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    while (restaurant.meal != null){
                        System.out.println("Chef waiting...");
                        wait(); // ... for the meal to be taken
                }}
                if (++count == 10) {
                    System.out.println(("Out of food, closing"));
                    restaurant.exec.shutdownNow();
                }
                System.out.println(("Order up! "));
                synchronized (restaurant.waitPerson) {
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.notifyAll();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println(("Chef interrupted"));
        }
    }
}

public class Restaurant {
    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);

    public Restaurant() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }

    public static void main(String[] args) {
        new Restaurant();
    }
}
