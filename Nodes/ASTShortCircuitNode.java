package Nodes;

import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;

/**
 * Interface to represent Short Circuit Nodes. These
 * nodes can be used for more efficent compilation of 
 * if/while conditions by directly jumping to labes.
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public interface ASTShortCircuitNode extends ASTNode{

    /**
     * Method that compiles a node for short circuiting.
     * @param c code block for the instructions
     * @param e coordinate environment
     * @param trueL the label to jump to if true
     * @param falseL the label to jump to if false
     */
    void compileSC(CodeBlock c, Environment<Coordinates> e, String trueL, String falseL);
	
}

