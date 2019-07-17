package concurrency.simulation;


import java.util.HashSet;;
import java.util.Set;
import java.util.concurrent.*;

//2018/11/11  16:01
class Car {
    private final int id;
    private boolean
            engine = false, driveTrain = false, wheels = false;

    public Car(int idn) {
        id = idn;
    }

    public Car() {
        id = -1;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized void addEngine() {
        engine = true;
    }

    public synchronized void addDriveTrain() {
        driveTrain = true;
    }

    public synchronized void addWheels() {
        wheels = true;
    }

    public synchronized String toString() {
        return "Car " + id + " [" + " engine: " + engine
                + " driveTrain: " + driveTrain
                + " wheels: " + wheels + " ]";
    }
}

class CarQueue extends LinkedBlockingQueue<Car> {
}

abstract class Workers implements Runnable {
    Assembler assembler;
    private boolean isWork = false;
    private WorkersPool pool;

    public synchronized void toWork() {
        isWork = true;
        notifyAll();
    }

    public Workers(WorkersPool pool) {
        this.pool = pool;
    }

    public void setAssembler(Assembler assembler) {
        this.assembler = assembler;
    }

    abstract public void doWork() throws BrokenBarrierException, InterruptedException;

    public synchronized void toRest() {
        isWork = false;
        pool.add(this);
    }

    public synchronized void run() {
        try {
            while (!Thread.interrupted()) {
                    if (!isWork)
                       wait();
                    doWork();
                    toRest();
            }
        } catch (InterruptedException e) {
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }


}

class WheelWorker extends Workers {
    public WheelWorker(WorkersPool pool) {
        super(pool);
    }

    @Override
    public void doWork() throws BrokenBarrierException, InterruptedException {
        assembler.getCar().addWheels();
        assembler.getCyclicBarrier().await();
    }
}

class EngineWorker extends Workers {
    public EngineWorker(WorkersPool pool) {
        super(pool);
    }

    @Override
    public void doWork() throws BrokenBarrierException, InterruptedException {
        assembler.getCar().addEngine();
        assembler.getCyclicBarrier().await();
    }
}

class DrivenWorker extends Workers {
    public DrivenWorker(WorkersPool pool) {
        super(pool);
    }

    @Override
    public void doWork() throws BrokenBarrierException, InterruptedException {
        assembler.getCar().addDriveTrain();
        assembler.getCyclicBarrier().await();
    }
}

class WorkersPool {
    private Set<Workers> set = new HashSet<Workers>();
    public synchronized void add(Workers w) {
        set.add(w);
        notifyAll();
    }

    public synchronized void hireWorkers(Assembler assembler, Class<? extends Workers> c) throws BrokenBarrierException, InterruptedException {
        for (Workers w : set) {
            if (w.getClass().equals(c)) {
                w.setAssembler(assembler);
                w.toWork();
                set.remove(w);
                return;
            }
        }
        wait();
        hireWorkers(assembler, c);
    }
}

/**
 * 3个worker线程  1 个assembler线程   >> cyclic
 */
class ChassisMaker implements Runnable {
    private CarQueue queue;

    private int count=1;
    public ChassisMaker(CarQueue queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.SECONDS.sleep(2);
                Car car = new Car(count++);
                queue.put(car);
                //System.out.println("A NEW " + car);
            }
        } catch (InterruptedException e) {
            System.out.println("Chassis interrupted");
        }
    }
}

class Assembler implements Runnable {
    private CarQueue queue;
    private CarQueue finished;
    private WorkersPool pool;
    private Car car;
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(4);

    public Assembler(CarQueue queue, CarQueue finished, WorkersPool pool) {
        this.queue = queue;
        this.finished = finished;
        this.pool = pool;
    }

    public Car getCar() {
        return car;
    }

    public CyclicBarrier getCyclicBarrier() {
        return cyclicBarrier;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                car = queue.take();
                System.out.println("new car "+car);
                pool.hireWorkers(this, WheelWorker.class);
                pool.hireWorkers(this, EngineWorker.class);
                pool.hireWorkers(this, DrivenWorker.class);
                cyclicBarrier.await();
                System.out.println("car done " + car);
                finished.put(car);
            }
        } catch (InterruptedException e) {
            System.out.println("assembler interrupted");
        } catch (BrokenBarrierException e) {

        }
    }
}

class Shop implements Runnable {
    private CarQueue finished;

    public Shop(CarQueue finished) {
        this.finished = finished;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                Car car = finished.take();
                System.out.println("SHOP >> " +" car ヽ(✿ﾟ▽ﾟ)ノ "+ car.getId());
            }
        } catch (InterruptedException e) {
            System.out.println("shop closed");
        }

    }
}

public class CarFactory {
    public static void main(String[] args) {
        CarQueue carQueue = new CarQueue();
        CarQueue finishCar = new CarQueue();
        WorkersPool pool = new WorkersPool();
        ExecutorService e = Executors.newCachedThreadPool();
        ChassisMaker chassisMaker = new ChassisMaker(carQueue);
        Assembler assembler = new Assembler(carQueue, finishCar, pool);
        Shop shop = new Shop(finishCar);

        WheelWorker wheelWorker = new WheelWorker(pool);
        EngineWorker engineWorker = new EngineWorker(pool);
        DrivenWorker drivenWorker = new DrivenWorker(pool);

        pool.add(wheelWorker);
        pool.add(engineWorker);
        pool.add(drivenWorker);
        e.execute(chassisMaker);
        e.execute(assembler);
        e.execute(shop);
        e.execute(wheelWorker);
        e.execute(engineWorker);
        e.execute(drivenWorker);

    }
}
