package Nodes;


import Types.IType;
import Types.TypeRef;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.CellValue;
import Values.IValue;
import Exceptions.InterpreterError;
import Exceptions.TypeCheckerError;

/**
 * Class representing the ASTNode for derefencing references.
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTDeref implements ASTShortCircuitNode{

    /**
     *
     */
    private static final String NON_CELL = "Impossible to dereference a non-cell value";
    private ASTNode child;
    private TypeRef type;

    public ASTDeref(ASTNode c) {
        child = c;
        type = null;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue v = child.eval(e);
        if(v instanceof CellValue){
            return ((CellValue)v).get();
        }
        
        throw new InterpreterError(NON_CELL);
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        this.child.compile(c, e);
        String instruction = String.format(JVMCodes.GETFIELD, type.getJVMName(), TypeRef.FIELD_NAME, type.getReference().getJVMType());        
        c.emit( instruction );
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType cell = child.typecheck(e);
        if(cell instanceof TypeRef){
            type = ((TypeRef)cell);
            return type.getReference();
        }
        
        throw new TypeCheckerError( NON_CELL );
    }

    @Override
    public void compileSC(CodeBlock c, Environment<Coordinates> e, String trueL, String falseL) {
        compile(c, e); //? Could I assume it extends SC compilation?
        c.emit( String.format( JVMCodes.IFEQ, falseL ) );
        c.emit( String.format( JVMCodes.GOTO, trueL) );
    }
    
}
