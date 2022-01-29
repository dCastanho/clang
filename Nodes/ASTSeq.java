package Nodes;

import Types.IType;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.IValue;

/**
 * Class that represents the sequence of two nodes
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTSeq implements ASTNode{

    private ASTNode left;
    private ASTNode right;

    public ASTSeq(ASTNode l , ASTNode r) {
        left = l;
        right = r;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        left.eval(e);
        return right.eval(e);
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        this.left.compile(c, e);
        c.emit( JVMCodes.POP );
        this.right.compile(c, e);       
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        left.typecheck(e); //to test for exception
        return right.typecheck(e);
    }
    
}
