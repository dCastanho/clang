package Nodes;

import Types.IType;
import Types.TypeBool;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.BoolValue;
import Values.IValue;
import Exceptions.InterpreterError;
import Exceptions.TypeCheckerError;

/**
 * Class representing the ASTNode that
 * negates a boolean value (true -> false, false -> true)
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTNeg implements ASTShortCircuitNode{


    private static final String MISMATCH_TYPE = "Mismatch type on operator !";

    private ASTNode hs;

    public IValue eval(Environment<IValue> e) {
        IValue  val = hs.eval(e);
        if(val instanceof BoolValue)
            return ((BoolValue) val).getValue() ? BoolValue.FALSE : BoolValue.TRUE;
        else 
            throw new InterpreterError(MISMATCH_TYPE);
    }

    public ASTNeg(ASTNode n) {
        hs = n;
    }

    public void compile(CodeBlock c, Environment<Coordinates> e) {
        this.hs.compile(c, e);
        //Convert 1 to 0 and 0 to 1. -(1-1) = 0 && -(0-1) = 1
        c.emit( String.format( JVMCodes.SIPUSH, 1) );
        c.emit( JVMCodes.SUB );
        c.emit( JVMCodes.NEG );
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType t1 = hs.typecheck(e);
        if(t1 instanceof TypeBool)
            return t1;
        
        throw new TypeCheckerError( MISMATCH_TYPE );
    }

    @Override
    public void compileSC(CodeBlock c, Environment<Coordinates> e, String trueL, String falseL) {
        ASTShortCircuitNode cSC = (ASTShortCircuitNode) this.hs;
        cSC.compileSC(c, e, falseL, trueL);        
    }

    
}
