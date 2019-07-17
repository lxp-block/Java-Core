package concurrency;
//2018/11/8  15:53


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*  @# 1 #@     class Pool<T> {
    private final Semaphore semaphore;
    private final boolean[] used;
    private int MAX_SIZE;
    private List<T> list = new ArrayList<>();

    Pool(Semaphore semaphore, int max, Class<T> obj) throws IllegalAccessException, InstantiationException {
        this.semaphore = semaphore;
        MAX_SIZE = max;
        used = new boolean[MAX_SIZE];
        for (int i = 0; i < max; i++) {
            list.add(obj.newInstance());
        }
    }

    public synchronized T getItem() throws InterruptedException {
        semaphore.acquire();
        for (int i = 0; i < used.length; ++i)
            if (!used[i]) {
                used[i] = true;
                return list.get(i);
            }
        return null;
    }

    public synchronized void addItem(T t) {
        for (int i = 0; i < used.length; ++i)
            if (used[i]) {
                list.add(i, t);
            }
        semaphore.release();
    }
}

class PeopleUseBike implements Runnable {
    int pid;
    Pool p;
    Bike b;
    public PeopleUseBike(int pid, Pool p) {
        this.pid = pid;
        this.p = p;
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                b= (Bike) p.getItem();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("# " + pid + "riding bike");
            p.addItem(b);
            System.out.println("put down");
        }
    }
}

class Bike {
    static int id;

    Bike() {
        id++;
        System.out.println("preparing a bike # " + id);
    }
}*/


class Count implements Runnable {
    Semaphore s;

    public Count(Semaphore s) {
        this.s = s;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                s.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"got");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            s.release();
            System.out.println(Thread.currentThread().getName()+"released");
        }
    }
}


public class SemaphoreTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException {

/*  @# 1 #@      int max = 10;
        Semaphore semaphore = new Semaphore(max, true);
        ExecutorService executorService = Executors.newCachedThreadPool();
        Pool<Bike> pool = new Pool<Bike>(semaphore, max, Bike.class);
        for (int i = 0; i < 20; i++)
            executorService.execute(new PeopleUseBike(i, pool));
    }*/

        Semaphore s = new Semaphore(1, true);
        new Thread(new Count(s)).start();
        new Thread(new Count(s)).start();
        new Thread(new Count(s)).start();
    }
}