package bstmap;

import edu.princeton.cs.algs4.BST;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private int size;
    private BSTNode list = new BSTNode();

    public void printInOrder() {
        String result = "[" + middleLoop(list) + "]";
        System.out.println(result);
    }

    private String middleLoop(BSTNode now) {
        if (now == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();

        if (now.left != null) {
            String left = middleLoop(now.left);
            result.append(left).append(", ");
        }

        if (now.right != null) {
            result.append(now.val).append(", ");
            String right = middleLoop(now.right);
            result.append(right);
        } else {
            result.append(now.val);
        }
        return result.toString();
    }

    @Override
    public void clear() {
        size = 0;
        list.left = null;
        list.right = null;
        list.key = null;
    }

    @Override
    public boolean containsKey(K key) {
        BSTNode result = list.get(key);
        return result != null;
    }

    @Override
    public V get(K key) {
        BSTNode result = list.get(key);
        return result == null ? null : result.val;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (list.key != null) {
            BSTNode lookup = list.get(key);
            if (lookup == null) {
                list = insert(list, key, value);
            } else {
                lookup.val = value;
            }
        } else {
            list = new BSTNode(key, value, null, null);
        }
        size++;
    }

    private BSTNode insert(BSTNode now, K key, V value) {
        if (now == null) {
            return new BSTNode(key, value, null, null);
        } else if (now.key.compareTo(key) > 0) {
            now.left = insert(now.left, key, value);
        } else if (now.key.compareTo(key) < 0) {
            now.right = insert(now.right, key, value);
        }
        return now;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    private class BSTNode {
        K key;
        V val;
        BSTNode left;
        BSTNode right;
        BSTNode(K k, V v, BSTNode left, BSTNode right) {
            key = k;
            val = v;
            this.left = left;
            this.right = right;
        }

        BSTNode() {
            key = null;
            val = null;
            left = null;
            right = null;
        }

        BSTNode get(K k) {
            if (key == null) {
                return null;
            }
            if (k.equals(key)) {
               return this;
            } else if (key.compareTo(k) < 0 && right != null) {
                return right.get(k);
            } else if (key.compareTo(k) > 0 && left != null) {
                return left.get(k);
            }
            return null;
        }
    }
}
