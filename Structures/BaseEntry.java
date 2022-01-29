package Structures;

public class BaseEntry<K,V> implements Entry<K,V> {

    private K key;
    private V value;

    public BaseEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}
