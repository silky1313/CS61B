package deque;

import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {

    @Test
    public void testaddFirst(){
        ArrayDeque<Integer> test = new ArrayDeque<>();
        for(int i = 0; i < 20; i++){
            test.addFirst(i);
        }
        System.out.println(test);
    }

    @Test
    public void testaddLast(){
        ArrayDeque<Integer> test = new ArrayDeque<>();
        for(int i = 0; i < 20; i++){
            test.addLast(i);
        }
        System.out.println(test);
    }

    @Test
    public void testRandom(){
        ArrayDeque<Integer> test = new ArrayDeque<>();
        java.util.ArrayDeque<Integer> answer = new java.util.ArrayDeque<>();
        for(int i = 0; i < 10; i++){
            Random r = new Random();
            int choose = r.nextInt(2);
            int value = r.nextInt();
            if(choose == 0){
                test.addFirst(value);
                answer.addFirst(value);
            }
            if(choose == 1){
                test.addLast(value);
                answer.addLast(value);
            }
        }
        assertEquals(test.toString(), answer.toString());
    }

    @Test
    public void testremove(){
        ArrayDeque<Integer> test = new ArrayDeque<>();
        for(int i = 0; i < 5; i++){
            test.addFirst(i);
        }
        System.out.println(test);
        for(int i = 0; i < 5; i++){
           int t = test.removeLast();
           System.out.println(t);
        }
    }
}
