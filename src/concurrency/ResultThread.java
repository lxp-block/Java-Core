package concurrency;
//2018/11/4  12:47

import java.util.concurrent.Callable;

public class ResultThread implements Callable<String> {

    private int num;

    public ResultThread() {
    }

    public ResultThread(int num) {
        this.num = num;
    }

    @Override
    public String call() {
        return "NUMBER."+num;
    }
}
