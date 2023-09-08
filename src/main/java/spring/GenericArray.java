package spring;

import java.util.ArrayList;

public class GenericArray<T> {
    private T[] array;
    public GenericArray(int sz) { this.array = (T[]) new Object[sz]; }
    public void put(int index, T item) { array[index] = item; }
    public T get(int index) { return array[index]; }
    public T[] rep() { return array; }
    public static void main(String[] args) {
        GenericArray<Integer> genericArray = new GenericArray<Integer>(10);
        Object[] oa = genericArray.rep();
    }
}
