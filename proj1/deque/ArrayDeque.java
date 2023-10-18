package deque;


import java.util.Arrays;
import java.util.Iterator;

/**
 * 循环数组
 * 头指针指向队列头
 * 尾指针指向队列尾
 * 动态拓展数组大小，使用率小于二十五时，数组大小减半
 */
public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] array;
    private int size;
    private int lengthArray;
    private int head;
    private int tail;

    public ArrayDeque() {
        array = (T[]) new Object[16];
        lengthArray = 16;
        size = 0;
        head = 0;
        tail = 1;
    }

    /**
     * expand实现size * 2
     * 同时将原数组复制到新数组上
     */
    private void changeLengthArray(int newLengthArray) {
        T[] oldArray = Arrays.copyOf(array, array.length);
        array = (T[]) new Object[newLengthArray];
        int oldArrayIndex = head;
        for (int i = 1; i <= size; i++) {
            oldArrayIndex = (oldArrayIndex + 1) % lengthArray;
            array[i] = oldArray[oldArrayIndex];
        }
        lengthArray = newLengthArray;
        head = 0;
        tail = size + 1;
    }

    @Override
    public void printDeque() {
        System.out.println(this.toString());
    }

    @Override
    public void addFirst(T value) {
        if (size == lengthArray) {
            changeLengthArray(lengthArray * 2);
        }
        array[head] = value;
        head = (head - 1 + lengthArray) % lengthArray;
        size++;
    }

    @Override
    public void addLast(T value) {
        if (size == lengthArray) {
            changeLengthArray(lengthArray * 2);
        }
        array[tail] = value;
        tail = (tail + 1) % lengthArray;
        size++;
    }

    @Override
    public T removeFirst() {
        if (size <= 0) {
            return null;
        }
        head = (head + 1) % lengthArray;
        size--;
        T result = array[head];
        if (lengthArray > 16 && size < lengthArray / 4) {
            changeLengthArray(lengthArray / 2);
        }
        return result;
    }

    @Override
    public T removeLast() {
        if (size <= 0) return null;
        tail = (tail - 1 + lengthArray) % lengthArray;
        size--;
        T result = array[tail];
        if (lengthArray > 16 && size < lengthArray / 4) {
            changeLengthArray(lengthArray / 2);
        }
        return result;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        if (index >= size) return null;
        int pos = (index + 1 + head) % lengthArray;
        return array[pos];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        ArrayDeque<?> ad = (ArrayDeque<?>) o;
        if (ad.size() != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (ad.get(i) != get(i)) {
                return false;
            }
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new Iterable();
    }

    private class Iterable implements Iterator<T> {
        private int wizPos;


        public Iterable() {
            wizPos = 0;
        }

        @Override
        public boolean hasNext() {
            return wizPos < size;
        }

        @Override
        public T next() {
            int pos = (wizPos + 1 + head) % lengthArray;
            T result = array[pos];
            wizPos++;
            return result;
        }
    }
}
