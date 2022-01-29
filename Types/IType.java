package Types;

/**
 * Represents a type
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public interface IType {

    /**
     * Similar to toString(), a method to help visualize the type
     * @return
     */
    String show();

    /**
     * How the type will be represented in JVM
     * @return the string with the representation
     */
    String getJVMType();

    /**
     * How the JVM will call the type
     * @return the name in a string
     */
    String getJVMName();

    /**
     * The JVM return code for this type
     * @return the String with the JVM code
     */
    String returnCode();

    /**
     * The JVM load code for this type
     * @return the String with the JVM code
     */
    String loadCode();

    /**
     * Compares two types
     * @param t2 type to ompare
     * @return true if they are the same type, false if they aren't
     */
    boolean sameType(IType t2);

}