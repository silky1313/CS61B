package bstmap;

public class Main {


    public static void main(String[] args) {
        BSTMap<String, String> tmp = new BSTMap<>();
        tmp.put("a", "a");
        tmp.put("b", "b");
        tmp.put("c", "c");
        tmp.put("d", "d");

        for(String i: tmp.keySet()) {
            System.out.println(i);
        }

    }
}
