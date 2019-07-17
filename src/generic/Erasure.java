package generic;
//2018/10/25  22:50

public class Erasure<T extends Holder> {
    T t;
    public void test() {
        t.setA("");
        //Erasure<Holder>[] e=new Erasure<Holder>[10];
    }
}
