package Structures;

import java.util.Iterator;

/**
 * Abstract Data Type that associates Values to Keys but iterates over them by insertion order. 
 * This relates to the order of their first insertion; if a value is later replaced,
 * it will still iterate in the order in which it was first inserted.
 * 
 * Currently only has the necessary methods for this particular application.
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public interface InsertionMap<K,V> extends Iterable<Entry<K,V>>{
 
    /**
     * Places a binding (key, value) on the insertion map.
     * If that binding already exists, it is replaced.
     * @param key key of the pair
     * @param value value of the pair
     * @return old value if it existed, null if it didn't
     */
    V put(K key, V value);
    
    /**
     * Gets the value associated with a given key
     * @param key the key to search for
     * @return the value, or null if the binding doesn't exist
     */
    V get(K key);

    /**
     * Iterates over the elements in insertion order
     * @return an iterator
     */
    Iterator<Entry<K,V>> iterator();

    /**
     * @return the number of elements in the map
     */
    int size();
}
