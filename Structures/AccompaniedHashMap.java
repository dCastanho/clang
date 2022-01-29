package Structures;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Currently not optimized, we could make all methods constant by inserting the list nodes in as the map values
 * We chose not to do this because the only non-constant method is the put, in the case a value is replaced,
 * which in our case never happens. 
 */
public class AccompaniedHashMap<K,V> implements InsertionMap<K,V> {

    private static final int DEFAULT_CAPACITY = 50;
    
    private HashMap<K, V> map;
    private List<Entry<K,V>> inserted;

    public AccompaniedHashMap(int capacity) {
        map = new HashMap<>(capacity);
        inserted = new LinkedList<>();
    }

    public AccompaniedHashMap() {
        this(DEFAULT_CAPACITY);
    }

    @Override
    public V put(K key, V value) {
        V old = map.put(key, value);
        if( old == null)
            inserted.add( new BaseEntry<K,V>(key, value) );
        else 
            replaceValue(key, value);
        return old;
    }

    private void replaceValue(K key, V value){
        for( Entry<K,V> e : inserted)
            if(e.getKey().equals(key))
                e.setValue(value);
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return inserted.iterator();
    }

    @Override
    public int size() {
        return inserted.size();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
