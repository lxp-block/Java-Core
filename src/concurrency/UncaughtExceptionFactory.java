package concurrency;
//2018/11/4  20:39

import java.util.concurrent.ThreadFactory;

public class UncaughtExceptionFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        Thread t=new Thread(r);
        t.setUncaughtExceptionHandler(new Uncaught());
        return t;
    }
}
