package concurrency;
//2018/11/4  10:37

public class LiftOff implements Runnable{

    private int countDown=10;
    private static int task=0;
    private final int taskId=task++;

    public LiftOff() {
        System.out.println("liftoff"+Thread.currentThread());
    }

    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status(){
        return "#"+taskId+":"+(countDown>0?countDown:"LIFT-OFF");
    }

    @Override
    public void run() {
        while (countDown-->0){
            System.out.print(status()+"  ");
            Thread.yield();
        }
    }
}
