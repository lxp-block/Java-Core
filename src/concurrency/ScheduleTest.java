package concurrency;
//2018/11/7  21:52

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** TV每隔一段时间随机播放广告*/

class AD{
    public static final String ad1="APPLE";
    public static final String ad2="SUPERMARKET";
    public static final String ad3="ORACLE";
    public static final String ad4="COCO";
    public static final String ad5="HUAWEI";
}
class TVChannel implements Runnable{
    private int channelNum;
    private String name="CCTV-";
    private Random random=new Random();

    public TVChannel(int channelNum) {
        this.channelNum = channelNum;
    }

    public void run() {
            System.out.print(name+channelNum+" AD TIME>>>");
            switch (random.nextInt(5)){
                case 0: System.out.println(AD.ad1);break;
                case 1: System.out.println(AD.ad2);break;
                case 2: System.out.println(AD.ad3);break;
                case 3: System.out.println(AD.ad4);break;
                case 4: System.out.println(AD.ad5);break;
        }
    }
}

public class ScheduleTest {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor s=new ScheduledThreadPoolExecutor(1);
        for(int i=1;i<6;i++)
            s.scheduleAtFixedRate(new TVChannel(i),2,5,TimeUnit.SECONDS);
        s.scheduleAtFixedRate(new TVChannel(6),5,10,TimeUnit.SECONDS);
        System.out.println(s.getQueue());
    }
}
