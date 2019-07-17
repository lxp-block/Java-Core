package concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

abstract class NumGenerator {
    protected int num;
    protected int evenNum;

    abstract public int next();

    synchronized public int getEvenNum() {
        return evenNum;
    }
}

class IntGenerator extends NumGenerator {
    //synchronized
    public int next() {
        evenNum++;
        Thread.yield();
        evenNum++;
        return evenNum;
    }
}

class IntGenByLock extends NumGenerator {
    private Lock lock = new ReentrantLock();

    @Override
    public int next() {
        lock.lock();
        try {
            evenNum++;
            Thread.yield();
            evenNum++;
            return evenNum;
        } finally {
            lock.unlock();
        }
    }

    public int getEvenNum() {
        lock.lock();
        try {
            return evenNum;
        } finally {
            lock.unlock();
        }
    }
}

class IntGenSynObj extends NumGenerator {

    private int num2 = 0;

    public boolean eq() {
        return num == evenNum && evenNum == num2;
    }

    public int next() {
        //synchronized (this) {
        num++;
   /*     try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
       // Thread.yield();
        num2++;
        //Thread.yield();
        evenNum++;
        return 0;
        //}

    }
}

class CheckIntGenSynObj implements Runnable {
    private IntGenSynObj gen;

    public CheckIntGenSynObj(IntGenSynObj gen) {
        this.gen = gen;
    }

    private static int count = 0;

    public void run() {
        while (gen.eq()) {
            gen.next();
            System.out.println("count:" + count++ + ":" + gen.num + "--" + gen.evenNum + Thread.currentThread().getName());
        }

    }
}

public class CheckNum implements Runnable {

    private final NumGenerator generator;

    CheckNum(NumGenerator generator) {
        this.generator = generator;
    }

    public void run() {
        while (generator.next() % 2 == 0)
            System.out.println(Thread.currentThread().getName() + ":" + generator.getEvenNum());
    }
}


