package generic;
//2018/10/25  19:56

import beans.Cat;

import java.util.List;

public class Holder<T> {
    T a;
    T b;
    T c;


    public Holder(T a) {
        this.a = a;
    }

    public Holder(T a, T b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public T getA() {
        return a;
    }

    public void setA(T a) {
        this.a = a;
    }

    public T getB() {
        return b;
    }

    public void setB(T b) {
        this.b = b;
    }

    public T getC() {
        return c;
    }

    public void setC(T c) {
        this.c = c;
    }

    T[] tArray;

    public T[] getArray(int size) {
        tArray = (T[]) new Object[size];
        return tArray;
    }

}
