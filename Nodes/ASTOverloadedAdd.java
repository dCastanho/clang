package Nodes;


import Types.IType;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.Overloader;
import Values.IValue;
import Exceptions.InterpreterError;
import Exceptions.TypeCheckerError;

/**
 * Class that represents an overloaded addition ASNode,
 * which means we can add two values of different types,
 * such as string, ints and booleans. 
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTOverloadedAdd implements ASTNode{


    private static final String MISMATCH_TYPES = "Mismatch types on operator +";
    private ASTNode lhs;
    private ASTNode rhs;

    private IType leftType;
    private IType rightType;

    public ASTOverloadedAdd(ASTNode l, ASTNode r) {
        lhs = l;
        rhs = r;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue leftValue = lhs.eval(e);
        if( Overloader.isValid(leftValue) ){
            IValue rightValue = rhs.eval(e);
            IValue res = Overloader.evalArguments(leftValue, rightValue);

            if(res != null)
                return res;
        }

        throw new InterpreterError(MISMATCH_TYPES);
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        lhs.compile(c, e);
        rhs.compile(c, e);
        Overloader.compileArguments(c, leftType, rightType);
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        leftType = lhs.typecheck(e);
       if (Overloader.isValid(leftType)){
           rightType = rhs.typecheck(e);
           IType res = Overloader.typecheckArguments(leftType, rightType);

           if( res != null)
            return res;
       }

        throw new TypeCheckerError(MISMATCH_TYPES);
    }
    
}
