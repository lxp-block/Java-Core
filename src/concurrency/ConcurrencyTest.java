package concurrency;
//2018/11/4  10:45

import org.junit.Test;
import java.util.ArrayList;
import java.util.concurrent.*;


/**
 * 线程的创建方式，对临界资源的访问的同步与控制
 */
public class ConcurrencyTest {


    //使用 Executor 创建和管理线程
    // CachedThreadPool 在程序执行中创建所需数量的线程，并且在回收旧线程时停止创建新线程。
    @Test
    public static void test1() {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++)
            executor.execute(new LiftOff());
        executor.shutdown();//可以防止新任务的提交
        //executor.execute(new LiftOff());
        System.out.println(Thread.currentThread());
    }


    //FixedThreadPool
    //任何线程池中，现有线程在可能的情况下，都会被自动复用
    @Test
    public static void test2() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 9; i++)
            pool.execute(new LiftOff());
        pool.shutdown();//可以防止新任务的提交
        System.out.println(Thread.currentThread());
    }

    //SingleThreadExecutor:线程数量为1的FixedThreadPool,如果提交了多个任务，南无这些任务将会排队
    //SingleThreadExecutor会序列化提交给它的任务，维护自己的悬挂任务队列。
    @Test
    public static void test3() {
        Executor executor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++)
            executor.execute(new LiftOff());
    }
    /*
      #0:9  #0:8  #0:7  #0:6  #0:5  #0:4  #0:3  #0:2  #0:1  #0:LIFT-OFF
      #1:9  #1:8  #1:7  #1:6  #1:5  #1:4  #1:3  #1:2  #1:1  #1:LIFT-OFF
      #2:9  #2:8  #2:7  #2:6  #2:5  #2:4  #2:3  #2:2  #2:1  #2:LIFT-OFF
      #3:9  #3:8  #3:7  #3:6  #3:5  #3:4  #3:3  #3:2  #3:1  #3:LIFT-OFF
      #4:9  #4:8  #4:7  #4:6  #4:5  #4:4  #4:3  #4:2  #4:1  #4:LIFT-OFF
      */

    @Test
    public static void test4() {
        for (int i = 0; i < 5; i++)
            new Thread(new LiftOff()).start();
    }


    //有返回值的线程
    @Test
    public static void test5() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ArrayList<Future<String>> result = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            result.add(executor.submit(new ResultThread(i)));
        System.out.println(result.get(1).isDone());
        System.out.println(Thread.currentThread().getPriority());
        TimeUnit.MILLISECONDS.sleep(500);
        for (Future<String> future : result) {
            if (future.isDone()) {
                System.out.println(future.get());
            }
        }
    }


    @Test
    public static void test6() {
        ExecutorService service = Executors.newCachedThreadPool(new DaemonThreadFactory());
        for (int i = 0; i < 5; i++)
            service.execute(new LiftOff());
        Thread.yield();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
    }


    //UncaughtExceptionHandlerFactory
    public static void test7() {
        ExecutorService service = Executors.newCachedThreadPool(new UncaughtExceptionFactory());
        service.execute(new ThrowE());
    }

    //使用关键字 synchronized
    public static void test8() {
        IntGenerator ig = new IntGenerator();
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++)
            service.execute(new CheckNum(ig));
    }

    //使用 Lock对象
    public static void test9() {
        NumGenerator ng = new IntGenByLock();
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++)
            service.execute(new CheckNum(ng));
    }


    //同步对象
    public static void test10() {
        IntGenSynObj gen = new IntGenSynObj();
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++)
            service.execute(new CheckIntGenSynObj(gen));
    }

    //ThreadLocal 线程本地存储
    public static void test11() {
        ThreadLocalHolder holder = new ThreadLocalHolder();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++)
            executor.execute(new TestThreadLocal(holder));
        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdownNow();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //test1();
        //test2();
        //test3();
        //test5();
        //test6();
        //test7();
        //test8();
        //test9();
        //test10();
        test11();
    }
}
