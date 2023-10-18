package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
  private Comparator<T> comparator;

  public MaxArrayDeque(Comparator<T> c) {
    super();
    this.comparator = c;
  }

  public T max() {
    int size = super.size();
    if (size == 0) {
      return null;
    }
    T max = super.get(0);
    for (int i = 1; i < size; i++) {
      T current = super.get(i);
      if (comparator.compare(current, max) > 0) {
        max = current;
      }
    }
    return max;
  }

  public T max(Comparator<T> c) {
    int size = super.size();
    if (size == 0) {
      return null;
    }
    T max = super.get(0);
    for (int i = 1; i < size; i++) {
      T current = super.get(i);
      if (c.compare(current, max) > 0) {
        max = current;
      }
    }
    return max;
  }
}
