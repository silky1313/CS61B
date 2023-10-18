package deque;

import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ArrayDequeTest {

    @Test
    public void testaddFirst(){
        ArrayDeque<Integer> test = new ArrayDeque<>();
        java.util.ArrayDeque<Integer> answer = new java.util.ArrayDeque<>();
        for(int i = 0; i < 20; i++){
            test.addFirst(i);
            answer.addFirst(i);
        }
        for(int i = 0; i < 20; i++){
            test.removeFirst();
            answer.removeLast();
        }
        System.out.println(test);
        System.out.println(answer);
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
        Random r = new Random();
        int loop = r.nextInt(10000);
        for(int i = 0; i < loop; i++){
            int choose = r.nextInt(4);
            int value = r.nextInt();
            if(choose == 0) {
                test.addFirst(value);
                answer.addFirst(value);
            } else if(choose == 1){
                test.addLast(value);
                answer.addLast(value);
            } else if(choose == 2 && answer.size() > 0){
                test.removeFirst();
                answer.removeFirst();
            } else if(choose == 3 && answer.size() > 0){
                test.removeLast();
                answer.removeLast();
            }
        }
        assertEquals(test.toString(), answer.toString());
    }
}
