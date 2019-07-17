package concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//2018/11/6  9:16
class BlockedMutex {
    private Lock lock = new ReentrantLock();
    public BlockedMutex() {
        // Acquire it right away, to demonstrate interruption
        // of a task blocked on a ReentrantLock:
        System.out.println("Thread : "+Thread.currentThread().getName());
        lock.lock();
    }
    public void f() {
        try {
            // This will never be available to a second task
            lock.lockInterruptibly(); // Special call
            System.out.println(("lock acquired in f()"));
        } catch(InterruptedException e) {
            System.out.println(("Interrupted from lock acquisition in f()"));
        }
    }
}

class Blocked2 implements Runnable {
    BlockedMutex blocked = new BlockedMutex();
    public void run() {
        System.out.println(Thread.currentThread().getName()+"Waiting for f() in BlockedMutex");
        blocked.f();
        System.out.println(("Broken out of blocked call"));
    }
}

public class InterruptTest {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new Blocked2());
        t.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Issuing t.interrupt()");
        t.interrupt();
    }
}
