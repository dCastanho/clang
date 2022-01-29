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
 * Class representing an ASTNode of assignment ( <code> != </code>). 
 * Left side must represent a reference, and the right side must represented 
 * the referenced type.
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTAssign implements ASTNode{

    /**
     *
     */
    private static final String REFED_TYPE_MISMATCH = "Assigned value on operator := does not matched the referenced type";

    private static final String LHS_NOT_REFERENCE = "Lefthand side of := does not represent a reference";

    private ASTNode left;
    private ASTNode right;
    private IType leftType; 
    private IType rightType; 

    public ASTAssign(ASTNode l, ASTNode r) {
        left = l;
        right = r;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue v1 = left.eval(e);
        if(v1 instanceof CellValue){
            IValue v2 = right.eval(e);
            ((CellValue)v1).setVal(v2);
            return v2;
        }
        
        throw new InterpreterError(LHS_NOT_REFERENCE);
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        left.compile(c, e);
        c.emit( JVMCodes.DUP );
        right.compile(c, e);
        String put_field = String.format(JVMCodes.PUTVALUE, leftType.getJVMName(), TypeRef.FIELD_NAME, rightType.getJVMType());
        c.emit( put_field );        
        //To leave the assigned value on top of the stack
        String get_field = String.format(JVMCodes.GETFIELD, leftType.getJVMName(), TypeRef.FIELD_NAME, rightType.getJVMType());
        c.emit( get_field );
        
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType t1 = left.typecheck(e);
        IType t2 = right.typecheck(e);
        if(t1 instanceof TypeRef){
            IType t1_ref = ((TypeRef)t1).getReference();
            if( t1_ref.sameType(t2) ){
                leftType = t1;
                rightType = t2;
                return t2;
            }
            throw new TypeCheckerError(REFED_TYPE_MISMATCH);
        }

        throw new TypeCheckerError(LHS_NOT_REFERENCE);
    }
    
}
