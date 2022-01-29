package Structures;

public interface Entry<K,V> {
    
    /**
     * Gets the value of the entry
     * @return the value
     */
    V getValue();

    /**
     * Gets the key of the entry
     * @return the key
     */
    K getKey();

    /**
     * Sets the value of the entry
     * @param value new value
     * @return old value
     */
    V setValue(V value);
}
