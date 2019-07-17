package concurrency;
//2018/11/5  19:08

import java.util.Random;

public class ThreadLocalHolder {
    private static ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
        private Random random = new Random();

        public Integer initialValue() {
            return random.nextInt(10);
        }
    };

    public void increment() {
        value.set(value.get() + 1);
    }

    public int get() {
        return value.get();
    }
}

class TestThreadLocal implements Runnable {

    private ThreadLocalHolder holder;

    public TestThreadLocal(ThreadLocalHolder holder) {
        this.holder = holder;
    }

    public void run() {
        while (holder.get() < 100) {
            System.out.println(Thread.currentThread().getName() + ":" + holder.get());
            holder.increment();
        }
    }
}