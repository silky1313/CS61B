package deque;

import java.util.Iterator;

public class Iterable<T> implements Iterator<T> {
    private int wizPos;
    private Deque<T> deque;

    public Iterable(Deque<T> deque) {
        this.deque = deque;
        wizPos = 0;
    }

    @Override
    public boolean hasNext() {
        return wizPos < deque.size();
    }

    @Override
    public T next() {
        T result = deque.get(wizPos);
        wizPos++;
        return result;
    }
}
