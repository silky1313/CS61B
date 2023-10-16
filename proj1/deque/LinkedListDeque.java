package deque;


/**
 * 采用的是循环方式实现的链表
 * 含有removeLast所以必须双指针
 */
public class LinkedListDeque<Item> {
    private class Node{
        Item value;
        Node prev;
        Node next;
        public Node(Item value){
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

    public void addFirst(Item value){
        Node addNode = new Node(value);

        addNode.prev = sentinel;
        addNode.next = sentinel.next;
        sentinel.next.prev = addNode;
        sentinel.next = addNode;
        size++;
    }

    public void addLast(Item value){
        Node addNode = new Node(value);
        Node lastNode = sentinel.prev;
        lastNode.next = addNode;

        addNode.prev = lastNode;
        addNode.next = sentinel;

        sentinel.prev = addNode;
        size++;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

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

    public Item printDeque(int index){
        if(index > size){
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


    public Item removeFirst(){
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

    public Item removeLast(){
        if(size == 0) return null;
        Node lastNode = sentinel.prev;
        Node secondLastNode = lastNode.prev;

        secondLastNode.next = sentinel;
        sentinel.prev = secondLastNode;

        lastNode.prev = lastNode.next = null;

        size--;
        return lastNode.value;
    }

    public Item get(int index){
      return printDeque(index);
    }
}
