package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        //存储数组长度
        AList<Integer> Ns = new AList<>();
        //存储处理N次addLast操作耗费的总时间，单位是s
        AList<Double> times = new AList<>();
        //操作的总次数
        AList<Integer> opCounts = new AList<>();


        Integer init = 1000;
        for(int i = 0; i < 8; i++){
            Ns.addLast(init);
            opCounts.addLast(10000);
            init *= 2;
        }
        for(int i = 0; i < Ns.size(); i++){
            int length = Ns.get(i);
            int opNumber = opCounts.get(i);
            SLList<Integer> tmp = new SLList<>();
            for(int j = 0; j < length; j++){
                tmp.addLast(i);
            }
            //TODO:计时操作
            Stopwatch sw = new Stopwatch();
            for(int j = 0; j < opNumber; j++){
                tmp.getLast();
            }
            times.addLast(sw.elapsedTime());
        }
        printTimingTable(Ns, times, opCounts);
    }

}
