package deque;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 采用的是循环方式实现的链表
 * 含有removeLast所以必须双指针
 */
public class LinkedListDeque<T>  implements Deque<T>, Iterator<T> {
    private class Node{
        T value;
        Node prev;
        Node next;
        public Node(T value){
            this.value = value;
        }
    }
    private Node sentinel;
    int size;

    public LinkedListDeque() {
        sentinel = new Node(null);  // 创建哨兵节点
        sentinel.next = sentinel;  // 哨兵节点的next指向自身
        sentinel.prev = sentinel;  // 哨兵节点的prev指向自身
    }

    @Override
    public void addFirst(T value){
        Node addNode = new Node(value);

        addNode.prev = sentinel;
        addNode.next = sentinel.next;
        sentinel.next.prev = addNode;
        sentinel.next = addNode;
        size++;
    }

    @Override
    public void addLast(T value){
        Node addNode = new Node(value);
        Node lastNode = sentinel.prev;
        lastNode.next = addNode;

        addNode.prev = lastNode;
        addNode.next = sentinel;

        sentinel.prev = addNode;
        size++;
    }

    @Override
    public int size(){
        return size;
    }

    @Override
    public void printDeque(){
        if(isEmpty()){
            return;
        }
        Node temporaryNode = sentinel.next;
        while(temporaryNode.next != sentinel){
            System.out.print(temporaryNode.value + " -> ");
            temporaryNode = temporaryNode.next;
        }
        System.out.println(temporaryNode.value);
    }

    public T printDeque(int index){
        if(index >= size){
            return null;
        }
        Node temporaryNode = sentinel.next;
        int pos = 0;
        while(temporaryNode.next != sentinel) {
            if (index == pos) {
                return temporaryNode.value;
            }
            temporaryNode = temporaryNode.next;
            pos++;
        }
        return temporaryNode.value;
    }

    @Override
    public T removeFirst(){
        if(size == 0) return null;
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
    public T removeLast(){
        if(size == 0) return null;
        Node lastNode = sentinel.prev;
        Node secondLastNode = lastNode.prev;

        secondLastNode.next = sentinel;
        sentinel.prev = secondLastNode;

        lastNode.prev = lastNode.next = null;

        size--;
        return lastNode.value;
    }

    @Override
    public T get(int index){
      return printDeque(index);
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Deque){
            Deque deque = (Deque) o;
            int length = this.size();
            if(deque.size() != length) return false;
            for(int i = 0; i < length; i++){
                if(deque.get(i) != this.get(i)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private int currentPosition;
    @Override
    public boolean hasNext() {
        return currentPosition < size;
    }

    @Override
    public T next() {
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        T element = this.get(currentPosition);
        currentPosition++;
        return element;
    }

    public Iterator<T> iterator() {
        currentPosition = 0; // 初始化迭代位置
        return this;
    }

    public T getRecursive(int index){
        if(index >= size || index < 0) return null;
        return find(sentinel, index);
    }

    public T find(Node head, int index){
        if(index == -1) return head.value;
        return find(head.next, index - 1);
    }
}
