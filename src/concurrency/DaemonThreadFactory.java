package concurrency;
//2018/11/4  16:08

import java.util.concurrent.ThreadFactory;

public class DaemonThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        Thread thread=new Thread(r);
        //thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        return thread;
    }
}
