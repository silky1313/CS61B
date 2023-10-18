package deque;


import java.util.Iterator;

/**
 * 采用的是循环方式实现的链表
 * 含有removeLast所以必须双指针
 */
public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class Node {
        T value;
        Node prev;
        Node next;

        public Node(T value) {
            this.value = value;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null);  // 创建哨兵节点
        sentinel.next = sentinel;  // 哨兵节点的next指向自身
        sentinel.prev = sentinel;  // 哨兵节点的prev指向自身
    }

    @Override
    public void addFirst(T value) {
        Node addNode = new Node(value);

        addNode.prev = sentinel;
        addNode.next = sentinel.next;
        sentinel.next.prev = addNode;
        sentinel.next = addNode;
        size++;
    }

    @Override
    public void addLast(T value) {
        Node addNode = new Node(value);
        Node lastNode = sentinel.prev;
        lastNode.next = addNode;

        addNode.prev = lastNode;
        addNode.next = sentinel;

        sentinel.prev = addNode;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (isEmpty()) {
            return;
        }
        Node temporaryNode = sentinel.next;
        while (temporaryNode.next != sentinel) {
            System.out.print(temporaryNode.value + " -> ");
            temporaryNode = temporaryNode.next;
        }
        System.out.println(temporaryNode.value);
    }

    private T printDeque(int index) {
        if (index >= size) {
            return null;
        }
        Node temporaryNode = sentinel.next;
        int pos = 0;
        while (temporaryNode.next != sentinel) {
            if (index == pos) {
                return temporaryNode.value;
            }
            temporaryNode = temporaryNode.next;
            pos++;
        }
        return temporaryNode.value;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node firstNode = sentinel.next;
        Node secondNode = firstNode.next;

        secondNode.prev = sentinel;
        sentinel.next = secondNode;

        /**
         * 设置为空垃圾回收
         */
        firstNode.next = firstNode.prev = null;

        size--;
        return firstNode.value;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node lastNode = sentinel.prev;
        Node secondLastNode = lastNode.prev;

        secondLastNode.next = sentinel;
        sentinel.prev = secondLastNode;

        lastNode.prev = lastNode.next = null;

        size--;
        return lastNode.value;
    }

    @Override
    public T get(int index) {
        return printDeque(index);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<?> lld = (Deque<?>) o;
        if (lld.size() != size) {
            return false;
        }
        /**注意应该使用equals方法而不是!=*/
        for (int i = 0; i < size; i++) {
            if (!(lld.get(i).equals(this.get(i)))) {
                return false;
            }
        }
        return true;
    }


    public Iterator<T> iterator() {
        return new Iterable();
    }

    private class Iterable implements Iterator<T> {
        private Node wizPos;


        public Iterable() {
            wizPos = sentinel;
        }

        @Override
        public boolean hasNext() {
            return wizPos.next != sentinel;
        }

        @Override
        public T next() {
            T result = wizPos.next.value;
            wizPos = wizPos.next;
            return result;
        }
    }

    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        return find(sentinel, index);
    }

    private T find(Node head, int index) {
        if (index == -1) {
            return head.value;
        }
        return find(head.next, index - 1);
    }
}
