package deque;

import com.sun.tools.hat.internal.util.Comparer;
import jh61b.junit.In;
import net.sf.saxon.expr.Component;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {

    @Test
    public void testMax(){
        MaxArrayDeque<Integer> test = new MaxArrayDeque<>(new IntegerMaxComparator());

        Random random = new Random();

        Integer add = random.nextInt();
        Integer max = add;
        for(int i = 0; i < 10000; i++){
            test.addFirst(add);
            if(add > max){
                max = add;
            }
            add = random.nextInt();
        }
        assertEquals(max, test.max());
    }

    @Test
    public void testMin(){
        MaxArrayDeque<Integer> test = new MaxArrayDeque<>();

        Random random = new Random();

        Integer add = random.nextInt();
        Integer min = add;
        for(int i = 0; i < 10000; i++){
            test.addFirst(add);
            if(add < min){
                min = add;
            }
            add = random.nextInt();
        }
        assertEquals(min, test.max(new IntegerMinComparator()));
    }
}


/**
 * 从大到小排序
 */
class IntegerMaxComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer o1, Integer o2) {
        if(o1 > o2){
            return 1;
        }
        else if(o1 == o2){
            return 0;
        } else{
            return -1;
        }
    }
}

/**
 * 从小到大排序
 */
class IntegerMinComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer o1, Integer o2) {
        if(o1 > o2){
            return -1;
        }
        else if(o1 == o2){
            return 0;
        } else{
            return 1;
        }
    }
}
