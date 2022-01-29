package Nodes;

import Parser.ParserConstants;
import Parser.Token;
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
 * Class that represents the ASTNode of an
 * operation between two <b>integers</b>
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTOperator implements ASTNode{


    private static final String NOT_SUPPORTED = "Operation %s only supported between integer values";

    //Should never be launched, parser is wrong if it does.
    private static final String UNKNOWN_OPERATOR = "Unknown operator";

    private ASTNode lhs;
    private ASTNode rhs;
    private Token token;

    public ASTOperator(ASTNode ln, ASTNode rn, Token t) {
        lhs = ln;
        rhs = rn;
        token = t;
    }

    @Override
    public IValue eval(Environment<IValue> e) {
        IValue v1 = lhs.eval(e);
        if(v1 instanceof IntValue){
            IValue v2 = rhs.eval(e);
            if(v2 instanceof IntValue) {
                int l = ((IntValue)v1).getValue();
                int r = ((IntValue)v2).getValue();
                return new IntValue(performOperationInts(l, r));
            }
        }

        throw new InterpreterError( String.format(NOT_SUPPORTED, token.image) );
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        lhs.compile(c, e);
        rhs.compile(c, e);
        c.emit(operation());
        
    }

    /**
     * Performs the node's operation (<code>+</code>,<code>-</code>,<code>*</code>,<code>/</code>) between two given integers
     * @param v1 left value
     * @param v2 right value
     * @return the result of the operation
     */
    private int performOperationInts(int v1, int v2){
        switch(token.kind) {
            case ParserConstants.PLUS:
                return v1 + v2;
            case ParserConstants.MINUS:
                return v1 - v2;
            case ParserConstants.MUL:
                return v1 * v2;
            case ParserConstants.DIV:
                return v1 / v2;
            default:
                throw new InterpreterError(UNKNOWN_OPERATOR);
        }
    }


    private String operation(){
        switch(token.kind) {
            case ParserConstants.PLUS:
                return JVMCodes.ADD;
            case ParserConstants.MINUS:
                return JVMCodes.SUB;
            case ParserConstants.MUL:
                return JVMCodes.MULT;
            case ParserConstants.DIV:
                return JVMCodes.DIV;
            default:
                throw new InterpreterError(UNKNOWN_OPERATOR);
        }
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType t1 = lhs.typecheck(e);
        IType t2 = rhs.typecheck(e);
        if(t1 instanceof TypeInt && t2 instanceof TypeInt)
            return t1;

        throw new TypeCheckerError( String.format(NOT_SUPPORTED, token.image));
    }
    
    
}
