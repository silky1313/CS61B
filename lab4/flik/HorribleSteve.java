package flik;

public class HorribleSteve {
    public static void main(String [] args) throws Exception {
        int i = 0;
        /**采用断电测试后发现在i==j=128的时候if里面的条件是false*/
        for (int j = 0; i < 500; ++i, ++j) {
            if (!Flik.isSameNumber(i, j)) {
                throw new Exception(
                        String.format("i:%d not same as j:%d ??", i, j));
            }
        }
        System.out.println("i is " + i);
    }
}
