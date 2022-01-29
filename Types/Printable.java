package Types;

import Util.CodeBlock;

/**
 * Represents if a type is printable
 */
public interface Printable {
    
    /**
     * Adds the correct instructions to print this type to the code block for compilation
     * @param c the code block to add the instructions to
     */
    public void print(CodeBlock c);

    /**
     * Adds the correct instructions to println this type to the code block for compilation
     * @param c the code block to add the instructions to
     */
    public void printLn(CodeBlock c);
}
