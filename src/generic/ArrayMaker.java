package generic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayMaker<T> {
    private Class<T> kind;
    public ArrayMaker(Class<T> kind) {
        this.kind = kind;
        System.out.println(kind.getName());
    }

    T[] create(int size) {
        return (T[]) Array.newInstance(String.class, size);
    }

    public static void main(String[] args) {
        ArrayMaker<String> maker = new ArrayMaker<String>(String.class);
        String[] array = maker.create(5);
        System.out.println(Arrays.toString(array));
    }
}
