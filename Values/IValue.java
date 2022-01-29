package Values;

/**
 * Interface to represent abstrat values
 * @author Daniel Castanho
 * @author Eric Solcan
 */
public interface IValue {
    
    /**
     * Gets a string representation of the value stored
     * @return the string representation of the value stored
     */
    String show();

    /**
     * Tests if a value is of the same type
     * @param v the other value
     * @return true if it is, false if it isn't
     */
    boolean sameType(IValue v);
}
