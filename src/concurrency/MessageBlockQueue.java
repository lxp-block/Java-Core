package concurrency;
//2018/11/6  19:42

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

class Message {
    public static int messageId = 0;
    private int message;

    public Message() {
        message = new Random().nextInt(100);
    }
    public static void setid(){
        ++messageId;
    }
    public String getMessage() {
        return "MESSAGE:" + messageId + ">" + message;
    }
}

class MessageWriter implements Runnable {
    public MessageWriter(LinkedBlockingQueue<Message> queue) {
        this.queue = queue;
    }

    LinkedBlockingQueue<Message> queue;

    private int count;
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Message m = new Message();
                Message.setid();
                System.out.println("write message:" +m.messageId);
                queue.put(m);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("MessageWriter shutdown");
        }
        System.out.println("stop writing");
    }
}

class MessageReader implements Runnable {
    LinkedBlockingQueue<Message> queue;

    public MessageReader(LinkedBlockingQueue<Message> queue) {
        this.queue = queue;
    }

    public void run() {
        try {

            while (!Thread.interrupted()) {
                Message m = queue.take();
                System.out.println(m.getMessage());
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("MessageReader shutdown");
        }
        System.out.println("stop reading");
    }
}

public class MessageBlockQueue {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new MessageReader(queue));
        executor.execute(new MessageReader(queue));
        executor.execute(new MessageWriter(queue));
        executor.execute(new MessageWriter(queue));
        TimeUnit.MILLISECONDS.sleep(500);
        executor.shutdownNow();
    }
}
/*
    write message:1
        MESSAGE:1>1
        write message:2
        MESSAGE:2>75
        write message:3
        write message:4
        MESSAGE:4>52
        write message:5
        write message:6
        MESSAGE:6>55
        write message:7
        write message:8
        MESSAGE:8>66
        write message:9
        write message:10
        MessageWriter shutdown
        MessageReader shutdown
        stop writing
        stop reading*/
