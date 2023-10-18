package deque;

import org.junit.Test;
import java.util.Random;

import static org.junit.Assert.assertFalse;

public class ArrayDequeTest {

  @Test
  public void testequals() {
    LinkedListDeque<Integer> test1 = new LinkedListDeque<>();
    LinkedListDeque<Integer> test2 = new LinkedListDeque<>();
    ArrayDeque<Integer> test3 = new ArrayDeque<>();
    for (int i = 0; i < 20; i++) {
      test1.addFirst(i);
      test2.addFirst(i);
      test3.addFirst(i);
    }

    System.out.println(test3.equals(test1));
    System.out.println(test3.equals(test2));
  }

  @Test
  public void testiterator() {
    LinkedListDeque<Integer> test1 = new LinkedListDeque<>();
    ArrayDeque<Integer> test3 = new ArrayDeque<>();
//        for (int i = 0; i < 5; i++) {
//            test1.addFirst(i);
//            test3.addFirst(i);
//        }

    for (Integer integer : test1) {
      System.out.print(integer + " ");
    }
    System.out.println();
    for (Integer integer : test3) {
      System.out.print(integer + " ");
    }
  }


  @Test
  public void testaddLast() {
    ArrayDeque<Integer> test = new ArrayDeque<>();
    for (int i = 0; i < 20; i++) {
      test.addLast(i);
    }
    System.out.println(test);
  }

  @Test
  public void testRandom() {
    ArrayDeque<Integer> test = new ArrayDeque<>();
    java.util.ArrayDeque<Integer> answer = new java.util.ArrayDeque<>();
    Random r = new Random();
    int loop = r.nextInt(10000);
    for (int i = 0; i < loop; i++) {
      int choose = r.nextInt(4);
      int value = r.nextInt();
      if (choose == 0) {
        test.addFirst(value);
        answer.addFirst(value);
      } else if (choose == 1) {
        test.addLast(value);
        answer.addLast(value);
      } else if (choose == 2 && answer.size() > 0) {
        test.removeFirst();
        answer.removeFirst();
      } else if (choose == 3 && answer.size() > 0) {
        test.removeLast();
        answer.removeLast();
      }
    }
  }
}
