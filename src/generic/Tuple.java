package generic;
//2018/10/25  20:19

/*
Tuple(元组)；可以保存多个不同类型的对象，可以将其作为方法的返回值
 */
public class Tuple<A, B, C> {
    final A a;
    B b;
    C c;

    public Tuple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public Tuple getObjects(){
        return this;
    }
}
