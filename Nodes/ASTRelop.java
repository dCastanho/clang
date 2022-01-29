package Nodes;


import Parser.ParserConstants;
import Parser.Token;
import Types.IType;
import Types.TypeBool;
import Types.TypeInt;
import Util.CodeBlock;
import Util.Coordinates;
import Util.Environment;
import Util.Generator;
import Util.JVMCodes;
import Values.BoolValue;
import Values.IValue;
import Values.IntValue;
import Exceptions.InterpreterError;
import Exceptions.TypeCheckerError;

/**
 * Class that represents the ASTNode with relation operation between two integers
 * @author Daniel Castanho, 55078
 * @author Eric Solcan, 55562
 */
public class ASTRelop implements ASTShortCircuitNode{


    private static final String TYPE_MISMATCH = "Type mismatch on %s operator";

    //Should never be seen, unless something is wrong with parser
    private static final String UNKNOWN_OPERATOR = "Unknown operator";
    private ASTNode lhs;
    private ASTNode rhs;
    private Token token;

    public ASTRelop(ASTNode l, ASTNode r, Token t) {
        lhs = l;
        rhs = r;
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
                return performOperation(l, r) ? BoolValue.TRUE : BoolValue.FALSE;
            }
        }

        throw new InterpreterError(String.format(TYPE_MISMATCH, token.image));
        
    }

    @Override
    public void compile(CodeBlock c, Environment<Coordinates> e) {
        String labelOne = Generator.genLabelName();
        String labelTwo = Generator.genLabelName();
        
        lhs.compile(c, e);
        rhs.compile(c, e);
        c.emit( JVMCodes.SUB );
        c.emit( String.format( compareJump(), labelOne));
        c.emit( JVMCodes.PUSH_FALSE);
        c.emit( String.format(JVMCodes.GOTO, labelTwo) );
        c.emit( labelOne + ":" );
        c.emit( JVMCodes.PUSH_TRUE );
        c.emit( labelTwo + ":" );        
    }

    /**
     * Performs the node's operation on two given values
     * @param v1 the left value
     * @param v2 the right value
     * @return the result
     */
    private boolean performOperation(int v1, int v2){
        switch(token.kind) {
            case ParserConstants.EQUALS:
                return v1 == v2;
            case ParserConstants.DIFF:
                return v1 != v2;
            case ParserConstants.GREATER:
                return v1 > v2;
            case ParserConstants.SMALLER:
                return v1 < v2;
            case ParserConstants.EQ_GREATER:
                return v1 >= v2;
            case ParserConstants.EQ_SMALLER:
                return v1 <= v2;
            default:
                throw new InterpreterError(UNKNOWN_OPERATOR);
        }
    }

    @Override
    public IType typecheck(Environment<IType> e) {
        IType t1 = lhs.typecheck(e);
        IType t2 = rhs.typecheck(e);
        if(t1 instanceof TypeInt && t2 instanceof TypeInt)
            return TypeBool.BOOL_TYPE; 

        throw new TypeCheckerError( String.format(TYPE_MISMATCH, token.image) );
    }

    @Override
    public void compileSC(CodeBlock c, Environment<Coordinates> e, String trueL, String falseL) {
        lhs.compile(c, e);
        rhs.compile(c, e);
        c.emit( JVMCodes.SUB );
        c.emit( String.format(compareJump(), trueL));
        c.emit( String.format(JVMCodes.GOTO, falseL));

    }

    /**
     * Gets the appropriate jump instruction for the comparison operator
     * @return the JVM code of the jump
     */
    private String compareJump(){
        switch(token.kind) {
            case ParserConstants.EQUALS:
                return JVMCodes.IFEQ; //( v1 - v2 = 0 -> v1 == v2 )
            case ParserConstants.GREATER:
                return JVMCodes.IFGT; //( v1 - v2 > 0 -> v1 > v2 )
            case ParserConstants.SMALLER:
                return JVMCodes.IFLT; //( v1 - v2 < 0 -> v1 < v2 )
            case ParserConstants.EQ_GREATER:
                return JVMCodes.IFGE; //( v1 - v2 >= 0 -> v1 >= v2 )
            case ParserConstants.EQ_SMALLER:
                return JVMCodes.IFLE; //( v1 - v2 <= 0 -> v1 <= v2 )
            case ParserConstants.DIFF:
                return JVMCodes.IFNE; //( v1 - v2 <= 0 -> v1 <= v2 )
            default:
                throw new InterpreterError(UNKNOWN_OPERATOR);
        }
    }
    
}
