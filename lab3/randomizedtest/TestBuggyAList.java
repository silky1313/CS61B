package randomizedtest;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {

    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> a = new AListNoResizing<>();
        BuggyAList<Integer> b = new BuggyAList<>();
        for(int i = 1; i < 4; i++){
            a.addLast(i);
            b.addLast(i);
        }
        assertEquals(a.size(), b.size());

        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> alist = new AListNoResizing<>();
        BuggyAList<Integer> buggy = new BuggyAList<>();

        int N = 999;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                alist.addLast(randVal);
                buggy.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                int size1 = alist.size();
                int size2 = buggy.size();
                assertEquals(size1, size2);
            } else if(operationNumber == 2 && alist.size() > 0 && buggy.size() > 0){
                int lastValue1 = alist.getLast();
                int lastValue2 = buggy.getLast();
                assertEquals(lastValue1, lastValue2);
            } else if(operationNumber == 3 && alist.size() > 0 && buggy.size() > 0){
                int lastValue1 = alist.removeLast();
                int lastValue2 = buggy.removeLast();
                assertEquals(lastValue1, lastValue2);
            }
        }
    }
}
