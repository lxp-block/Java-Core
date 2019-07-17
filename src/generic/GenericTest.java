package generic;
//2018/10/25  19:58

import beans.Book;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericTest {

    //Holder
    @Test
    public void test1() {
        Holder<Book> holder = new Holder(new Book("story", 4.5),
                new Book("Readers", 5.0),
                20);
        Book book = holder.getA();
        book = holder.getC();//no compiler wrong
        holder.setC(new Book());
        //Book book2=holder.getC();//runtime wrong
    }


    //Tuple
    @Test
    public void test2() {
        Tuple t = getTuple();
    }

    private Tuple getTuple() {
        Tuple<Book, String, Integer> tuple = new Tuple(new Book("abc", 20), "holle", 2);
        return tuple;
    }

    //RandomList
    @Test
    public void test3() {
        RandomList randomList = new RandomList<String>();
        for (String s : "i have a book".split(" "))
            randomList.add(s);
        System.out.println(randomList.getRandomElement());
        System.out.println(randomList.getRandomElement());
        System.out.println(randomList.getRandomElement());
    }

    //擦除 Erasure
    @Test
    public void test4() {
        Class class1 = new ArrayList<String>().getClass();
        Class class2 = new ArrayList<Book>().getClass();
        System.out.println(Arrays.toString(new ArrayList<Integer>().getClass().getTypeParameters()));
        System.out.println(class1 == class2);
        System.out.println(class1.equals(class2));
        System.out.println(Arrays.toString(new Erasure<>().getClass().getTypeParameters()));
    }
    //output:
    //[E]
    //true
    //true

    //generic array  1
    @Test
    public void test5() {
        Holder<String>[] holder = (Holder<String>[]) new Object[10];
        //Holder<String>[] holder2= new Holder<String>[10];compiler error

        holder[1] = new Holder<String>("aa");
        holder[2] = (Holder<String>) new Object();
        //holder[3]=new Holder<Integer>(10);compiler timer error
        // please cast Holder<Integer> to Holder<String>
        //在编译器进行类型检查
    }

    //generic array  2
    @Test
    public void test6() {
        Holder<String> holder = new Holder<String>("aaa");
        //String[] s=holder.getArray(3);runtime error L Object cannot cas to L String
        Object[] s=holder.getArray(3);
        System.out.println(s);

    }

    //通配符
    @Test
    public <T> void test7() {
        List<? extends Fruit> list = new ArrayList<Apple>();
        //List <Fruit> l1=new ArrayList<Apple>();
        //compiler error
        //list.add(new Apple());
        //list.add(new Fruit());
        System.out.println(list instanceof ArrayList);


        //java.lang.ArrayStoreException
      /*  Fruit[] fruits=new Apple[10];
        fruits[0]=new Orange();*/
    }

    //边界
    @Test
    public void test8() {
        List<? extends Fruit> fruits1 = new ArrayList<Apple>();

       /* fruits1.add(new Object());
          fruits1.add(new Fruit());
          fruits1.add(new Apple());
          add(capture<? extends generic.Fruit>)in List 
          cannot be applied to generic.Apple
        */
        //fruits1.add(new Fruit());
        fruits1 = Arrays.asList(new Apple(), new Fruit());
        Fruit f = fruits1.get(0);
        Apple apple = (Apple) fruits1.get(0);


        List<? super Apple> fruits2 = new ArrayList<Fruit>();//ok
        //List<? super Fruit> fruits3=new ArrayList<Apple>();//compiler error
        fruits2.add(new Apple());
        //fruits2.add(new Fruit());
        fruits2.add(new RedApple());
        Apple a = (Apple) fruits2.get(0);
        Fruit ff = (Fruit) fruits2.get(1);
        Object o = fruits2.get(1);

        List<? super Apple> fruits3 = new ArrayList<Apple>();//ok
    }

    //无界通配符 <?>
    @Test
    public void test9() {
        List<?> list = new ArrayList<Apple>();
        //compiler error
        //list.add(new Object());
        //list.add(new Apple());
        Apple aa = (Apple) list.get(0);
        Object o = list.get(0);

        List list2 = new ArrayList();
        list2.add(new Apple());
        list2.add(new Object());
        Apple a = (Apple) list2.get(0);

    }

    //无界通配符 <?>
    @Test
    public void test10() {
        Holder<?> holder = new Holder<Fruit>(new Apple());
        //holder.setA(new Apple());
       // List<Integer> l=new ArrayList<String>();
        int[] i=new int[5];
        g(i);
    }



    public static <T> void g(T T){
        System.out.println("method g");
    }
}
