package bstmap;

import edu.princeton.cs.algs4.BST;

import java.util.*;

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
        if(list == null) {
            return null;
        }
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
            size++;
        }
    }

    private BSTNode insert(BSTNode now, K key, V value) {
        if (now == null) {
            size++;
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
        Set<K> keySet = new LinkedHashSet<>();
        Deque<BSTNode> deque = new ArrayDeque<>();
        BSTNode p = list;
        // middle loop left- me - right
        while (p != null || !deque.isEmpty()) {
            if (p != null) {
                deque.addFirst(p);
                p = p.left;
            } else {
                p = deque.pollLast();
                keySet.add(p.key);
                p = p.right;
            }
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        BSTNode now = list.get(key);
        if (now == null) {
            return null;
        }
        return remove(key, now.val);
    }

    @Override
    public V remove(K key, V value) {
        BSTNode now = list.get(key);
        if (now == null || now.val != value) {
            return null;
        }

        V result = now.val;
        list = delete(list, key);
        size--;
        return result;
    }

    private BSTNode delete(BSTNode root, K key) {
        if (root == null) {
            return null;
        }
        if(root.key == key) {
            if(root.left == null) {
                return root.right;
            } else if(root.right == null) {
                return root.left;
            } else {
                // 左子树放到后驱结点
                BSTNode min = root.right;
                while(min.left != null) {
                    min = min.left;
                }
                min.left = root.left;
                root = root.right;
                return root;
            }
        }
        if(root.key.compareTo(key) > 0) {
            root.left = delete(root.left, key);
        } else if(root.key.compareTo(key) < 0 ){
            root.right = delete(root.right, key);
        }
        return root;
    }

    public Iterator<K> iterator() {
        return keySet().iterator();
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
