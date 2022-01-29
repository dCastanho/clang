package Nodes;

import Types.IType;
import Types.Printable;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.IValue;
import Exceptions.TypeCheckerError;

/**
 * Class that represents the print
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTPrint implements ASTNode{

    /**
     *
     */
    private static final String CANNOT_PRINT = "Cannot print values of this type";
    private ASTNode child;
    private Printable type;
    private boolean endLine;

    public ASTPrint(ASTNode c, boolean ln) {
        child = c;
        endLine = ln;
        type = null;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue v = child.eval(e);
        if(endLine)
            System.out.println(v.show());
        else
            System.out.print(v.show());

        return v;
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        child.compile(c, e);
        c.emit( JVMCodes.DUP );
        if(endLine)
            type.printLn(c);
        else 
            type.print(c);   
    }

    @Override
    public IType typecheck(Environment<IType> e) {
            IType t = child.typecheck(e);
            if(t instanceof Printable){
                type = (Printable)t;
                return t;
            }

            throw new TypeCheckerError( CANNOT_PRINT );
    }
    
    
}
