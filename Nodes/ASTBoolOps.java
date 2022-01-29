package Nodes;

import Parser.ParserConstants;
import Parser.Token;
import Types.IType;
import Types.TypeBool;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.Generator;
import Util.JVMCodes;
import Values.BoolValue;
import Values.IValue;
import Exceptions.InterpreterError;
import Exceptions.TypeCheckerError;
/**
 * Class representing an ASTNode for operation between boolean values (<code> && </code>/<code> || </code>)
 * Both sides must represent boolean values
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTBoolOps implements ASTShortCircuitNode{



    private static final String TYPE_MISMATCH = "Type mismatch in %s operator";
    private static final String UNKNOWN_OPERATOR = "Unknown operator";

    private ASTNode lhs;
    private ASTNode rhs;
    private Token token;


    public ASTBoolOps(ASTNode ln, ASTNode rn, Token t) {
        lhs = ln;
        rhs = rn;
        token = t;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue v1= lhs.eval(e);
        if(v1 instanceof BoolValue){
            IValue v2 = rhs.eval(e);
            if(v2 instanceof BoolValue){
                boolean b1 = ((BoolValue)v1).getValue();
                boolean b2 = ((BoolValue)v2).getValue();
                return BoolValue.createValue( performOperation(b1, b2) );
            }
        }

        throw new InterpreterError( String.format(TYPE_MISMATCH, token.image) );
    }
    
    /**
     * Performs the correct operation depending on the token of the node
     * @param b1 left value
     * @param b2 right value
     * @return result
     */
    private boolean performOperation(boolean b1, boolean b2) {
        switch(token.kind) {
            case ParserConstants.AND:
                return b1 && b2;
            case ParserConstants.OR:
                return b1 || b2;
            default:
                throw new InterpreterError(UNKNOWN_OPERATOR);
        }
    }

    /**
     * Returns the JVM bytecode appropriate to the operation to be performed by the node
     * @return JVM code for OR or AND operation, whichever is used by the node at the time.
     */
    private String jvmOperation() {
        switch(token.kind) {
            case ParserConstants.AND:
                return JVMCodes.AND;
            case ParserConstants.OR:
                return JVMCodes.OR;
            default:
                throw new InterpreterError(UNKNOWN_OPERATOR);
        }
    }


    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        this.lhs.compile(c, e);
        this.rhs.compile(c, e);
        c.emit( jvmOperation() );
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType t1 = lhs.typecheck(e);
        IType t2 = rhs.typecheck(e);
        if(t1 instanceof TypeBool && t2 instanceof TypeBool)
            return t1;

        throw new TypeCheckerError( String.format(TYPE_MISMATCH, token.image) );
    }

    @Override
    public void compileSC(CodeBlock c, Environment<Coordinates> e, String trueL, String falseL) {
        ASTShortCircuitNode lSC = (ASTShortCircuitNode)lhs;
        ASTShortCircuitNode rSC = (ASTShortCircuitNode)rhs;
        String auxlabel = Generator.genLabelName();
        switch(token.kind) {
            
            case ParserConstants.AND:
                lSC.compileSC(c, e, auxlabel, falseL);
                c.emit(auxlabel + ":");
                rSC.compileSC(c, e, trueL, falseL);
                break;

            case ParserConstants.OR:
                lSC.compileSC(c, e, trueL, auxlabel);
                c.emit(auxlabel + ":");
                rSC.compileSC(c, e, trueL, falseL);
                break;

            default:
                throw new InterpreterError(UNKNOWN_OPERATOR);
        }
        
    }

    
    
}
