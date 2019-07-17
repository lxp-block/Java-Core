package concurrency;
//2018/11/8  9:38

import java.util.concurrent.*;

class DelayWork implements Runnable, Delayed {
    private int id;
    public long delayTime;
    DelayQueue<DelayWork> queue;

    public DelayWork(int id, long delayTime, DelayQueue<DelayWork> q) {
        this.id = id;
        this.delayTime = delayTime+TimeUnit.SECONDS.convert(System.nanoTime(),TimeUnit.NANOSECONDS);
        q.add(this);
    }

    public long getDelay(TimeUnit unit) {
        long i=TimeUnit.SECONDS.convert(System.nanoTime(),TimeUnit.NANOSECONDS);
        return delayTime-i;
    }

    public int compareTo(Delayed o) {
        DelayWork d = (DelayWork) o;
        return delayTime < d.delayTime ? -1 : (delayTime == d.delayTime ? 0 : 1);
    }

    public void run() {
        System.out.println("#" + id);
    }
}

class GetDelay implements Runnable {
    DelayQueue<DelayWork> q;

    public GetDelay(DelayQueue<DelayWork> q) {
        this.q = q;
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                System.out.println("take&run...");
                q.take().run();//队列为空时阻塞
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class DelayedTest {
    public static void main(String[] args) {
        DelayQueue<DelayWork> queue = new DelayQueue<>();
        ExecutorService e = Executors.newCachedThreadPool();
        e.execute(new DelayWork(1, 10, queue));
        e.execute(new DelayWork(2, 6, queue));
        e.execute(new DelayWork(3, 7, queue));
        e.execute(new DelayWork(4, 8, queue));
        e.execute(new DelayWork(5, 2, queue));
        e.execute(new DelayWork(6, 4, queue));
        e.execute(new GetDelay(queue));
    }
}
