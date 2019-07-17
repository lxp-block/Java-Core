package concurrency;
//2018/11/4  20:37

public class Uncaught implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(t.getName()+e);
    }
}

class ThrowE implements Runnable{

    @Override
    public void run() {
        //throw new RuntimeException();
        int i=10/0;
    }
}