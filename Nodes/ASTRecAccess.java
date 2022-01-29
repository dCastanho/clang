package Nodes;

import Types.IType;
import Types.TypeRecord;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.JVMCodes;
import Values.IValue;
import Values.RecordValue;
import Exceptions.InterpreterError;
import Exceptions.TypeCheckerError;

/**
 * Class that represents the access to a record's field.
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTRecAccess implements ASTNode{

    private static final String NOT_RECORD = "Type mismatch, cannot access field of a non record value";
    private static final String NOT_FIELD = "%s is not a field identifier of this record";

    private ASTNode child;
    private String field;
    private TypeRecord childType;
    private IType thisType;

    public ASTRecAccess(ASTNode c,String f) {
        child = c;
        field = f;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue v = child.eval(e);
        if(v instanceof RecordValue){
            RecordValue rec = (RecordValue)v;
            return rec.getFields().get(field);
        }

        throw new InterpreterError( NOT_RECORD );
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        child.compile(c, e);

        String childJVMName = childType.getJVMName();
        String thisJVMType = thisType.getJVMType();

        c.emit( String.format(JVMCodes.GETFIELD, childJVMName, field, thisJVMType) );        
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType ctype = child.typecheck(e);

        if(ctype instanceof TypeRecord){
            childType = (TypeRecord)ctype;
            IType f = childType.getFields().get(field);

            if(f == null)
                throw new TypeCheckerError( String.format(NOT_FIELD, field));

            thisType = f;
            return thisType;
        }

        throw new TypeCheckerError(NOT_RECORD);
    }
    
}
