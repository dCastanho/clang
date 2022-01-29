package Nodes;
import Types.IType;
import Types.TypeInt;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.IValue;
import Values.IntValue;
import Exceptions.InterpreterError;
import Exceptions.TypeCheckerError;

/**
 * Class that represents the ASTNode
 * of integer negation ( 10 -> -10)
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTUneg implements ASTNode{


    private static final String NOT_INTEGER = "Can only apply integer negation to integers";

    private ASTNode hs;

    public IValue eval(Environment<IValue> e) {
        IValue  val = hs.eval(e);
        if(val instanceof IntValue)
            return new IntValue(-((IntValue)val).getValue());
        else 
            throw new InterpreterError( NOT_INTEGER );
    }

    public ASTUneg(ASTNode n) {
        hs = n;
    }

    public void compile(CodeBlock c, Environment<Coordinates> e) {
        hs.compile(c, e);
        c.emit( JVMCodes.NEG );
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType t = hs.typecheck(e);
        if(t instanceof TypeInt)
            return t;
        
        throw new TypeCheckerError(NOT_INTEGER);
    }

}