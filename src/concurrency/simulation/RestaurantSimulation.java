package concurrency.simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

class Food {
    public final static String[] foods = {"apple", "beer", "roast duck", "rice", "egg", "water", "beef", "dumplings"};
}

class CookedFood {
    private Customer customer;
    private String foodName;

    @Override
    public String toString() {
        return "CookedFood{" +
                customer +
                ", foodName='" + foodName + '\'' +
                '}';
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getFoodName() {
        return foodName;
    }

    public CookedFood(Customer customer, String foodName) {
        this.customer = customer;
        this.foodName = foodName;
    }
}

class Order {
    public Customer customer;
    private List<String> foods = new ArrayList<>();
    public Waiter waiter;

    public Order(Customer customer) {
        this.customer = customer;
    }

    public void addFoodsItems(int i) {
        foods.add(Food.foods[i]);
    }

    public String getItem(int i) {
        return foods.get(i);
    }

    public int getNum() {
        return foods.size();
    }

    @Override
    public String toString() {
        return "Order{" +
                customer +
                ", foods=" + foods +
                '}';
    }
}

class Waiter implements Runnable {
    BlockingDeque<CookedFood> foodList;
    private static int count = 0;
    private final int id = ++count;

    public Waiter(BlockingDeque<CookedFood> foodList) {
        this.foodList = foodList;
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                CookedFood food = foodList.take();
                System.out.println("waiter #" + id + " take " + food);
                food.getCustomer().okFood.put(food.getFoodName());
            } catch (InterruptedException e) {
            }
        }
    }
}

class Chef implements Runnable {
    private static int count = 0;
    private final int id = ++count;
    Order order;
    BlockingDeque<Order> deque;
    BlockingDeque<CookedFood> CookedFood;

    public Chef(BlockingDeque<Order> deque, BlockingDeque<CookedFood> okDeque) {
        this.deque = deque;
        this.CookedFood = okDeque;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                order = deque.take();
                for (int i = 0; i < order.getNum(); i++) {
                    System.out.println("chef # " + id + " cooking " + order.getItem(i));
                    TimeUnit.MILLISECONDS.sleep(3000);
                    CookedFood.put(new CookedFood(order.customer, order.getItem(i)));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Customer implements Runnable {
    private static int count = 0;
    public final int id = ++count;
    private Random random = new Random();
    private Order order = new Order(this);
    int foodNum = random.nextInt(3) + 1;
    BlockingDeque<Order> orderList;
    StringBuilder s = new StringBuilder();
    SynchronousQueue<String> okFood = new SynchronousQueue<>();
    BlockingDeque<Order> myOrder = new LinkedBlockingDeque<>();

    public Customer(BlockingDeque<Order> deque) throws InterruptedException {
        this.orderList = deque;
        for (int i = 0; i < foodNum; i++)
            order.addFoodsItems(random.nextInt(8));
        System.out.println(order);
        deque.put(order);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                '}';
    }

    public void run() {
        int i = 0;
        while (i < foodNum) {
            try {
                System.out.println("C- " + id + " got " + okFood.take());
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        s.append("CUS-").append(id).append("   left>>>>>");
       /* for (int star = 0; i < random.nextInt(6); star++) {
         s.append("★");
        }*/
        System.out.println(s);
    }
}

class CustomerGen implements Runnable {
    private Random random = new Random();
    BlockingDeque<Order> deque;

    public CustomerGen(BlockingDeque<Order> deque) {
        this.deque = deque;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                if (deque.size() < 5) {
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(5000));
                    new Thread(new Customer(deque)).start();
                } else {
                    System.out.println("TOO MANY ORDERS,PLEASE WAIT..................................");
                    TimeUnit.SECONDS.sleep(5);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Cashier {
}

public class RestaurantSimulation {
    public static void main(String[] args) throws IOException {
        BlockingDeque<Order> deque = new LinkedBlockingDeque<>();
        BlockingDeque<CookedFood> okdeque = new LinkedBlockingDeque<>();
        ExecutorService s = Executors.newCachedThreadPool();
        s.execute(new CustomerGen(deque));
        s.execute(new Chef(deque, okdeque));
        s.execute(new Chef(deque, okdeque));
        s.execute(new Waiter(okdeque));
        s.execute(new Waiter(okdeque));
        s.execute(new Waiter(okdeque));


        System.out.println("PRESS ENTER TO CLOSE RESTAURANT");
        System.in.read();
        s.shutdownNow();
        System.out.println("RESTAURANT CLOSED ");
    }
}
