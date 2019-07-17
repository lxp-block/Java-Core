package concurrency;
//2018/11/6  21:52

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class PipedR implements Runnable {
    private PipedWriter writer;
    private PipedReader reader;

    public PipedR(PipedWriter writer) throws IOException {
        this.writer = writer;
        reader = new PipedReader(writer);
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                char c= (char) reader.read();
                if(c=='a')
                    System.out.println();
                System.out.print(c);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class PipedW implements Runnable {
    private PipedWriter out;

    public PipedW(PipedWriter out) {
        this.out = out;
    }

    public void run() {
        while (!Thread.interrupted()) {
            for (char c = 'a'; c <= 'z'; c++) {
                try {
                    out.write(c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

public class PipedTest {
    public static void main(String[] args) throws IOException {
        PipedWriter w=new PipedWriter();
        ExecutorService executor=Executors.newCachedThreadPool();
        executor.execute(new PipedR(w));
        executor.execute(new PipedW(w));
    }
}
