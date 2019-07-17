package generic;
//2018/10/25  21:22

import java.util.ArrayList;
import java.util.Random;

/*
用于存入对象，并随机取得存入的对象
 */
public class RandomList<T> {
    private ArrayList<T> list = new ArrayList<T>();

    public void add(T t) {
        list.add(t);
    }
    public T getRandomElement() {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
