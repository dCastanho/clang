package Util;

/**
 * Class to hold all jvm codes as String constants
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class JVMCodes {


    /**
     * Swaps the position of two elements on top of the stack
     */
    public static final String SWAP = "swap";


    /**
     * Arbitrary load of a local variable to the top of the stack
     * <p>
     * <code> %d </code> - number of the variable
     */
    public static final String LOAD = "aload %d"; 

    /**
     * Arbitrary store of the value on top of the stack to a local variable
     * <p>
     * <code> %d </code> - number of the variable
     */
    public static final String STORE = "astore %d"; 


    /**
     * Puts the value on top of the stack into the value in the class
     * <p>
     * <code> %s </code> - name of the class to put the value inside of
     * <p>
     * <code> %s </code> - name of the field to occupy
     * <p>
     * <code> %s </code> - type of the field
     */
    public static final String PUTVALUE = "putfield %s/%s %s";

    /**
     * Jumps to the instruction with the given label
     * <p>
     * <code> %s </code> - label to jump to
     */
    public static final String GOTO = "goto %s";

    /**
     * Pushes an integer onto the stack
     * <p>
     * <code> %d </code> - the integer to put on the stack
     */
    public static final String SIPUSH = "sipush %d";

    /**
     * Gets the given value from the class reference on top of the stack
     * <p>
     * <code> %s </code> - name of the class to get from
     * <p>
     * <code> %s </code> - name of the field to get
     * <p>
     * <code> %s </code> - type of the field
     */
    public static final String GETFIELD = "getfield %s/%s %s";

    /**
     * Adds the two integers on top of the stack and stacks the result
     */
    public static final String ADD = "iadd";

    /**
     * Multiplies the two integers on top of the stack and stacks the result
     */
    public static final String MULT = "imul";

    /**
     * Divides the two integers on top of the stack and stacks the result
     */
    public static final String DIV = "idiv";

    /**
     * Substracts the two integers on top of the stack and stacks the result
     */
    public static final String SUB = "isub";

    /**
     * Pushes 0 onto the stack, represents false
     */
    public static final String PUSH_FALSE = "sipush 0";

    /**
     * Pushes 1 onto the stack, represents true
     */
    public static final String PUSH_TRUE = "sipush 1";

    /**
     * Performs the OR operation over the two integers on the stack
     */
    public static final String OR = "ior";

    /**
     * Performs the AND operation over the two integers on the stack
     */
    public static final String AND = "iand";

    /**
     * Duplicates the value on top of the stack
     */
    public static final String DUP = "dup";

    /**
     * Creates a new instance of the class
     * <p>
     * <code> %s </code> - name of the class
     *
     */
    public static final String NEW = "new %s";

    /**
     * Negates the integer on top of the stack
     * <p>
     */
    public static final String NEG = "ineg";

    /**
     * Pops the topmost value of the stack
     */
    public static final String POP = "pop";

    /**
     * Jumps to label if the value on top of the stack is equal to 0 (v == 0)
     * <p>
     * <code> %s </code> - label to jump to
     */
    public static final String IFEQ = "ifeq %s";

    /**
     * Jumps to label if the value on top of the stack is different than 0 (v != 0)
     * <p>
     * <code> %s </code> - label to jump to
     */
    public static final String IFNE = "ifne %s";

    /**
     * Jumps to label if the value on top of the stack is strictly greater than 0 (v > 0)
     * <p>
     * <code> %s </code> - label to jump to
     */
    public static final String IFGT = "ifgt %s";

    /**
     * Jumps to label if the value on top of the stack is strictly smaller than 0 (v < 0)
     * <p>
     * <code> %s </code> - label to jump to
     */
    public static final String IFLT = "iflt %s";

    /**
     * Jumps to label if the value on top of the stack is greater or equal to 0 (v >= 0)
     * <p>
     * <code> %s </code> - label to jump to
     */
    public static final String IFGE = "ifge %s";

    /**
     * Jumps to label if the value on top of the stack is smaller or equal to 0 (v <= 0)
     * <p>
     * <code> %s </code> - label to jump to
     */
    public static final String IFLE = "ifle %s";

    /**
     * To be placed inside a class definition, creates a field with the given name and type
     * <p>
     * <code> %s </code> - Name of the field
     * <p>
     * <code> %s </code> - Type of the field
     */
    public static final String CREATE_FIELD = ".field public %s %s";

    /**
     * Calls the default constructor of a given class, whose reference is on top of the stack
     * <p>
     * <code>%s</code> - Name of the class
     */
    public static final String CONSTRUCTOR_CALL = "invokespecial %s/<init>()V";

    /**
     * Easy call to store the frame in a local register
     */
    public static final String STORE_FRAME = "astore 3";

    /**
     * Easy call to get the frame from a local register
     */
    public static final String LOAD_FRAME = "aload 3";


    /**
     * Checks whether the object on top of the stack is of the given type
     * <p> 
     * <code>%s</code> - Type to compare to
     */
    public static final String CHECKCAST = "checkcast %s";

    /**
     * Calls the given function in the given interface
     * <p>
     * <code>%s</code> - Interface to call the function in 
     * <p>
     * <code>%s</code> - Function to call (its whole signature) 
     * <p>
     * <code>%d</code> - Number of arguments + return type
     */
    public static final String INVOKEINTERFACE = "invokeinterface %s/%s %d";

    /**
     * Method signature declaration
     * <p>
     * <code>%s</code> - The signature (name and arguments) of the method
     */
    public static final String PUBLIC_METHOD_DECL = ".method public %s";


    /**
     * Limits the amount of local variables
     * <p>
     * <code>%d</code> - Number of variables, minimum of 4
     */
    public static final String LOCAL_VARIABLE_LIMIT = ".limit locals %d";


    /**
     * Limit the size of the stack in the local context
     */
    public static final String LOCAL_STACK_LIMIT = ".limit stack 256";

    /**
     * Get the class, similar to <code>this</code> keyword (?). Assuming constructor stores it there
     */
    public static final String LOAD_SELF = "aload_0";

    /**
     * Load local integer variable/register.
     * <p>
     * <code> %d </code> - Local variable to load
     */
    public static final String ILOAD = "iload %d";

    /**
     * Load local reference variable/register.
     * <p>
     * <code> %d </code> - Local variable to load
     */
    public static final String ALOAD = "aload %d";

    /**
     * Return an integer 
     */
    public static final String IRETURN = "ireturn";

    /**
     * Return a reference
     */
    public static final String ARETURN = "areturn";

    /**
     * Ends a method
     */
    public static final String END_METHOD = ".end method";

    /**
     * Implements keyword
     * <p>
     * <code>%s</code> - The interface to implement
     */
    public static final String IMPLEMENTS = ".implements %s";


    /**
     * Invokes the static method println.
     * Requires a string to be on top of the stack, followed by the
     * static PrintStream reference
     */
    public static final String PRINT_LN = "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V";

    /**
     * Invokes the static method print.
     * Requires a string to be on top of the stack, followed by the
     * static PrintStream reference
     */
    public static final String PRINT = "invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V";

    /**
     * Converts a value into a string, must be of type I or Z
     * <p>
     * <code>%s</code> - type of the value, must be I or Z
     */
    public static final String STRING_VALUE_OF = "invokestatic java/lang/String/valueOf(%s)Ljava/lang/String;";

    /**
     * Gets the static reference for PrintStream and puts it on
     * top of the stack
     */
    public static final String GET_PRINT_STREAM = "getstatic java/lang/System/out Ljava/io/PrintStream;";

    /**
     * Calls the library print function for strings, printing a string on top of the stack
     */
    public static final String PRINT_STRING = "invokestatic Lib/printString(Ljava/lang/String;)V";

    /**
     * Calls the library println function for strings, printing a string on top of the stack and ending with a \n
     */
    public static final String PRINT_STRINGLN = "invokestatic Lib/printStringLn(Ljava/lang/String;)V";

    /**
     * Calls the library print function for integers, printing a int on top of the stack
     */
    public static final String PRINT_INT = "invokestatic Lib/printInt(I)V";

    /**
     * Calls the library println function for integers, printing a int on top of the stack and ending with a \n
     */
    public static final String PRINT_INTLN = "invokestatic Lib/printIntLn(I)V";

    /**
     * Calls the library print function for booleans, printing a bool on top of the stack
     */
    public static final String PRINT_BOOL = "invokestatic Lib/printBoolean(Z)V";

    /**
     * Calls the library println function for booleans, printing a bool on top of the stack and ending with a \n
     */
    public static final String PRINT_BOOLLN = "invokestatic Lib/printBooleanLn(Z)V";


    /**
     * Pushes null onto the stack
     */
    public static final String PUSH_NULL = "aconst_null";

}
