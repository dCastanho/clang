package Types;

import Util.CodeBlock;

public interface IComparable {
    
    /**
     * Compilation instructions for comparison. Assumes two elements of this type are
     * on top of the stack and will replace them with an integer. This integer is:
     * > 0 if the first value is greater
     * < 0 if the second value is greater
     * = 0 if they are equal
     * @param c Codeblock to put the instructions in
     */
    public void comparison(CodeBlock c);

}
