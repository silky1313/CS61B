package hashmap;

import org.w3c.dom.Node;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private Integer initialSize = 16;
    private Double maxLoad = 0.75;
    private Collection<Node>[] buckets;
    private Integer size = 0;

    /**
     * hashtable capacity, also the model
     */
    @Override
    public void clear() {
        size = 0;
        buckets = createTable(initialSize);
    }

    @Override
    public boolean containsKey(K key) {
        Integer hashcode = getHashCode(key);
        if (buckets[hashcode] == null) {
            return false;
        }
        return buckets[hashcode].stream().anyMatch(s -> s.key.equals(key));
    }

    @Override
    public V get(K key) {
        if (containsKey(key)) {
            Integer hashcode = getHashCode(key);
            List<Node> filteredNodes = buckets[hashcode].stream()
                    .filter(s -> s.key.equals(key))
                    .collect(Collectors.toList());
            return filteredNodes.get(0).value;
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        Integer hashcode = getHashCode(key);
        if (containsKey(key)) {
            buckets[hashcode].stream().filter(i -> i.key.equals(key)).forEach(i -> i.value = value);
        } else {
            if (buckets[hashcode] == null) {
                buckets[hashcode] = createBucket();
            }
            buckets[hashcode].add(createNode(key, value));
            size++;
            judgeLoad();
        }
    }

    private void judgeLoad() {
        double nowLoad = (double) size / (double) initialSize;
        if (nowLoad > maxLoad) {
            Collection<Node>[] tmp = createTable(initialSize * 2);
            Collection<Node>[] tmpBuckets = buckets;
            buckets = tmp;
            Integer tmpSize = initialSize;
            initialSize *= 2;
            size = 0;
            for (int i = 0; i < tmpSize; i++) {
                if (tmpBuckets[i] == null) {
                    continue;
                }
                tmpBuckets[i].forEach(s -> this.put(s.key, s.value));
            }
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (int i = 0; i < initialSize; i++) {
            if (buckets[i] == null) {
                continue;
            }
            buckets[i].forEach(s -> keySet.add(s.key));
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        Integer hashcode = getHashCode(key);
        Collection<Node> bucket = buckets[hashcode];

        Node removedNode = null;

        Iterator<Node> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.key.equals(key)) {
                iterator.remove();
                removedNode = node;
                break;
            }
        }

        return removedNode == null ? null : removedNode.value;
    }

    @Override
    public V remove(K key, V value) {
        Integer hashcode = getHashCode(key);
        Collection<Node> bucket = buckets[hashcode];

        Node removedNode = null;

        Iterator<Node> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.key.equals(key) && node.value.equals(value)) {
                iterator.remove();
                removedNode = node;
                break;
            }
        }

        return removedNode == null ? null : removedNode.value;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }


    /**
     * Constructors
     */
    public MyHashMap() {
        buckets = createTable(initialSize);
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        buckets = createTable(initialSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.maxLoad = maxLoad;
        buckets = createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return (Collection<Node>[]) new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    private Integer getHashCode(K key) {
        return (key.hashCode() % initialSize + initialSize) % initialSize;
    }

}
