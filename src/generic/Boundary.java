package generic;

interface Move<T> {
    T move();
}

interface Color {
    int getOne();
}
class go<T>{}
class Walk<E> extends go<E> implements Color{
    static int a = 0;

    public String say() {
        return "hi";
    }

    @Override
    public int getOne() {
        return 0;
    }
}

class Bound<T extends Walk & Color >{
    T t;
    int a = 0;

    public Bound(T t) {
        this.t = t;
    }
}

public class Boundary {
    public static void main(String[] args) {
        Bound<Walk> b=new Bound<Walk>(new Walk());
        b.a=2;
        b.t.getOne();
    }
}
